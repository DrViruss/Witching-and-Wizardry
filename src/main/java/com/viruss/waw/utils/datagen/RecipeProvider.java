package com.viruss.waw.utils.datagen;

import com.viruss.waw.common.objects.packs.WoodenPack;
import com.viruss.waw.utils.recipes.bases.MortarRecipe;
import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import java.util.Set;
import java.util.function.Consumer;

@SuppressWarnings("all")
public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {
    private Set<WoodenPack> woods;

    public RecipeProvider(DataGenerator p_125973_,Set<WoodenPack> woods) {
        super(p_125973_);
        this.woods = woods;
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        registerWood(consumer,woods);
        registerMortar(consumer);
    }

    private void registerWood(Consumer<FinishedRecipe> consumer, Set<WoodenPack> woods) {
        for(WoodenPack wood : woods){

            VanillaRecipes.shapelessPlanksNew(consumer, wood.getPlanks().getPrimary(), wood.getLogsItemTag());
            VanillaRecipes.shapelessStrippedToPlanks(consumer,wood.getWood().getPrimary() ,wood.getLog().getPrimary());

            if(wood.getStrippedLog() != null)
                VanillaRecipes.shapelessStrippedToPlanks(consumer, wood.getStrippedWood().getPrimary(), wood.getStrippedLog().getPrimary());

            VanillaRecipes.shapelessWoodenButton(consumer, wood.getButton().getPrimary(), wood.getPlanks().getPrimary());
            VanillaRecipes.shapedWoodenDoor(consumer, wood.getDoor().getPrimary(), wood.getPlanks().getPrimary());
            VanillaRecipes.shapedWoodenFence(consumer, wood.getFence().getPrimary(), wood.getPlanks().getPrimary());
            VanillaRecipes.shapedWoodenFenceGate(consumer, wood.getGate().getPrimary(), wood.getPlanks().getPrimary());
            VanillaRecipes.shapedWoodenPressurePlate(consumer, wood.getPressurePlate().getSecondary(), wood.getPlanks().getPrimary());
            VanillaRecipes.shapedWoodenSlab(consumer,wood.getSlab().getSecondary(), wood.getPlanks().getPrimary());
            VanillaRecipes.shapedWoodenStairs(consumer,wood.getStairs().getSecondary(), wood.getPlanks().getPrimary());
            VanillaRecipes.shapedWoodenTrapdoor(consumer,wood.getTrapdoor().getSecondary(), wood.getPlanks().getPrimary());
            VanillaRecipes.shapedSign(consumer, wood.getSign().getItem(), wood.getPlanks().getPrimary());
            VanillaRecipes.shapedBoat(consumer, wood.getBoat().get(), wood.getPlanks().getPrimary());
        }
    }
    private void registerMortar(Consumer<FinishedRecipe> consumer){
        /*
        * Only 1 item/Tag on ingredient
        * TODO: fix this ⬆️⬆️⬆️ (cause - JEI)
        *  Comment: think how to identify tagIngredient and itemIngredient
        */
        consumer.accept(new MortarRecipe.FinishedRecipe(
            5,
            ModRegistry.INGREDIENTS.getCalcosvis(),
            Ingredient.of(ModRegistry.CHALKS.getChalk()),
            Ingredient.of(Tags.Items.BONES),
            Ingredient.of(ModRegistry.INGREDIENTS.getMagicAsh())
        ));
    }

    private ItemStack colorfulChalk(int color){
        ItemStack stack = new ItemStack(ModRegistry.CHALKS.getChalk());
        CompoundTag tag = new CompoundTag();
        tag.putInt("color", color);
        stack.setTag(tag);
        return stack;
    }


    private static class VanillaRecipes {
        private static void shapelessPlanksNew(Consumer<FinishedRecipe> reipceConsumer, ItemLike result, Tag<Item> wood) {
            ShapelessRecipeBuilder.shapeless(result, 4).requires(wood).group("planks")
                    .unlockedBy("has_log", has(wood)).save(reipceConsumer);
        }

        private static void shapelessStrippedToPlanks(Consumer<FinishedRecipe> reipceConsumer, ItemLike result, ItemLike p_240471_2_) {
            ShapedRecipeBuilder.shaped(result, 3).define('#', p_240471_2_).pattern("##").pattern("##")
                    .group("bark").unlockedBy("has_log", has(p_240471_2_)).save(reipceConsumer);
        }

        private static void shapedBoat(Consumer<FinishedRecipe> reipceConsumer, ItemLike result, ItemLike planks) {
            ShapedRecipeBuilder.shaped(result).define('#', planks).pattern("# #").pattern("###").group("boat")
                    .unlockedBy("in_water", insideOf(Blocks.WATER)).save(reipceConsumer);
        }

        private static void shapelessWoodenButton(Consumer<FinishedRecipe> reipceConsumer, ItemLike result, ItemLike p_240474_2_) {
            ShapelessRecipeBuilder.shapeless(result).requires(p_240474_2_).group("wooden_button")
                    .unlockedBy("has_planks", has(p_240474_2_)).save(reipceConsumer);
        }

        private static void shapedWoodenDoor(Consumer<FinishedRecipe> reipceConsumer, ItemLike result, ItemLike p_240475_2_) {
            ShapedRecipeBuilder.shaped(result, 3).define('#', p_240475_2_).pattern("##").pattern("##")
                    .pattern("##").group("wooden_door").unlockedBy("has_planks", has(p_240475_2_))
                    .save(reipceConsumer);
        }

        private static void shapedWoodenFence(Consumer<FinishedRecipe> reipceConsumer, ItemLike result, ItemLike p_240476_2_) {
            ShapedRecipeBuilder.shaped(result, 3).define('#', Items.STICK).define('W', p_240476_2_).pattern("W#W")
                    .pattern("W#W").group("wooden_fence").unlockedBy("has_planks", has(p_240476_2_))
                    .save(reipceConsumer);
        }

        private static void shapedWoodenFenceGate(Consumer<FinishedRecipe> reipceConsumer, ItemLike result, ItemLike p_240477_2_) {
            ShapedRecipeBuilder.shaped(result).define('#', Items.STICK).define('W', p_240477_2_).pattern("#W#")
                    .pattern("#W#").group("wooden_fence_gate").unlockedBy("has_planks", has(p_240477_2_))
                    .save(reipceConsumer);
        }

        private static void shapedWoodenPressurePlate(Consumer<FinishedRecipe> reipceConsumer, ItemLike result, ItemLike p_240478_2_) {
            ShapedRecipeBuilder.shaped(result).define('#', p_240478_2_).pattern("##")
                    .group("wooden_pressure_plate").unlockedBy("has_planks", has(p_240478_2_))
                    .save(reipceConsumer);
        }

        private static void shapedWoodenSlab(Consumer<FinishedRecipe> reipceConsumer, ItemLike result, ItemLike p_240479_2_) {
            ShapedRecipeBuilder.shaped(result, 6).define('#', p_240479_2_).pattern("###").group("wooden_slab")
                    .unlockedBy("has_planks", has(p_240479_2_)).save(reipceConsumer);
        }

        private static void shapedWoodenStairs(Consumer<FinishedRecipe> reipceConsumer, ItemLike result, ItemLike p_240480_2_) {
            ShapedRecipeBuilder.shaped(result, 4).define('#', p_240480_2_).pattern("#  ").pattern("## ")
                    .pattern("###").group("wooden_stairs").unlockedBy("has_planks", has(p_240480_2_))
                    .save(reipceConsumer);
        }

        private static void shapedWoodenTrapdoor(Consumer<FinishedRecipe> reipceConsumer, ItemLike result, ItemLike p_240481_2_) {
            ShapedRecipeBuilder.shaped(result, 2).define('#', p_240481_2_).pattern("###").pattern("###")
                    .group("wooden_trapdoor").unlockedBy("has_planks", has(p_240481_2_)).save(reipceConsumer);
        }

        private static void shapedSign(Consumer<FinishedRecipe> reipceConsumer, ItemLike result, ItemLike planks) {
            String s = Registry.ITEM.getKey(planks.asItem()).getPath();
            ShapedRecipeBuilder.shaped(result, 3).group("sign").define('#', planks).define('X', Items.STICK)
                    .pattern("###").pattern("###").pattern(" X ").unlockedBy("has_" + s, has(planks))
                    .save(reipceConsumer);
        }
    }

}
