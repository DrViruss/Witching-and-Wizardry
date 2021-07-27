package com.viruss.waw.utils.datagen;

import com.viruss.waw.common.objects.packs.WoodenObject;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.registry.Registry;

import java.util.Set;
import java.util.function.Consumer;

public class RecipeProvider extends net.minecraft.data.RecipeProvider {
    private final Set<WoodenObject> woodenObjects;
    public RecipeProvider(DataGenerator generator,Set<WoodenObject> woods) {
        super(generator);
        this.woodenObjects = woods;
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> recipeConsumer) {
       for (WoodenObject wood : woodenObjects)
           generateWoodRecipes(recipeConsumer,wood);
    }

    private void generateWoodRecipes(Consumer<IFinishedRecipe> recipeConsumer,WoodenObject wood){
        shapelessPlanksNew(recipeConsumer, wood.getPlanks().getPrimary(), wood.getLogsItemTag());
        shapelessStrippedToPlanks(recipeConsumer,wood.getWood().getPrimary() ,wood.getLog().getPrimary());

        if(wood.getStrippedLog() != null)
            shapelessStrippedToPlanks(recipeConsumer, wood.getStrippedWood().getPrimary(), wood.getStrippedLog().getPrimary());

        shapelessWoodenButton(recipeConsumer, wood.getButton().getPrimary(), wood.getPlanks().getPrimary());
        shapedWoodenDoor(recipeConsumer, wood.getDoor().getPrimary(), wood.getPlanks().getPrimary());
        shapedWoodenFence(recipeConsumer, wood.getFence().getPrimary(), wood.getPlanks().getPrimary());
        shapedWoodenFenceGate(recipeConsumer, wood.getGate().getPrimary(), wood.getPlanks().getPrimary());
        shapedWoodenPressurePlate(recipeConsumer, wood.getPressure_plate().getSecondary(), wood.getPlanks().getPrimary());
        shapedWoodenSlab(recipeConsumer,wood.getSlab().getSecondary(), wood.getPlanks().getPrimary());
        shapedWoodenStairs(recipeConsumer,wood.getStairs().getSecondary(), wood.getPlanks().getPrimary());
        shapedWoodenTrapdoor(recipeConsumer,wood.getTrapdoor().getSecondary(), wood.getPlanks().getPrimary());
        shapedSign(recipeConsumer, wood.getSign().getItem(), wood.getPlanks().getPrimary());
        shapedBoat(recipeConsumer, wood.getBoat().get(), wood.getPlanks().getPrimary());
    }



    private static void shapelessPlanksNew(Consumer<IFinishedRecipe> reipceConsumer, IItemProvider result, ITag<Item> wood) {
        ShapelessRecipeBuilder.shapeless(result, 4).requires(wood).group("planks")
                .unlockedBy("has_log", has(wood)).save(reipceConsumer);
    }

    private static void shapelessStrippedToPlanks(Consumer<IFinishedRecipe> reipceConsumer, IItemProvider result, IItemProvider p_240471_2_) {
        ShapedRecipeBuilder.shaped(result, 3).define('#', p_240471_2_).pattern("##").pattern("##")
                .group("bark").unlockedBy("has_log", has(p_240471_2_)).save(reipceConsumer);
    }

    private static void shapedBoat(Consumer<IFinishedRecipe> reipceConsumer, IItemProvider result, IItemProvider planks) {
        ShapedRecipeBuilder.shaped(result).define('#', planks).pattern("# #").pattern("###").group("boat")
                .unlockedBy("in_water", insideOf(Blocks.WATER)).save(reipceConsumer);
    }

    private static void shapelessWoodenButton(Consumer<IFinishedRecipe> reipceConsumer, IItemProvider result, IItemProvider p_240474_2_) {
        ShapelessRecipeBuilder.shapeless(result).requires(p_240474_2_).group("wooden_button")
                .unlockedBy("has_planks", has(p_240474_2_)).save(reipceConsumer);
    }

    private static void shapedWoodenDoor(Consumer<IFinishedRecipe> reipceConsumer, IItemProvider result, IItemProvider p_240475_2_) {
        ShapedRecipeBuilder.shaped(result, 3).define('#', p_240475_2_).pattern("##").pattern("##")
                .pattern("##").group("wooden_door").unlockedBy("has_planks", has(p_240475_2_))
                .save(reipceConsumer);
    }

    private static void shapedWoodenFence(Consumer<IFinishedRecipe> reipceConsumer, IItemProvider result, IItemProvider p_240476_2_) {
        ShapedRecipeBuilder.shaped(result, 3).define('#', Items.STICK).define('W', p_240476_2_).pattern("W#W")
                .pattern("W#W").group("wooden_fence").unlockedBy("has_planks", has(p_240476_2_))
                .save(reipceConsumer);
    }

    private static void shapedWoodenFenceGate(Consumer<IFinishedRecipe> reipceConsumer, IItemProvider result, IItemProvider p_240477_2_) {
        ShapedRecipeBuilder.shaped(result).define('#', Items.STICK).define('W', p_240477_2_).pattern("#W#")
                .pattern("#W#").group("wooden_fence_gate").unlockedBy("has_planks", has(p_240477_2_))
                .save(reipceConsumer);
    }

    private static void shapedWoodenPressurePlate(Consumer<IFinishedRecipe> reipceConsumer, IItemProvider result, IItemProvider p_240478_2_) {
        ShapedRecipeBuilder.shaped(result).define('#', p_240478_2_).pattern("##")
                .group("wooden_pressure_plate").unlockedBy("has_planks", has(p_240478_2_))
                .save(reipceConsumer);
    }

    private static void shapedWoodenSlab(Consumer<IFinishedRecipe> reipceConsumer, IItemProvider result, IItemProvider p_240479_2_) {
        ShapedRecipeBuilder.shaped(result, 6).define('#', p_240479_2_).pattern("###").group("wooden_slab")
                .unlockedBy("has_planks", has(p_240479_2_)).save(reipceConsumer);
    }

    private static void shapedWoodenStairs(Consumer<IFinishedRecipe> reipceConsumer, IItemProvider result, IItemProvider p_240480_2_) {
        ShapedRecipeBuilder.shaped(result, 4).define('#', p_240480_2_).pattern("#  ").pattern("## ")
                .pattern("###").group("wooden_stairs").unlockedBy("has_planks", has(p_240480_2_))
                .save(reipceConsumer);
    }

    private static void shapedWoodenTrapdoor(Consumer<IFinishedRecipe> reipceConsumer, IItemProvider result, IItemProvider p_240481_2_) {
        ShapedRecipeBuilder.shaped(result, 2).define('#', p_240481_2_).pattern("###").pattern("###")
                .group("wooden_trapdoor").unlockedBy("has_planks", has(p_240481_2_)).save(reipceConsumer);
    }

    private static void shapedSign(Consumer<IFinishedRecipe> reipceConsumer, IItemProvider result, IItemProvider planks) {
        String s = Registry.ITEM.getKey(planks.asItem()).getPath();
        ShapedRecipeBuilder.shaped(result, 3).group("sign").define('#', planks).define('X', Items.STICK)
                .pattern("###").pattern("###").pattern(" X ").unlockedBy("has_" + s, has(planks))
                .save(reipceConsumer);
    }
}
