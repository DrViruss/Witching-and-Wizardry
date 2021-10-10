package com.viruss.waw.utils.registries;

import com.viruss.waw.Main;
import com.viruss.waw.common.objects.packs.ChalkSet;
import com.viruss.waw.common.objects.packs.Gadgets;
import com.viruss.waw.common.objects.packs.Ingredients;
import com.viruss.waw.common.objects.packs.WoodenPack;
import com.viruss.waw.utils.recipes.RecipeTypes;
import com.viruss.waw.utils.registration.MultyDeferredRegister;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class ModRegistry {
    public static final MultyDeferredRegister MDR = new MultyDeferredRegister(Main.MOD_ID,new IForgeRegistry[]{
            ForgeRegistries.BLOCKS,
            ForgeRegistries.ITEMS,
            ForgeRegistries.ENTITIES,
            ForgeRegistries.BLOCK_ENTITIES,
            ForgeRegistries.SOUND_EVENTS,
    });

    public static void init(IEventBus bus)
    {
        MDR.register(bus);
        bus.addGenericListener(RecipeSerializer.class, RecipeTypes::registerRecipes);
    }

    //Woods
    public static final WoodenPack ASH = new WoodenPack("ash", Main.ITEM_GROUP,true, null,true);
    public static final WoodenPack SAMBUCUS = new WoodenPack("sambucus", Main.ITEM_GROUP,true, Foods.POISONOUS_POTATO,true);

    public static final Gadgets GADGETS = new Gadgets();
    public static final ChalkSet CHALKS = new ChalkSet();
    public static final EntityRegistry ENTITIES = new EntityRegistry(ASH,SAMBUCUS);
    public static final Ingredients INGREDIENTS = new Ingredients();
}
