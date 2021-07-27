
package com.viruss.waw.common.worldgen;

import com.google.common.collect.Lists;
import com.viruss.waw.WitchingAndWizardry;
import net.minecraft.block.Block;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.NoiseDependant;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.*;
import java.util.function.Supplier;

public class Features {

    public static void setup() {
        Map<Biome.Category,Set< ConfiguredFeature<?, ?>>> vegetals = new HashMap<>();
        Map<Biome.Category,Set< ConfiguredFeature<?, ?>>> ores = new HashMap<>();

        init(ores,vegetals);

        for (Map.Entry<RegistryKey<Biome>, Biome> biomeEntry : WorldGenRegistries.BIOME.entrySet()) {
            Biome biome = biomeEntry.getValue();

            if (biome.getBiomeCategory().equals(Biome.Category.NETHER) || biome.getBiomeCategory().equals(Biome.Category.THEEND))
                return;

            //vegetals
            vegetals.forEach((category, configuredFeatures) -> {
                if(biome.getBiomeCategory().equals(category) || category.equals(Biome.Category.NONE))
                    addFeatures(biome,configuredFeatures, GenerationStage.Decoration.VEGETAL_DECORATION);
            });

            //ores
            ores.forEach((category, configuredFeatures) -> {
                if(biome.getBiomeCategory().equals(category) || category.equals(Biome.Category.NONE))
                    addFeatures(biome,configuredFeatures, GenerationStage.Decoration.UNDERGROUND_ORES);
            });
        }
    }

    private static void init(Map<Biome.Category,Set< ConfiguredFeature<?, ?>>> vegetals,Map<Biome.Category,Set< ConfiguredFeature<?, ?>>> ores) {

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
    }

    public static class Ores {
        private static final Map<Biome.Category,Set<ConfiguredFeature<?, ?>>> features = new HashMap<>();

        public static void registerOreFeature(Block ore, int maxSize, int maxHeight, int veinsPerChunk,Biome.Category biome)
        {
            ConfiguredFeature<?,?> configuredFeature = registerWorldFeature(ore.getRegistryName().getPath()+"_oregen",
                Feature.ORE.configured(
                    new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ore.defaultBlockState(), maxSize)
                )
                .range(maxHeight)
                .squared()
                .chance(veinsPerChunk)
            );

            Set<ConfiguredFeature<?,?>> currentFeatures = features.getOrDefault(biome,new HashSet<>());
            currentFeatures.add(configuredFeature);
            features.put(biome,currentFeatures);
        }
    }

    public static class Trees{
        private static final Map<Biome.Category,Set<ConfiguredFeature<?, ?>>> features = new HashMap<>();

        public static void addWorldGenFeature(String name,ConfiguredFeature<BaseTreeFeatureConfig, ?> feature,int chance, int countRandom,int noiseLevel,int belowNoise, int aboveNoise,Biome.Category biome)
        {
            ConfiguredFeature<?,?> configuredFeature = registerWorldFeature(name+"_wgen",feature
                    .chance(chance)
                    .countRandom(countRandom)
                    .squared()
                    .decorated(Placement.COUNT_NOISE.configured(new NoiseDependant(noiseLevel,belowNoise,aboveNoise)))
            );

            Set<ConfiguredFeature<?,?>> currentFeatures = features.getOrDefault(biome,new HashSet<>());
            currentFeatures.add(configuredFeature);
            features.put(biome,currentFeatures);
        }
    }


    private static void addFeatures(Biome biome, Set<ConfiguredFeature<?, ?>> configuredFeatures, GenerationStage.Decoration decoration)
    {
        List<List<Supplier<ConfiguredFeature<?, ?>>>> biomeFeatures = new ArrayList<>(biome.getGenerationSettings().features());

        while (biomeFeatures.size() <=  decoration.ordinal())
            biomeFeatures.add(Lists.newArrayList());

        List<Supplier<ConfiguredFeature<?, ?>>> features = new ArrayList<>(biomeFeatures.get(decoration.ordinal()));

        for(ConfiguredFeature<?,?> feature : configuredFeatures)
            features.add(() -> feature);

        biomeFeatures.set(decoration.ordinal(), features);
        ObfuscationReflectionHelper.setPrivateValue(BiomeGenerationSettings.class, biome.getGenerationSettings(), biomeFeatures,
                "field_242484_f");
    }

    public static <FC extends IFeatureConfig, F extends Feature<FC>> ConfiguredFeature<FC, F> registerWorldFeature(String rl, ConfiguredFeature<FC, F> feature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(WitchingAndWizardry.MOD_ID,rl), feature);
    }
}