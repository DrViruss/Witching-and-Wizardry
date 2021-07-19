package com.viruss.waw.utils;

import com.viruss.waw.common.worldgen.Features;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.WoodType;
import net.minecraft.client.renderer.Atlases;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.ArrayList;
import java.util.Objects;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class EventHandler {

    @SubscribeEvent
    public static void commonSetupEvent(final FMLCommonSetupEvent event) {
        Features.setup();

        ComposterBlock.COMPOSTABLES.put(RegistryHandler.ASH.getLeaves().getSecondary(),0.3f);
        ComposterBlock.COMPOSTABLES.put(RegistryHandler.ASH.getSapling().getSecondary(),0.3f);
        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(Objects.requireNonNull(RegistryHandler.ASH.getSapling().getPrimaryRO().getId()), RegistryHandler.ASH::getPotted_sapling);

    }
}
