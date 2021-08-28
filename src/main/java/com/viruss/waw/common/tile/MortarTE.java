package com.viruss.waw.common.tile;

import com.viruss.waw.utils.recipes.RecipeTypes;
import com.viruss.waw.utils.recipes.bases.MortarRecipe;
import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

@SuppressWarnings("all")
public class MortarTE extends NetworkTileEntity {
    private byte hits=0;
    private MortarRecipe recipe = null;

    final SimpleContainer inventory = new SimpleContainer(4){
        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public void setChanged() {
            MortarTE.this.updateNetwork();
            recipe = null;
        }

        @Override
        public ItemStack removeItem(int slot, int amount) {
            if(slot>0)
                return super.removeItem(slot, amount);
            ItemStack result = super.getItem(0).copy();
            clearContent();
            return result;
        }
    };

    public MortarTE(BlockPos pos, BlockState state) {
        super(ModRegistry.MORTAR_AND_PESTLE.getMortarTE(), pos, state);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.inventory.clearContent();
        super.handleUpdateTag(tag);
    }

    @Override
    public void loadNetwork(CompoundTag tag) {
        this.inventory.fromTag(tag.getList("inventory",10));
    }

    @Override
    public CompoundTag saveNetwork(CompoundTag tag) {
        tag.put("inventory",inventory.createTag());
        return super.saveNetwork(tag);
    }

    public SimpleContainer getInventory() {
        return inventory;
    }

    public void craft(Player player) {
        if(level == null || level.isClientSide()) return;
        if(recipe == null) {
            Optional<MortarRecipe> optional = this.level.getRecipeManager().getRecipeFor(RecipeTypes.Mortar.TYPE, inventory, level);
            optional.ifPresent(mortarRecipe -> this.recipe = mortarRecipe);
            if(recipe == null) return;
        }

        hits +=1;
        if(this.hits >= recipe.getHits()){
            ItemStack result =recipe.assemble(inventory);
            inventory.clearContent();
            inventory.addItem(result);
        }
    }


}