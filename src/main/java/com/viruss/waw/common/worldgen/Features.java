package com.viruss.waw.common.worldgen;

import com.google.common.collect.ImmutableList;
import com.viruss.waw.Main;
import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;
import java.util.function.Supplier;

@SuppressWarnings("all")
public class Features {


    @SubscribeEvent
    public static void onBiomeLoad(BiomeLoadingEvent event) {
        Map<Biome.BiomeCategory,Set< ConfiguredFeature<?, ?>>> vegetals = new HashMap<>();
        Map<Biome.BiomeCategory,Set< ConfiguredFeature<?, ?>>> ores = new HashMap<>();

        init(ores,vegetals);
        if (event.getCategory().equals(Biome.BiomeCategory.NETHER) || event.getCategory().equals(Biome.BiomeCategory.THEEND))
            return;

        //vegetals
        vegetals.forEach((category, configuredFeatures) -> {
            if(event.getCategory().equals(category)) {
                List<Supplier<ConfiguredFeature<?, ?>>> features = event.getGeneration().getFeatures(GenerationStep.Decoration.VEGETAL_DECORATION);
                configuredFeatures.forEach(configuredFeature -> features.add(()->configuredFeature));
            }
        });

        //ores
        ores.forEach((category, configuredFeatures) -> {
            if(event.getCategory().equals(category)) {
                List<Supplier<ConfiguredFeature<?, ?>>> features = event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES);
                configuredFeatures.forEach(configuredFeature -> features.add(()->configuredFeature));
            }
        });
    }

    private static void init(Map<Biome.BiomeCategory,Set< ConfiguredFeature<?, ?>>> vegetals,Map<Biome.BiomeCategory,Set< ConfiguredFeature<?, ?>>> ores) {

        Ores.features.forEach((category, feature) -> {
            Set< ConfiguredFeature<?, ?>> currentFeatures = ores.getOrDefault(category,new HashSet<>());
            currentFeatures.addAll(feature);
            ores.put(category,currentFeatures);
        });

        Trees.features.forEach((category, feature) -> {
            Set< ConfiguredFeature<?, ?>> currentFeatures = vegetals.getOrDefault(category,new HashSet<>());
            currentFeatures.addAll(feature);
            vegetals.put(category,currentFeatures);
        });

        Trees.addWorldGenFeature("ash",Trees.ASH,1240,1,132,3,6, Biome.BiomeCategory.PLAINS);
        Trees.addWorldGenFeature("sambucus",Trees.SAMBUCUS,1240,1,132,3,6, Biome.BiomeCategory.PLAINS);
    }

    public static class Ores {
        private static final Map<Biome.BiomeCategory,Set<ConfiguredFeature<?, ?>>> features = new HashMap<>();

        public static void registerOre(Block ore,Block deepslateOre ,int minInclusive, int maxInclusive,int size, int veinsPerChunk, Biome.BiomeCategory biome)
        {
            String name = ore.getRegistryName().getPath();

            ImmutableList<OreConfiguration.TargetBlockState> TARGET_LIST = ImmutableList.of(
                OreConfiguration.target(OreConfiguration.Predicates.STONE_ORE_REPLACEABLES, ore.defaultBlockState()),
                OreConfiguration.target(OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES, deepslateOre.defaultBlockState())
            );

            ConfiguredFeature<?, ?> configuredFeature = registerWorldFeature(name,
                Feature.ORE.configured(new OreConfiguration(TARGET_LIST, size))
                    .rangeTriangle(VerticalAnchor.absolute(minInclusive), VerticalAnchor.absolute(maxInclusive))
                    .squared()
                    .count(veinsPerChunk)
            );


            Set<ConfiguredFeature<?,?>> currentFeatures = features.getOrDefault(biome,new HashSet<>());
            currentFeatures.add(configuredFeature);
            features.put(biome,currentFeatures);
        }
        public static void registerBasicOre(Block ore,int aboveBottom, int belowTop,int size, int discardChanceOnAirExposure,int veinsPerChunk ,Biome.BiomeCategory biome)
        {
            String name = ore.getRegistryName().getPath();

            ConfiguredFeature<?, ?> configuredFeature = registerWorldFeature(name,
                Feature.SCATTERED_ORE.configured(new OreConfiguration(
                    OreConfiguration.Predicates.STONE_ORE_REPLACEABLES,
                    ore.defaultBlockState(),
                    size,
                    discardChanceOnAirExposure))
                .count(veinsPerChunk)
                .range(new RangeDecoratorConfiguration(UniformHeight.of(VerticalAnchor.aboveBottom(aboveBottom), VerticalAnchor.belowTop(belowTop))))
                .squared()
            );

            Set<ConfiguredFeature<?,?>> currentFeatures = features.getOrDefault(biome,new HashSet<>());
            currentFeatures.add(configuredFeature);
            features.put(biome,currentFeatures);
        }
        public static void registerDeepSlateOre(Block ore,int aboveBottom, int belowTop,int size, int discardChanceOnAirExposure,int veinsPerChunk ,Biome.BiomeCategory biome)
        {
            String name = ore.getRegistryName().getPath();

            ConfiguredFeature<?, ?> configuredFeature = registerWorldFeature(name,
                Feature.SCATTERED_ORE.configured(new OreConfiguration(
                    OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES,
                    ore.defaultBlockState(),
                    size,
                    discardChanceOnAirExposure))
                    .count(veinsPerChunk)
                    .range(new RangeDecoratorConfiguration(UniformHeight.of(VerticalAnchor.aboveBottom(aboveBottom), VerticalAnchor.belowTop(belowTop))))
                    .squared()
            );

            Set<ConfiguredFeature<?,?>> currentFeatures = features.getOrDefault(biome,new HashSet<>());
            currentFeatures.add(configuredFeature);
            features.put(biome,currentFeatures);
        }
    }

    public static class Trees{
        private static final Map<Biome.BiomeCategory,Set<ConfiguredFeature<?, ?>>> features = new HashMap<>();

        public static final ConfiguredFeature<TreeConfiguration, ?> ASH = registerWorldFeature("ash", Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider(ModRegistry.ASH.getLog().getPrimary().defaultBlockState()), new FancyTrunkPlacer(3, 11, 0), new SimpleStateProvider(ModRegistry.ASH.getLeaves().getPrimary().defaultBlockState()), new SimpleStateProvider(ModRegistry.ASH.getSapling().getPrimary().defaultBlockState()), new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines().build()));
        public static final ConfiguredFeature<TreeConfiguration, ?> SAMBUCUS = registerWorldFeature("sambucus", Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider(ModRegistry.SAMBUCUS.getLog().getPrimary().defaultBlockState()), new FancyTrunkPlacer(4, 2, 0), new SimpleStateProvider(ModRegistry.SAMBUCUS.getLeaves().getPrimary().defaultBlockState()), new SimpleStateProvider(ModRegistry.SAMBUCUS.getSapling().getPrimary().defaultBlockState()), new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 6), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines().build()));

        public static void addWorldGenFeature(String name, ConfiguredFeature<TreeConfiguration, ?> feature, int chance, int countRandom, int noiseLevel, int belowNoise, int aboveNoise, Biome.BiomeCategory biome) {
            ConfiguredFeature<?,?> configuredFeature = registerWorldFeature(name+"_wgen",feature
                .countRandom(countRandom)
                .squared()
                .decorated(FeatureDecorator.HEIGHTMAP.configured(new HeightmapConfiguration(Heightmap.Types.MOTION_BLOCKING)))
                .rarity(chance)
                .decorated(FeatureDecorator.COUNT_NOISE.configured(new NoiseDependantDecoratorConfiguration(noiseLevel,belowNoise,aboveNoise)))
            );

            Set<ConfiguredFeature<?,?>> currentFeatures = features.getOrDefault(biome,new HashSet<>());
            currentFeatures.add(configuredFeature);
            features.put(biome,currentFeatures);
        }
    }

    public static <FC extends FeatureConfiguration, F extends Feature<FC>> ConfiguredFeature<FC, F> registerWorldFeature(String rl, ConfiguredFeature<FC, F> feature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(Main.MOD_ID,rl), feature);
    }
}