package com.viruss.waw.compat.jei;

import com.viruss.waw.Main;
import com.viruss.waw.utils.recipes.RecipeTypes;
import com.viruss.waw.utils.registries.ModRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class Plugin implements IModPlugin {
    private final ResourceLocation id = new ResourceLocation(Main.MOD_ID,"jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return id;
    }


    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();

        registration.addRecipeCategories(new MortarCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(RecipeTypes.Mortar.TYPE), MortarCategory.ID);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModRegistry.GADGETS.getMortar()), MortarCategory.ID);
    }
}
