package com.viruss.waw.utils.recipes;

import com.viruss.waw.WitchingAndWizardry;
import com.viruss.waw.utils.recipes.types.MortarAndPestle;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public interface IModdedRecipeType<T extends IRecipe<?>>{
    IRecipeType<MortarAndPestle> mortar_and_pestle = register("mortar_and_pestle");

    static <T extends IRecipe<?>> IRecipeType<T> register(final String key) {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(WitchingAndWizardry.MOD_ID, key), new IRecipeType<T>() {
            public String toString() {
                return key;
            }
        });
    }

    default <C extends IInventory> Optional<T> matches(IRecipe<C> recipe, World worldIn, C inv) {
        return recipe.matches(inv, worldIn) ? Optional.of((T)recipe) : Optional.empty();
    }

    static <C extends IInventory, T extends IRecipe<C>> List<T> getRecipes(World world, IRecipeType<T> type) {
        return world.getRecipeManager().getAllRecipesFor(type);
    }
}