package com.viruss.waw.utils.recipes;

import com.viruss.waw.Main;
import com.viruss.waw.utils.recipes.bases.MortarRecipe;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.event.RegistryEvent;

public class RecipeTypes {

    public static void registerRecipes(RegistryEvent.Register<RecipeSerializer<?>> event){
        Mortar.register(event);
    }

    public static class Mortar {
        public static final RecipeType<MortarRecipe> TYPE = new RecipeType<>() {
            @Override
            public String toString() {
                return Main.MOD_ID + ":mortar";
            }
        };
        public static final RecipeSerializer<MortarRecipe> SERIALIZER = new MortarRecipe.Serializer();

        private static void register(RegistryEvent.Register<RecipeSerializer<?>> event) {
            Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(RecipeTypes.Mortar.TYPE.toString()), RecipeTypes.Mortar.TYPE);
            event.getRegistry().register(RecipeTypes.Mortar.SERIALIZER);
        }
    }

}
