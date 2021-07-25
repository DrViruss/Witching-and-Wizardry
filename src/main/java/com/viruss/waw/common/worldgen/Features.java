
package com.viruss.waw.common.worldgen;

import com.google.common.collect.Lists;
import com.viruss.waw.WitchingAndWizardry;
import com.viruss.waw.utils.ModRegistry;
import net.minecraft.block.Block;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FancyFoliagePlacer;
import net.minecraft.world.gen.placement.NoiseDependant;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.trunkplacer.FancyTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.function.Supplier;

public class Features {

    public static void setup() {
        init();

        for (Map.Entry<RegistryKey<Biome>, Biome> biomeEntry : WorldGenRegistries.BIOME.entrySet()) {
            Biome biome = biomeEntry.getValue();
            if (biome.getBiomeCategory().equals(Biome.Category.NETHER) || biome.getBiomeCategory().equals(Biome.Category.THEEND))
                return;

            if(biome.getBiomeCategory().equals(Biome.Category.PLAINS))
                for (ConfiguredFeature<?,?> feature: Trees.list)
                    addFeatures(biome,feature,GenerationStage.Decoration.VEGETAL_DECORATION);

            for (ConfiguredFeature<?,?> feature: Ores.list)
                addFeatures(biome, feature,GenerationStage.Decoration.UNDERGROUND_ORES);
        }
    }

    private static void init() {
//      Features.Ore.registerOreFeature("ore_tin",RegistryHandler.ORE_TIN.getPrimary(),9,64,20);

//        Features.Trees.addWorldGenFeature(Features.Trees.ASH,240,2);
    }

    public static class Ores {
        public static final List<ConfiguredFeature<?, ?>> list = new ArrayList<>();

        public static void registerOreFeature(String name_id, Block ore, int maxSize, int maxHeight, int veinsPerChunk)
        {
            list.add(registerWorldFeature(name_id,
                    Feature.ORE.configured(
                            new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ore.defaultBlockState(), maxSize))
                            .range(maxHeight)
                            .squared()
                            .chance(veinsPerChunk)
            ));
        }
    }

    public static class Trees{
        public static final List<ConfiguredFeature<?, ?>> list = new ArrayList<>();



//        public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> ASH = registerWorldFeature("ash_tree", Feature.TREE.configured(
//                (new BaseTreeFeatureConfig.Builder
//                        (
//                                new SimpleBlockStateProvider(ModRegistry.ASH.getLog().getPrimary().defaultBlockState()),
//                                new SimpleBlockStateProvider(ModRegistry.ASH.getLeaves().getPrimary().defaultBlockState()),
//                                new FancyFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(4), 4),
//                                new FancyTrunkPlacer(5, 2, 0),
//                                new TwoLayerFeature(0, 0, 0, OptionalInt.of(4))
//                        )
//                )
//                        .ignoreVines()
//                        .heightmap(Heightmap.Type.MOTION_BLOCKING)
//                        .build()
//        ));

//        public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> ELDER = registerWorldFeature("elder_tree", Feature.TREE.configured(
//        (new BaseTreeFeatureConfig.Builder
//            (
//                new SimpleBlockStateProvider(ModRegistry.ELDER.getLog().getPrimary().defaultBlockState()),
//                new SimpleBlockStateProvider(ModRegistry.ELDER.getLeaves().getPrimary().defaultBlockState()),
//                new FancyFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(4), 6),
//                new FancyTrunkPlacer(4, 2, 0),
//                new TwoLayerFeature(0, 0, 0, OptionalInt.of(4))
//            )
//        )
//        .ignoreVines()
//        .heightmap(Heightmap.Type.MOTION_BLOCKING)
//        .build()
//        ));




//        public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> ASH = registerWorldFeature("ash_tree", Feature.TREE.configured(
//        (new BaseTreeFeatureConfig.Builder
//            (
//                new SimpleBlockStateProvider(ModRegistry.ASH.getLog().getPrimary().defaultBlockState()),
//                new SimpleBlockStateProvider(ModRegistry.ASH.getLeaves().getPrimary().defaultBlockState()),
//                new FancyFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(4), 4),
//                new FancyTrunkPlacer(5, 2, 0),
//                new TwoLayerFeature(0, 0, 0, OptionalInt.of(4))
//            )
//        )
//        .ignoreVines()
//        .heightmap(Heightmap.Type.MOTION_BLOCKING)
//        .build()
//        ));



//        public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> ELDER = registerWorldFeature("elder_tree", Feature.TREE.configured(
//            (new BaseTreeFeatureConfig.Builder
//            (
//                new SimpleBlockStateProvider(ModRegistry.ELDER.getLog().getPrimary().defaultBlockState()),
//                new SimpleBlockStateProvider(ModRegistry.ELDER.getLeaves().getPrimary().defaultBlockState()),
//                new FancyFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(4), 6),
//                new FancyTrunkPlacer(4, 2, 0),
//                new TwoLayerFeature(0, 0, 0, OptionalInt.of(4))
//            )
//        )
//        .ignoreVines()
//        .heightmap(Heightmap.Type.MOTION_BLOCKING)
//        .build()
//        ));

//OakTree

        public static void addWorldGenFeature(ConfiguredFeature<BaseTreeFeatureConfig, ?> feature,int chance, int countRandom)
        {
            ConfiguredFeature<?,?> configuredFeature = registerWorldFeature(feature.feature().getRegistryName().getPath()+"_wgen",feature
                    .chance(chance)
                    .countRandom(countRandom)
                    .squared()
                    .decorated(Placement.COUNT_NOISE.configured(new NoiseDependant(12,3,6)))
            );
            list.add(configuredFeature);
        }
    }


    private static void addFeatures(Biome biome, ConfiguredFeature<?, ?> configuredFeature, GenerationStage.Decoration decoration)
    {
        List<List<Supplier<ConfiguredFeature<?, ?>>>> biomeFeatures = new ArrayList<>(biome.getGenerationSettings().features());

        while (biomeFeatures.size() <=  decoration.ordinal())
            biomeFeatures.add(Lists.newArrayList());

        List<Supplier<ConfiguredFeature<?, ?>>> features = new ArrayList<>(biomeFeatures.get(decoration.ordinal()));
        features.add(() -> configuredFeature);
        biomeFeatures.set(decoration.ordinal(), features);
        ObfuscationReflectionHelper.setPrivateValue(BiomeGenerationSettings.class, biome.getGenerationSettings(), biomeFeatures,
                "field_242484_f");
    }

    public static <FC extends IFeatureConfig, F extends Feature<FC>> ConfiguredFeature<FC, F> registerWorldFeature(String rl, ConfiguredFeature<FC, F> feature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(WitchingAndWizardry.MOD_ID,rl), feature);
    }
}