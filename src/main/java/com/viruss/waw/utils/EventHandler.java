package com.viruss.waw.utils;

import com.viruss.waw.common.objects.blocks.WoodenObject;
import com.viruss.waw.common.worldgen.Features;
import net.minecraft.block.*;
import net.minecraft.client.renderer.Atlases;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.ArrayList;
import java.util.Objects;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class EventHandler {
    private static ArrayList<WoodenObject>  woodenObjects = new ArrayList<>();

    @SubscribeEvent
    public static void commonSetupEvent(final FMLCommonSetupEvent event) {
        Features.setup();

        for(WoodenObject wood: woodenObjects) {
            ComposterBlock.COMPOSTABLES.put(wood.getLeaves().getSecondary(), 0.3f);
            ComposterBlock.COMPOSTABLES.put(wood.getSapling().getSecondary(), 0.3f);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(Objects.requireNonNull(wood.getSapling().getPrimaryRO().getId()), wood::getPotted_sapling);
        }

//            ComposterBlock.COMPOSTABLES.put(RegistryHandler.ASH.getLeaves().getSecondary(), 0.3f);
//            ComposterBlock.COMPOSTABLES.put(RegistryHandler.ASH.getSapling().getSecondary(), 0.3f);
//            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(Objects.requireNonNull(RegistryHandler.ASH.getSapling().getPrimaryRO().getId()), RegistryHandler.ASH::getPotted_sapling);

    }

    public static void addWood(WoodenObject wood)
    {
        woodenObjects.add(wood);
    }
}
