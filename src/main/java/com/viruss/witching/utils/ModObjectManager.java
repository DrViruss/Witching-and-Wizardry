package com.viruss.witching.utils;

import com.viruss.witching.blocks.RubyBlock;
import com.viruss.witching.blocks.bases.LogBase;
import com.viruss.witching.items.BlockItemBase;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashMap;
import java.util.Map;

public class ModObjectManager {

    public static Map<String, Block> Blocks_Map = new HashMap<>();
    public static Map<String, Item> Items_Map = new HashMap<>();

    public static void initObjects()
    {
        //not a fuel
        //do not burns
        generateBlocks(new String[]{
                "ruby_block",
                "ash_log"
                /* add smth*/
        },new Block[]{
                new RubyBlock(),
                new LogBase()
                /* add smth*/
        });


        //Items

    }

    private static void generateBlocks(String[] regnames,Block[] blocks)
    {
        for (int i=0; i< regnames.length; i++)
        {
            Blocks_Map.put(regnames[i],blocks[i]);
            Items_Map.put(regnames[i],new BlockItemBase(blocks[i]));
        }
    }

    public static void registerBlocks(IForgeRegistry<Block> register)
    {
        String[] names = Blocks_Map.keySet().toArray(new String[0]);
        for (String name : names)
            register.register(Blocks_Map.get(name).setRegistryName(name));
    }

    public static void registerItems(IForgeRegistry<Item> register)
    {
        String[] names = Items_Map.keySet().toArray(new String[0]);
        for (String name : names)
            register.register(Items_Map.get(name).setRegistryName(name));
    }

}
