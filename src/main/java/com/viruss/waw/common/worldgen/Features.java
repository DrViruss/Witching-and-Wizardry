package com.viruss.waw.common.worldgen;

import com.viruss.waw.Main;
import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.biome.EndBiomes;
import net.minecraft.data.worldgen.biome.NetherBiomes;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.targets.FMLServerDevLaunchHandler;
import net.minecraftforge.fml.loading.targets.FMLServerLaunchHandler;
import net.minecraftforge.fml.loading.targets.ForgeServerLaunchHandler;
import net.minecraftforge.server.ServerLifecycleHooks;
import oshi.util.tuples.Pair;

import java.util.*;

import static net.minecraft.data.worldgen.features.OreFeatures.DEEPSLATE_ORE_REPLACEABLES;
import static net.minecraft.data.worldgen.features.OreFeatures.STONE_ORE_REPLACEABLES;

//@SuppressWarnings("all")
public class Features {


    @SubscribeEvent
    public static void onBiomeLoad(BiomeLoadingEvent event) {
        if(event.getCategory()!= Biome.BiomeCategory.THEEND && event.getCategory()!= Biome.BiomeCategory.NETHER){
            Ores.features.forEach((placedFeatures) -> event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,placedFeatures));
        }

        Set<PlacedFeature> features = Trees.features.getOrDefault(event.getCategory(),new HashSet<>());
        if (features.isEmpty()) return;
        features.forEach(placedFeature -> event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,placedFeature));


    }

    public static class Ores {
        private static final Set<PlacedFeature> features = new HashSet<>();

        static {
        }

        private static void addOres(int size, HeightRangePlacement large_placement, HeightRangePlacement small_placement,int count ,Block stone, Block deepslate) {
            List<OreConfiguration.TargetBlockState> list = Arrays.asList(
                OreConfiguration.target(STONE_ORE_REPLACEABLES,stone.defaultBlockState()),
                OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES,deepslate.defaultBlockState())
            );
            addWorldGenFeature(stone.getRegistryName().getPath()+"_large",size*2, list,count,large_placement);
            addWorldGenFeature(stone.getRegistryName().getPath()+"_small",size,list,count,small_placement);
        }

        private static void addOres(int size, HeightRangePlacement large_placement, HeightRangePlacement small_placement, HeightRangePlacement buried_placement,int count ,Block stone, Block deepslate) {
            addOres(size,large_placement,small_placement,count,stone,deepslate);
            addWorldGenFeature(deepslate.getRegistryName().getPath()+"_buried",size,Arrays.asList(OreConfiguration.target(STONE_ORE_REPLACEABLES,stone.defaultBlockState()), OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES,deepslate.defaultBlockState())),count,buried_placement);
        }

        private static void addWorldGenFeature(String name,int size,List<OreConfiguration.TargetBlockState> list,int count,HeightRangePlacement placement) {
            features.add(registerGenFeature(name, Feature.ORE.configured(new OreConfiguration(list, size)).placed(commonOrePlacement(count, placement))));
        }

        private static void addWorldGenFeature(String name, int size, BlockState state, RuleTest replace_rule, int count, HeightRangePlacement placement) {
            features.add(registerGenFeature(name, Feature.ORE.configured(new OreConfiguration(replace_rule,state, size)).placed(commonOrePlacement(count, placement))));
        }



        private static List<PlacementModifier> orePlacement(PlacementModifier rarityModifier, PlacementModifier placementModifier) {
            return List.of(rarityModifier, InSquarePlacement.spread(), placementModifier, BiomeFilter.biome());
        }

        private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier placementModifier) {
            return orePlacement(CountPlacement.of(count), placementModifier);
        }

        private static List<PlacementModifier> rareOrePlacement(int rarity, PlacementModifier placementModifier) {
            return orePlacement(RarityFilter.onAverageOnceEvery(rarity), placementModifier);
        }
    }

    public static class Trees{
        private static final Map<Biome.BiomeCategory,Set<PlacedFeature>> features = new HashMap<>();

        public static final ConfiguredFeature<TreeConfiguration, ?> ASH = registerGenTree("ash",Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(ModRegistry.WOOD.ASH.getLog().getPrimary().defaultBlockState()), new FancyTrunkPlacer(3, 11, 0), BlockStateProvider.simple(ModRegistry.WOOD.ASH.getLeaves().getPrimary().defaultBlockState()),new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines().build()), ModRegistry.WOOD.ASH.getSapling().getPrimary(),12,1, Biome.BiomeCategory.PLAINS);
        public static final ConfiguredFeature<TreeConfiguration, ?> SAMBUCUS = registerGenTree("sambucus", Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(ModRegistry.WOOD.SAMBUCUS.getLog().getPrimary().defaultBlockState()), new FancyTrunkPlacer(4, 2, 0), BlockStateProvider.simple(ModRegistry.WOOD.SAMBUCUS.getLeaves().getPrimary().defaultBlockState()), new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 6), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines().build()),ModRegistry.WOOD.ASH.getSapling().getPrimary(), 12,1, Biome.BiomeCategory.PLAINS);

        private static void addWorldGenFeature(String name, ConfiguredFeature<TreeConfiguration, ?> feature, BlockState sapling , int chance, int countRandom, Biome.BiomeCategory biome) {
            PlacedFeature placedFeature = registerGenFeature(name+"_wgen",feature.placed(
                RarityFilter.onAverageOnceEvery(chance),
                PlacementUtils.countExtra(0,0.05F,1),
                CountPlacement.of(countRandom),
                InSquarePlacement.spread(),
                BiomeFilter.biome(),
                PlacementUtils.HEIGHTMAP,
                VegetationPlacements.TREE_THRESHOLD,
                BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(sapling, BlockPos.ZERO))
            ));

            Set<PlacedFeature> currentFeatures = features.getOrDefault(biome,new HashSet<>());
            currentFeatures.add(placedFeature);
            features.put(biome,currentFeatures);
        }

        private static ConfiguredFeature<TreeConfiguration, ?> registerGenTree(String rl, ConfiguredFeature<TreeConfiguration, ?> feature,Block sapling,int chance, int countRandom, Biome.BiomeCategory biome) {
            addWorldGenFeature(rl, feature,sapling.defaultBlockState(),chance,countRandom,biome);
            return registerFeature(rl,feature);
        }
    }

    public static PlacedFeature registerGenFeature(String rl, PlacedFeature feature) {
        return Registry.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(Main.MOD_ID,rl), feature);
    }

    public static <FC extends FeatureConfiguration, F extends Feature<FC>> ConfiguredFeature<FC, F> registerFeature(String rl, ConfiguredFeature<FC, F> feature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(Main.MOD_ID,rl), feature);
    }
}