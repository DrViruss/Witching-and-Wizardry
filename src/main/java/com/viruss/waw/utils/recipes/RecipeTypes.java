package com.viruss.waw.utils.recipes;

import com.viruss.waw.Main;
import com.viruss.waw.utils.recipes.bases.BrazierRecipe;
import com.viruss.waw.utils.recipes.bases.MortarRecipe;
import com.viruss.waw.utils.recipes.bases.RitualRecipe;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.event.RegistryEvent;

public class RecipeTypes {

    public static void registerRecipes(RegistryEvent.Register<RecipeSerializer<?>> event){
        Mortar.register(event);
        Rituals.register(event);
        Brazier.register(event);
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
            Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(TYPE.toString()), TYPE);
            event.getRegistry().register(SERIALIZER);
        }
    }

    public static class Rituals {
        public static final RecipeType<RitualRecipe> TYPE = new RecipeType<>() {
            @Override
            public String toString() {
                return Main.MOD_ID + ":rituals";
            }
        };
        public static final RecipeSerializer<RitualRecipe> SERIALIZER = new RitualRecipe.Serializer();

        private static void register(RegistryEvent.Register<RecipeSerializer<?>> event) {
            Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(TYPE.toString()), TYPE);
            event.getRegistry().register(SERIALIZER);
        }
    }

    public static class Brazier {
        public static final RecipeType<BrazierRecipe> TYPE = new RecipeType<>() {
            @Override
            public String toString() {
                return Main.MOD_ID + ":brazier";
            }
        };
        public static final RecipeSerializer<BrazierRecipe> SERIALIZER = new BrazierRecipe.Serializer();

        private static void register(RegistryEvent.Register<RecipeSerializer<?>> event) {
            Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(TYPE.toString()), TYPE);
            event.getRegistry().register(SERIALIZER);
        }
    }

}
