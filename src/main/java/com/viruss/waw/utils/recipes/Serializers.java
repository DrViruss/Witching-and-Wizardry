package com.viruss.waw.utils.recipes;

import com.viruss.waw.utils.recipes.types.MortarAndPestle;
import net.minecraft.item.crafting.IRecipeSerializer;

public class Serializers {
    public static final IRecipeSerializer<MortarAndPestle> mortar_and_pestle = new MortarAndPestle.Serializer() {};
}
