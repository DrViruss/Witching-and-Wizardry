package com.viruss.waw.utils;

import com.viruss.waw.WitchingAndWizardry;
import com.viruss.waw.common.objects.items.Chalk;
import com.viruss.waw.common.objects.packs.ChalkSet;
import com.viruss.waw.common.objects.packs.MortarAndPestle;
import com.viruss.waw.common.objects.packs.WoodenObject;
import com.viruss.waw.utils.registration.MultyDeferredRegister;
import com.viruss.waw.utils.registries.ModEntities;
import net.minecraft.block.Block;
import net.minecraft.item.Foods;
import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.TwoLayerFeature;
import net.minecraft.world.gen.foliageplacer.FancyFoliagePlacer;
import net.minecraft.world.gen.trunkplacer.FancyTrunkPlacer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.OptionalInt;

public class ModRegistry {
    public static final MultyDeferredRegister MDR = new MultyDeferredRegister(WitchingAndWizardry.MOD_ID,new IForgeRegistry[]{
            ForgeRegistries.BLOCKS,
            ForgeRegistries.ITEMS,
            ForgeRegistries.ENTITIES,
            ForgeRegistries.TILE_ENTITIES,
            ForgeRegistries.SOUND_EVENTS
    });

    public static void init(IEventBus bus)
    {
        MDR.register(bus);
    }


                            /*NOTE: add all generatedObjects to dataGen */
    public static final WoodenObject ASH = new WoodenObject("ash",WitchingAndWizardry.ITEM_GROUP,new FancyFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(4), 4),new FancyTrunkPlacer(5, 2, 0),new TwoLayerFeature(0, 0, 0, OptionalInt.of(4)),false, Heightmap.Type.MOTION_BLOCKING,true,240,2,12,3,6, Biome.Category.PLAINS);
    public static final WoodenObject SAMBUCUS = new WoodenObject("sambucus",WitchingAndWizardry.ITEM_GROUP,new FancyFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(4), 6),new FancyTrunkPlacer(4, 2, 0),new TwoLayerFeature(0, 0, 0, OptionalInt.of(4)),false, Heightmap.Type.MOTION_BLOCKING,true, Foods.SWEET_BERRIES,240,2,12,3,6, Biome.Category.PLAINS);


    public static final MortarAndPestle MORTAR_AND_PESTLE = new MortarAndPestle();

    public static final ChalkSet CHALKS = new ChalkSet(new Chalk.Type[]{
         Chalk.Type.WHITE,
         Chalk.Type.RED
    });

    public static final ModEntities ENTITIES = new ModEntities(ASH, SAMBUCUS);





                                /*~ Special Methods ~*/
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