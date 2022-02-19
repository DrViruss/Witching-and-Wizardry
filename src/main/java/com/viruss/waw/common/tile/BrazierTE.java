package com.viruss.waw.common.tile;

import com.viruss.waw.utils.ModUtils;
import com.viruss.waw.utils.recipes.RecipeTypes;
import com.viruss.waw.utils.recipes.bases.BrazierRecipe;
import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;

import java.util.UUID;

import static com.viruss.waw.common.objects.blocks.BrazierBlock.LIT;

public class BrazierTE extends NetworkTileEntity {
    final SimpleContainer inventory = new SimpleContainer(4){
        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public void setChanged() {
            BrazierTE.this.updateNetwork();
        }
    };

    int litTime=0;
    Type type = Type.NONE;
    String load = "";
    Recipe<?> recipe;
    UUID playerID;

    public BrazierTE(BlockPos p_155229_, BlockState p_155230_) {
        super(ModRegistry.GADGETS.getBrazierTE(), p_155229_, p_155230_);
    }

    @Override
    public void loadNetwork(CompoundTag tag) {
        this.inventory.clearContent();
        this.inventory.fromTag(tag.getList("inventory",10));
    }

    @Override
    public CompoundTag saveNetwork(CompoundTag tag) {
        tag.put("inventory",inventory.createTag());
        return super.saveNetwork(tag);
    }

    public void use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand).copy();
        item.setCount(1);

        if(player.isShiftKeyDown() && !inventory.isEmpty())
            for (int i = inventory.getContainerSize()-1; i >=0 ; i--) {
                ItemStack extracted = inventory.removeItem(i, 1);
                if (extracted != ItemStack.EMPTY) {
                    player.addItem(extracted);
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 3f,3f);
                    return;
                }
            }

        if(item.isEmpty()) return;

        if(AbstractFurnaceBlockEntity.isFuel(item) && inventory.addItem(item).isEmpty())
            player.getItemInHand(hand).shrink(1);

    }

    public SimpleContainer getInventory() {
        return inventory;
    }

    private void updateRecipe() {
        assert level != null;
        clearRecipe();

        for(int i=0; i<inventory.getContainerSize();i++) {
            for (SmeltingRecipe recipe : level.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING))
                if (recipe.getIngredients().get(0).test(inventory.getItem(i))) {
                    this.type = Type.SMELTING;
                    this.recipe = recipe;
                    this.litTime = recipe.getCookingTime();
                    level.setBlock(getBlockPos(), getBlockState().setValue(LIT, true),11);
                    return;
                }
        }

        for (BrazierRecipe recipe : level.getRecipeManager().getAllRecipesFor(RecipeTypes.Brazier.TYPE))
            if (recipe.matches(inventory,level)) {
                this.type = Type.RITUAL;
                this.recipe = recipe;
                this.litTime = recipe.getTime();
                level.setBlock(getBlockPos(), getBlockState().setValue(LIT, true),11);
                return;
            }


        for(int i=0; i<inventory.getContainerSize();i++) {
            ItemStack stack = inventory.getItem(i);
            if(stack.isEmpty()) continue;
            litTime = ForgeHooks.getBurnTime(stack,null);
            level.setBlock(getBlockPos(), getBlockState().setValue(LIT, true),11);
            break;
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BrazierTE tile) {
        if (tile.isLit()) {
            if( (level.isRaining() && level.canSeeSky(pos) && level.getBiome(pos).getPrecipitation() != Biome.Precipitation.NONE ) ||  (level.getBlockState(pos.above()) != Blocks.AIR.defaultBlockState()) ) {
                level.setBlock(pos, state.setValue(LIT, false),11);
                tile.clearRecipe();
                return;
            }
            tile.updateLit(level, pos, state);
        }
    }

    public boolean isLit() {
        return this.litTime > 0;
    }

    private void updateLit(Level level, BlockPos pos, BlockState state){
        if(this.recipe instanceof BrazierRecipe brecipe)
            brecipe.getAction().tick(level,pos);

        --litTime;
        if(isLit()) return;

        if(!load.isEmpty()) {
            level.getRecipeManager().byKey(new ResourceLocation(load));
            load ="";
        }

        level.setBlockAndUpdate(pos,state.setValue(LIT,false));

        if(type == Type.RITUAL) {
            BrazierRecipe r = (BrazierRecipe)recipe;
            inventory.addItem(r.assemble(inventory));
            r.getAction().start(level,pos,inventory,playerID != null ? level.getPlayerByUUID(playerID) : null );
            clearRecipe();
            return;
        }
        else if(type ==Type.SMELTING) {
            inventory.setItem(ModUtils.Inventory.getSlotByItem(inventory,recipe.getIngredients()),((SmeltingRecipe)recipe).assemble(inventory));
            clearRecipe();
        }
        else{
            for(int i=0; i<inventory.getContainerSize();i++) {
                ItemStack stack = inventory.getItem(i);
                if(stack.isEmpty()) continue;
                inventory.setItem(i,ItemStack.EMPTY);
                break;
            }
        }

        updateRecipe();
    }

    public void clearRecipe(){
        this.litTime =0;
        this.type = Type.NONE;
        this.recipe = null;
        this.playerID = null;
    }

    public boolean tryLit(BlockState state, Level level, BlockPos pos, ServerPlayer player) {
        if(player!= null)
            this.playerID = player.getUUID();
        boolean test = level.getBlockState(pos.above()) != Blocks.AIR.defaultBlockState();
        if(isLit() || test || inventory.isEmpty()) return false;
        updateRecipe();
        return this.litTime > 0;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        litTime = nbt.getInt("litTime") ;
        if(nbt.contains("type"))
        type = Type.valueOf(nbt.getString("type"));
        if(nbt.contains("recipe"))
            load = nbt.getString("recipe");
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        nbt.putInt("litTime",litTime);
        nbt.putString("type",type.toString());
        if(recipe != null)
            nbt.putString("recipe", recipe.getId().toString());

        return super.save(nbt);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        assert level != null;
        if(!level.isClientSide())
            Containers.dropContents(level,getBlockPos(),inventory);
    }

    enum Type{
        NONE,
        SMELTING,
        RITUAL
    }
}
