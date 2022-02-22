package com.viruss.waw.common.tile;

import com.viruss.waw.utils.recipes.RecipeTypes;
import com.viruss.waw.utils.recipes.bases.MortarRecipe;
import com.viruss.waw.utils.recipes.bases.RitualRecipe;
import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;


public class CentralSymbolTE extends NetworkTileEntity {

private String load="";
private RitualRecipe ritual;
private int time =0;

    final SimpleContainer inventory = new SimpleContainer(6){
        @Override
        public void setChanged() {
            CentralSymbolTE.this.updateNetwork();
            CentralSymbolTE.this.setChanged();
        }
    };

    public CentralSymbolTE(BlockPos p_155229_, BlockState p_155230_) {
        super(ModRegistry.CHALKS.getCenterTE(), p_155229_, p_155230_);
    }

    @Override
    public void loadNetwork(CompoundTag tag) {
        this.inventory.clearContent();
        this.inventory.fromTag(tag.getList("inventory",10));
    }

    @Override
    public CompoundTag saveNetwork(CompoundTag tag) {
        tag.put("inventory",inventory.createTag());
        if(time >0)
            tag.putInt("time",time);
        if(ritual != null)
            tag.putString("ritual", ritual.getId().toString());
        return super.saveNetwork(tag);

    }

    public void use(BlockState state, Level level, BlockPos pos, Player player) {
        if(!inventory.isEmpty()) {
            Containers.dropContents(level, pos, inventory);
            return;
        }
        else
            level.getEntities(EntityType.ITEM, new AABB(pos).expandTowards(2, 0, 2), entity -> true).forEach(item -> {
                inventory.addItem(item.getItem());
                item.kill();
            });

        if(!load.isEmpty()) {
            level.getRecipeManager().byKey(new ResourceLocation(load)).ifPresent(recipe -> this.ritual = (RitualRecipe) recipe);
            load = "";
        }

        if(inventory.isEmpty()) return;

        ritual = level.getRecipeManager().getAllRecipesFor(RecipeTypes.Rituals.TYPE).stream().filter(ritualRecipe -> ritualRecipe.matches(inventory, level)).findFirst().orElse(null);
        if(ritual == null || !ritual.testAdditional(level,player,pos)) {
            player.displayClientMessage(new TranslatableComponent("waw.ritual.unknown"),true);
            Containers.dropContents(level,pos,inventory);
            resetRitual();
            return;
        }

        if(ritual.getAction().start(level,pos,inventory,player).isFinished())
            resetRitual();
        else
            this.time = ritual.getTime();

        setChanged();

    }

    public static void tick(Level level, BlockPos pos, BlockState state, CentralSymbolTE tile){
        if(tile.getRitual() != null)
            if((!tile.getRitual().getAction().tick(level,pos).isFinished() || tile.getTime() >0))
                tile.passTick();
            else
                tile.resetRitual();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        assert level != null;
        if(!level.isClientSide())
            Containers.dropContents(level,getBlockPos(),inventory);
    }

    public int getTime() {
        return time;
    }

    public RitualRecipe getRitual() {
        return ritual;
    }

    public void passTick(){
        this.time = this.time-1;
        setChanged();
    }

    public void resetRitual(){
        this.ritual = null;
        this.time =0;
        setChanged();
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.time = nbt.getInt("time");
        if(nbt.contains("ritual"))
            this.load=nbt.getString("ritual");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("time",time);
        if(ritual!=null)
            nbt.putString("ritual",ritual.getId().toString());
    }
}
