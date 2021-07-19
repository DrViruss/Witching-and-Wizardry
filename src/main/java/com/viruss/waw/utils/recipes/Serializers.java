package com.viruss.waw.utils.recipes;

import com.viruss.waw.utils.recipes.types.MortarAndPestleRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;

public class Serializers {
    public static final IRecipeSerializer<MortarAndPestleRecipe> mortar_and_pestle = new MortarAndPestleRecipe.Serializer() {};
}
