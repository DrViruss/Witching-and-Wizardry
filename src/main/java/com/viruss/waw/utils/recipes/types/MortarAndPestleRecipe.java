package com.viruss.waw.utils.recipes.types;

import com.google.gson.JsonObject;
import com.viruss.waw.utils.RegistryHandler;
import com.viruss.waw.utils.recipes.IModdedRecipeType;
import com.viruss.waw.utils.recipes.Serializers;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class MortarAndPestleRecipe implements IRecipe<IInventory> {
        private final Ingredient ingredient;
        private final ItemStack result;
        private final ResourceLocation recipeId;

    public MortarAndPestleRecipe(ResourceLocation recipeId, Ingredient ingredient, ItemStack result) {
            this.recipeId = recipeId;
            this.ingredient = ingredient;
            this.result = result;
        }

        public boolean matches(IInventory inv, World worldIn) {
            return this.ingredient.test(inv.getItem(0));
        }

    @Override
    public ItemStack assemble(IInventory p_77572_1_) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 1;
    }

    @Override
    public ItemStack getResultItem() {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient);
        return nonnulllist;
    }

    @Override
    public ItemStack getToastSymbol() {
//        return new ItemStack(RegistryHandler.MORTAR_AND_PESTLE.getSecondary());
        return new ItemStack(Blocks.ACACIA_LEAVES);
    }

    @Override
    public ResourceLocation getId() {
        return this.recipeId;
    }
    @Override
    public IRecipeSerializer<?> getSerializer() {
        return Serializers.mortar_and_pestle;
    }
    @Override
    public IRecipeType<?> getType() {
//        return IModdedRecipeType.mortar_and_pestle;
        return null;
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<MortarAndPestleRecipe> {
        @Override
        public MortarAndPestleRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "ingredient"));
            ItemStack itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
            return new MortarAndPestleRecipe(recipeId, ingredient, itemstack);
        }

        @Nullable
        @Override
        public MortarAndPestleRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack itemstack = buffer.readItem();
            return new MortarAndPestleRecipe(recipeId, ingredient, itemstack);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, MortarAndPestleRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
        }
    }
}
