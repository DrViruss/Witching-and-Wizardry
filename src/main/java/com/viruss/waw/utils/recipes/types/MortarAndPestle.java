package com.viruss.waw.utils.recipes.types;

import com.google.gson.JsonObject;
import com.viruss.waw.utils.ModRegistry;
import com.viruss.waw.utils.recipes.IModdedRecipeType;
import com.viruss.waw.utils.recipes.Serializers;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class MortarAndPestle implements IRecipe<IInventory> {

    private final Ingredient ingredient;
    private final ItemStack result;
    private final ResourceLocation recipeId;

    public MortarAndPestle(ResourceLocation recipeId, Ingredient ingredient, ItemStack result) {
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
        NonNullList<Ingredient> nonnull = NonNullList.create();
        nonnull.add(this.ingredient);
        return nonnull;
    }

    public ItemStack getIcon() {
        return new ItemStack(ModRegistry.MORTAR_AND_PESTLE.getMortar());
    }

    public ResourceLocation getId() {
        return this.recipeId;
    }

    public IRecipeSerializer<?> getSerializer() {
        return Serializers.mortar_and_pestle;
    }

    public IRecipeType<?> getType() {
        return IModdedRecipeType.mortar_and_pestle;
    }


    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<MortarAndPestle> {

        @Override
        public MortarAndPestle fromJson(ResourceLocation recipeId, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "ingredient"));
            ItemStack itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
            return new MortarAndPestle(recipeId, ingredient, itemstack);
        }

        @Nullable
        @Override
        public MortarAndPestle fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack itemstack = buffer.readItem();
            return new MortarAndPestle(recipeId, ingredient, itemstack);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, MortarAndPestle recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
        }
    }
}