package com.viruss.waw.utils.registries;

import com.viruss.waw.Main;
import com.viruss.waw.common.objects.items.Chalk;
import com.viruss.waw.common.objects.packs.ChalkSet;
import com.viruss.waw.common.objects.packs.MortarAndPestle;
import com.viruss.waw.common.objects.packs.WoodenObject;
import com.viruss.waw.utils.registration.MultyDeferredRegister;
import net.minecraft.world.food.Foods;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class ModRegistry {
    public static final MultyDeferredRegister MDR = new MultyDeferredRegister(Main.MOD_ID,new IForgeRegistry[]{
            ForgeRegistries.BLOCKS,
            ForgeRegistries.ITEMS,
            ForgeRegistries.ENTITIES,
            ForgeRegistries.BLOCK_ENTITIES,
            ForgeRegistries.SOUND_EVENTS
    });

    public static void init(IEventBus bus)
    {
        MDR.register(bus);
    }

    public static final WoodenObject ASH = new WoodenObject("ash", Main.ITEM_GROUP,true, null);
    public static final WoodenObject SAMBUCUS = new WoodenObject("sambucus", Main.ITEM_GROUP,true, Foods.POISONOUS_POTATO);

    public static final MortarAndPestle MORTAR_AND_PESTLE = new MortarAndPestle();

    public static final ChalkSet CHALKS = new ChalkSet(Chalk.Type.WHITE, Chalk.Type.RED);


    public static final EntityRegistry ENTITIES = new EntityRegistry(ASH,SAMBUCUS);
}
