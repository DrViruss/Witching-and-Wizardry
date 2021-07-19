package com.viruss.waw.utils;

import com.viruss.waw.WitchingAndWizardry;
import com.viruss.waw.common.objects.blocks.ChalkSet;
import com.viruss.waw.common.objects.blocks.WoodenObject;
import com.viruss.waw.common.objects.items.Chalk;
import com.viruss.waw.utils.registrations.MultyDeferredRegister;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class RegistryHandler {
    public static final MultyDeferredRegister MDR = new MultyDeferredRegister(WitchingAndWizardry.MOD_ID,new IForgeRegistry[]{
            ForgeRegistries.BLOCKS,
            ForgeRegistries.ITEMS,
            ForgeRegistries.ENTITIES,
            ForgeRegistries.TILE_ENTITIES
    });

    public static void init(IEventBus bus)
    {
        MDR.register(bus);
    }

    public static final WoodenObject ASH = new WoodenObject("ash",WitchingAndWizardry.ITEM_GROUP,true,true);
//    public static final DoubleRegisteredObject<Block,Item> MORTAR_AND_PESTLE = MDR.register("mortar_n_pestle",new com.viruss.waw.utils.registrations.Block.Builder().setBlockSup(MortarAndPestle::new).needItem(WitchingAndWizardry.ITEM_GROUP));
    public static final ChalkSet CHALKS = new ChalkSet(new Chalk.Type[]{
     Chalk.Type.DEFAULT
    });

    private static Item registerItem(String name, Item item)
    {
        MDR.register(name,()->item,ForgeRegistries.ITEMS);
        return item;
    }
    private static Block registerBlock(String name, Block block)
    {
        MDR.register(name,()->block,ForgeRegistries.BLOCKS);
        return block;
    }
}