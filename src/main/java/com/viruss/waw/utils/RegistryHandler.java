package com.viruss.waw.utils;

import com.viruss.waw.WitchingAndWizardry;
import com.viruss.waw.common.objects.blocks.WoodenObject;
import com.viruss.waw.utils.registrations.MultyDeferredRegister;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class RegistryHandler {
    public static final MultyDeferredRegister MDR = new MultyDeferredRegister(new IForgeRegistry[]{
            ForgeRegistries.BLOCKS,
            ForgeRegistries.ITEMS,
            ForgeRegistries.ENTITIES,
            ForgeRegistries.TILE_ENTITIES
    });
    /*
    * sign
    * */
    public static void init(IEventBus bus)
    {
        MDR.register(bus);
    }

    public static final WoodenObject ASH = new WoodenObject("ash",WitchingAndWizardry.ITEM_GROUP,true,true);

    public static Boolean never(BlockState p_235427_0_, IBlockReader p_235427_1_, BlockPos p_235427_2_, EntityType<?> p_235427_3_) {
        return false;
    }
}