package com.viruss.waw.common.tile;

import com.viruss.waw.utils.recipes.RecipeTypes;
import com.viruss.waw.utils.recipes.bases.MortarRecipe;
import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("all")
public class MortarTE extends NetworkTileEntity {
    private byte hits=0;
    private MortarRecipe recipe = null;
    private String loadedID = "";

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
    };

    public MortarTE(BlockPos pos, BlockState state) {
        super(ModRegistry.GADGETS.getMortarTE(), pos, state);
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

    public SimpleContainer getInventory() {
        return inventory;
    }

    public void craft(Player player) {
        if(level == null || level.isClientSide()) return;
        if(recipe == null) {
            if(loadedID.isBlank()) {
                this.level.getRecipeManager().getRecipeFor(RecipeTypes.Mortar.TYPE, inventory, level).ifPresent(mortarRecipe -> this.recipe = mortarRecipe);
                if (recipe == null) return;
            }
            level.getRecipeManager().byKey(new ResourceLocation(loadedID)).ifPresent(recipe -> this.recipe = (MortarRecipe) recipe);
        }

        hits +=1;
        if(this.hits >= recipe.getHits()){
            ItemStack result =recipe.assemble(inventory);
            inventory.clearContent();
            inventory.addItem(result);
            hits=0;
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        hits = nbt.getByte("hits");
        if(nbt.contains("recipe"))
            loadedID = nbt.getString("recipe");
        super.load(nbt);
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        nbt.putByte("hits",hits);
        if(recipe != null)
            nbt.putString("recipe",recipe.getId().toString());
        return super.save(nbt);
    }
}