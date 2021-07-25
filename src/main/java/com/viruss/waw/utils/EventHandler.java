package com.viruss.waw.utils;

import com.viruss.waw.common.objects.packs.WoodenObject;
import com.viruss.waw.common.worldgen.Features;
import com.viruss.waw.utils.datagen.*;
import net.minecraft.block.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.util.ArrayList;
import java.util.Objects;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class EventHandler {
    private static ArrayList<WoodenObject>  woodenObjects = new ArrayList<>();

        @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        net.minecraft.data.DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
            generator.addProvider(new RecipeProvider(generator));
            generator.addProvider(new ItemTagProvider(generator, event.getExistingFileHelper()));
            generator.addProvider(new BlockTagProvider(generator, event.getExistingFileHelper()));

            generator.addProvider(new DefaultLootProvider(generator));
        }
        if (event.includeClient()) {
            generator.addProvider(new BlockStateProvider(generator, event.getExistingFileHelper()));
        }
    }

    @SubscribeEvent
    public static void commonSetupEvent(final FMLCommonSetupEvent event) {
        Features.setup();

        for(WoodenObject wood: woodenObjects) {
            if(wood.getSapling()== null) continue;
            ComposterBlock.COMPOSTABLES.put(wood.getLeaves().getSecondary(), 0.3f);
            ComposterBlock.COMPOSTABLES.put(wood.getSapling().getSecondary(), 0.3f);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(Objects.requireNonNull(wood.getSapling().getPrimaryRO().getId()), wood::getPotted_sapling);
        }
    }

    public static void addWood(WoodenObject wood)
    {
        woodenObjects.add(wood);
    }
}
