package com.viruss.waw.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.viruss.waw.Main;
import com.viruss.waw.utils.recipes.bases.MortarRecipe;
import com.viruss.waw.utils.registries.ModRegistry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public class MortarCategory implements IRecipeCategory<MortarRecipe> {
    public static final ResourceLocation ID = new ResourceLocation(Main.MOD_ID,"mortar");
    private final IDrawable background;
    private final IDrawable icon;

    public MortarCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(new ResourceLocation(Main.MOD_ID, "textures/gui/jei/mortar.png"),3, 4, 155, 65);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModRegistry.GADGETS.getMortar()));
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<? extends MortarRecipe> getRecipeClass() {
        return MortarRecipe.class;
    }

    @Override
    public Component getTitle() {
        return ModRegistry.GADGETS.getMortar().getName();
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(MortarRecipe mortarRecipe, IIngredients iIngredients) {
        iIngredients.setInputIngredients(mortarRecipe.getIngredients());
        iIngredients.setOutput(VanillaTypes.ITEM, mortarRecipe.getResultItem());
    }

    @Override
    public void draw(MortarRecipe recipe, PoseStack stack, double mouseX, double mouseY) {
        String hits = new TranslatableComponent("waw.gui.hits").getString()+"  "+recipe.getHits();
        Font fontRenderer = Minecraft.getInstance().font;
        fontRenderer.draw(stack, hits, 100 - fontRenderer.width(hits) / 2, 0, Color.gray.getRGB());
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, MortarRecipe mortarRecipe, IIngredients iIngredients) {
        IGuiItemStackGroup guiItemStacks = iRecipeLayout.getItemStacks();


        guiItemStacks.init(0, true, 58, 0);
        guiItemStacks.set(0, iIngredients.getInputs(VanillaTypes.ITEM).get(0));

        guiItemStacks.init(1, true, 39, 0);
        guiItemStacks.set(1, iIngredients.getInputs(VanillaTypes.ITEM).get(1));

        guiItemStacks.init(2, true, 20, 0);
        guiItemStacks.set(2, iIngredients.getInputs(VanillaTypes.ITEM).get(2));

        guiItemStacks.init(3, true, 1, 0);
        guiItemStacks.set(3, iIngredients.getInputs(VanillaTypes.ITEM).get(3)); //java.util.List.of(mortarRecipe.getIngredients().get(3).getItems())


        guiItemStacks.init(4, false, 125, 30);
        guiItemStacks.set(4, iIngredients.getOutputs(VanillaTypes.ITEM).get(0));

    }
}
