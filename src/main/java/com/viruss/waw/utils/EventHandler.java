package com.viruss.waw.utils;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.viruss.waw.Main;
import com.viruss.waw.common.objects.packs.WoodenPack;
import com.viruss.waw.common.worldgen.Features;
import com.viruss.waw.utils.datagen.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import java.util.*;

@SuppressWarnings("all")
@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class EventHandler {
    private static final Set<WoodenPack> woods = new HashSet<>();

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        net.minecraft.data.DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
            generator.addProvider(new ItemTagProvider(generator, event.getExistingFileHelper(), woods));
            generator.addProvider(new BlockTagProvider(generator, event.getExistingFileHelper(), woods));
            generator.addProvider(new LootProvider(generator, woods,new HashMap<>())); //TODO: add MobLoot!
            generator.addProvider(new RecipeProvider(generator, woods));
            generator.addProvider(new BlockStateProvider(generator, event.getExistingFileHelper(), woods));
        }
    }

    @SubscribeEvent
    public static void commonSetupEvent(final FMLCommonSetupEvent event) {
        Features.setup();
        for(WoodenPack wood: woods) {
            if(wood.getSapling()== null) continue;
            ComposterBlock.COMPOSTABLES.put(wood.getLeaves().getSecondary(), 0.3f);
            ComposterBlock.COMPOSTABLES.put(wood.getSapling().getSecondary(), 0.3f);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(Objects.requireNonNull(wood.getSapling().getPrimaryRO().getId()), wood.getPottedSapling());
        }
    }

    @SubscribeEvent
    public static void commonSetupEvent(EntityAttributeCreationEvent event) {
        //        event.put(ModRegistry.ENTITIES.getOwl(), OwlEntity.createAttributes().build());
    }

    public static void addWood(WoodenPack wood)
    {
        woods.add(wood);
    }
}
