package com.viruss.waw.common.tile;

import com.viruss.waw.common.objects.blocks.BrazierBlock;
import com.viruss.waw.utils.ModUtils;
import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
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

public class BrazierTE extends NetworkTileEntity {
    final SimpleContainer inventory = new SimpleContainer(4){
        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public void setChanged() {
            BrazierTE.this.updateNetwork();
            recipe = "";
        }
    };

    int litTime=0;
    Type type = Type.NONE;
    String recipe = "";

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

        if(inventory.addItem(item).isEmpty())
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
                    setRecipe(recipe);
                    return;
                }
            //TODO: check rituals
        }
        for(int i=0; i<inventory.getContainerSize();i++) {
            ItemStack stack = inventory.getItem(i);
            if(stack.isEmpty()) continue;
            litTime = AbstractFurnaceBlockEntity.getFuel().getOrDefault(stack.getItem(),0);
            break;
        }
    }

    public static void brazierTick(Level level, BlockPos pos, BlockState state, BrazierTE tile) {
        if (!level.isClientSide() && tile.isLit()) {
            if( (level.isRaining() && level.canSeeSky(pos) && level.getBiome(pos).getPrecipitation() != Biome.Precipitation.NONE ) ||  (level.getBlockState(pos.above()) != Blocks.AIR.defaultBlockState()) ) {
                level.setBlock(pos, state.setValue(BrazierBlock.LIT, false),11);
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
        --litTime;
        if(isLit()) return;

        if(type ==Type.RECIPE) {
            level.getRecipeManager().byKey(new ResourceLocation(recipe)).ifPresent(value -> inventory.setItem(ModUtils.Inventory.getSlotByItem(inventory,value.getIngredients()),((SmeltingRecipe)value).assemble(inventory)));
            type = Type.NONE;
        }
        else
            for(int i=0; i<inventory.getContainerSize();i++) {
                if(inventory.getItem(i).isEmpty()) continue;
                inventory.removeItem(i,Integer.MAX_VALUE);
                break;
            }

        updateRecipe();

        if(!isLit())
            level.setBlockAndUpdate(pos,state.setValue(BrazierBlock.LIT,false));
    }

    public void clearRecipe(){
        litTime =0;
        type = Type.NONE;
        recipe = "";
    }

    public void setRecipe(Recipe<?> recipe) {
        type = Type.RECIPE;
        this.recipe = recipe.getId().toString();

        if(recipe instanceof SmeltingRecipe smeltingRecipe)
            litTime = smeltingRecipe.getCookingTime();
    }

    public boolean tryLit(BlockState state, Level level, BlockPos pos) {
        if(isLit() || level.getBlockState(pos.above()) != Blocks.AIR.defaultBlockState() || inventory.isEmpty()) return false;
        updateRecipe();
        return this.litTime > 0;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        litTime = nbt.getInt("litTime") ;
        if(nbt.contains("type"))
        type = Type.valueOf(nbt.getString("type"));
        if(type == Type.RECIPE)
            recipe = nbt.getString("recipe");

    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        nbt.putInt("litTime",litTime);
        nbt.putString("type",type.toString());
        if(type == Type.RECIPE)
            nbt.putString("recipe", recipe);

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
        RECIPE,
        RITUAL
    }
}
