package com.viruss.waw.common.objects.packs;

import com.viruss.waw.WitchingAndWizardry;
import com.viruss.waw.common.objects.items.CustomBoatItem;
import com.viruss.waw.common.worldgen.Features;
import com.viruss.waw.utils.EventHandler;
import com.viruss.waw.utils.ModRegistry;
import com.viruss.waw.utils.registration.DoubleRegisteredObject;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.trees.Tree;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.AbstractFeatureSizeType;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.trunkplacer.AbstractTrunkPlacer;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Random;

public class WoodenObject {

    private final ITag.INamedTag<Item> logsItemTag;
    private final ITag.INamedTag<Block> logsBlockTag;
    private final WoodType woodType;
    private final DoubleRegisteredObject<Block, Item> log;
    private DoubleRegisteredObject<Block, Item> stripped_log;
    private DoubleRegisteredObject<Block, Item> stripped_wood;
    private final DoubleRegisteredObject<Block, Item> wood;
    private DoubleRegisteredObject<Block, Item> leaves;
    private DoubleRegisteredObject<Block, Item> sapling;
    private RegistryObject<Block> potted_sapling;
    private final DoubleRegisteredObject<Block, Item> planks;
    private final DoubleRegisteredObject<Block, Item> stairs;
    private final DoubleRegisteredObject<Block, Item> slab;
    private final DoubleRegisteredObject<Block, Item> fence;
    private final DoubleRegisteredObject<Block, Item> gate;
    private final DoubleRegisteredObject<Block, Item> door;
    private final DoubleRegisteredObject<Block, Item> trapdoor;
    private final DoubleRegisteredObject<Block, Item> button;
    private final DoubleRegisteredObject<Block, Item> pressure_plate;
    private final SignObject sign;
    private final RegistryObject<Item> boat;

    public WoodenObject(String name,ItemGroup group,boolean isStrippable) {
        com.viruss.waw.utils.registration.Block.Builder builder = new com.viruss.waw.utils.registration.Block.Builder().needItem(group);
        this.woodType = WoodType.register(WoodType.create(WitchingAndWizardry.MOD_ID+":"+name));
        if (isStrippable) {
            this.stripped_log = ModRegistry.MDR.register(name+"_stripped_log",builder.setBlockSup(AbstractLog::new));
            this.log = ModRegistry.MDR.register(name+"_log",builder.setBlockSup(()-> new AbstractStrippableLog(stripped_log.getPrimary())));
            this.stripped_wood = ModRegistry.MDR.register(name+"_stripped_wood",builder.setBlockSup(AbstractLog::new));
            this.wood = ModRegistry.MDR.register(name+"_wood",builder.setBlockSup(()-> new AbstractStrippableLog(stripped_wood.getPrimary())));
        }
        else{
            this.wood = ModRegistry.MDR.register(name+"_wood",builder.setBlockSup(AbstractLog::new));
            this.log = ModRegistry.MDR.register(name+"_log",builder.setBlockSup(AbstractLog::new));
        }
        this.planks = ModRegistry.MDR.register(name+"_planks",builder.setBlockSup(AbstractPlanks::new));
        this.stairs = ModRegistry.MDR.register(name+"_stairs",builder.setBlockSup(() -> new AbstractStairs(planks.getPrimary())));
        this.slab = ModRegistry.MDR.register(name+"_slab",builder.setBlockSup(AbstractSlab::new));
        this.fence = ModRegistry.MDR.register(name+"_fence",builder.setBlockSup(() -> new AbstractFence(planks.getPrimary().defaultMaterialColor())));
        this.gate = ModRegistry.MDR.register(name+"_gate",builder.setBlockSup(() -> new AbstractFenceGate(planks.getPrimary().defaultMaterialColor())));
        this.door = ModRegistry.MDR.register(name+"_door",builder.setBlockSup(() -> new DoorBlock(AbstractBlock.Properties.of(Material.WOOD, planks.getPrimary().defaultMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion())));
        this.trapdoor = ModRegistry.MDR.register(name+"_trapdoor",builder.setBlockSup(()-> new TrapDoorBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(3.0F).sound(SoundType.WOOD).noOcclusion().isValidSpawn(WoodenObject::never))));
        this.button = ModRegistry.MDR.register(name+"_button",builder.setBlockSup(()-> new WoodButtonBlock(AbstractBlock.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.WOOD))));
        this.pressure_plate = ModRegistry.MDR.register(name+"_pressure_plate",builder.setBlockSup(()-> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, AbstractBlock.Properties.of(Material.WOOD, planks.getPrimary().defaultMaterialColor()).noCollission().strength(0.5F).sound(SoundType.WOOD))));
        this.sign = new SignObject(name,this.woodType,group);
        this.boat = ModRegistry.MDR.register(name + "_boat", CustomBoatItem::new,ForgeRegistries.ITEMS);


        this.logsItemTag = ItemTags.bind(WitchingAndWizardry.MOD_ID + ":" + name + "_logs");
        this.logsBlockTag = BlockTags.bind(WitchingAndWizardry.MOD_ID + ":" + name + "_logs");

        WitchingAndWizardry.CLIENT_RENDERER.addBlockRenderer(door, RenderType.translucent());
        WitchingAndWizardry.CLIENT_RENDERER.addBlockRenderer(trapdoor, RenderType.translucent());
    }

    public WoodenObject(String name, ItemGroup group, FoliagePlacer foliagePlacer, AbstractTrunkPlacer trunkPlacer, AbstractFeatureSizeType sizeType, boolean vines, Heightmap.Type heightmap, boolean isStrippable) {
        this(name,group, isStrippable);
        com.viruss.waw.utils.registration.Block.Builder builder = new com.viruss.waw.utils.registration.Block.Builder().needItem(group);
        this.leaves = ModRegistry.MDR.register(name + "_leaves", builder.setBlockSup(AbstractLeaves::new));



        this.sapling = ModRegistry.MDR.register(name + "_sapling", builder.setBlockSup(() -> new AbstractSapling(new AbstractTree(name,log.getPrimary(),leaves.getPrimary(),foliagePlacer,trunkPlacer,sizeType,vines,heightmap))));
        this.potted_sapling = ModRegistry.MDR.register(name + "_potted", () -> new FlowerPotBlock(()-> (FlowerPotBlock) Blocks.FLOWER_POT, this.sapling::getPrimary, AbstractBlock.Properties.copy(Blocks.FLOWER_POT)), ForgeRegistries.BLOCKS);

        WitchingAndWizardry.CLIENT_RENDERER.addBlockRenderer(sapling, RenderType.cutout());
        WitchingAndWizardry.CLIENT_RENDERER.addBlockRenderer(potted_sapling, RenderType.cutout());
        EventHandler.addWood(this);

    }

    public ITag.INamedTag<Item> getLogsItemTag() {
        return logsItemTag;
    }

    public ITag.INamedTag<Block> getLogsBlockTag() {
        return logsBlockTag;
    }

    public WoodType getWoodType() {
        return woodType;
    }

    public DoubleRegisteredObject<Block, Item> getLog() {
        return log;
    }

    public DoubleRegisteredObject<Block, Item> getLeaves() {
        return leaves;
    }

    public DoubleRegisteredObject<Block, Item> getSapling() {
        return sapling;
    }

    public Block getPotted_sapling() {
        return potted_sapling.get();
    }

    public DoubleRegisteredObject<Block, Item> getPlanks() {
        return planks;
    }

    public DoubleRegisteredObject<Block, Item> getStairs() {
        return stairs;
    }

    public DoubleRegisteredObject<Block, Item> getSlab() {
        return slab;
    }

    public DoubleRegisteredObject<Block, Item> getFence() {
        return fence;
    }

    public DoubleRegisteredObject<Block, Item> getGate() {
        return gate;
    }

    public DoubleRegisteredObject<Block, Item> getDoor() {
        return door;
    }

    public DoubleRegisteredObject<Block, Item> getTrapdoor() {
        return trapdoor;
    }

    public DoubleRegisteredObject<Block, Item> getButton() {
        return button;
    }

    public DoubleRegisteredObject<Block, Item> getPressure_plate() {
        return pressure_plate;
    }

    public SignObject getSign() {
        return sign;
    }

    public DoubleRegisteredObject<Block, Item> getStrippedLog() {
        return stripped_log;
    }

    public DoubleRegisteredObject<Block, Item> getWood() {
        return wood;
    }

    public RegistryObject<Item> getBoat() {
        return boat;
    }

    public DoubleRegisteredObject<Block, Item> getStrippedWood() {
        return stripped_wood;
    }

    /*~  Spawn predicates  ~*/

    public static Boolean never(BlockState state, IBlockReader worldIn, BlockPos pos, EntityType<?> entityType) {
        return false;
    }

    public static Boolean parrotOrOcelot(BlockState state, IBlockReader worldIn, BlockPos pos, EntityType<?> entityType) {
        return entityType == EntityType.PARROT || entityType == EntityType.OCELOT;
    }

            /*~  ~Additional Classes~  ~*/

    public static class AbstractFence extends FenceBlock {
        public AbstractFence(MaterialColor color) {
            super(AbstractBlock.Properties.of(Material.WOOD, color).strength(2.0F, 3.0F).sound(SoundType.WOOD));
        }

        @Override
        public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
            return 20;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
            return 5;
        }

        @Override
        public boolean connectsTo(BlockState blockState, boolean b, Direction direction) {
            Block block = blockState.getBlock();
            return !isExceptionForConnection(block) && b || block instanceof FenceBlock || block instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(blockState, direction);
        }
    }

    public static class AbstractFenceGate extends FenceGateBlock {
        public AbstractFenceGate(MaterialColor color) {
            super(AbstractBlock.Properties.of(Material.WOOD, color).strength(2.0F, 3.0F).sound(SoundType.WOOD));
        }

        @Override
        public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
            return 20;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
            return 5;
        }
    }

    public static class AbstractLeaves extends LeavesBlock {
        public AbstractLeaves() {
            super(AbstractBlock.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(WoodenObject::parrotOrOcelot));
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
            return 60;
        }

        @Override
        public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
            return 30;
        }
    }

    public static class AbstractLog extends RotatedPillarBlock {

        public AbstractLog() {
            super(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).strength(2.0F).sound(SoundType.WOOD));
        }

        public AbstractLog(Properties p_i48339_1_) {
            super(p_i48339_1_);
        }

        @Override
        public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
            return 5;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
            return 5;
        }
    }

    public static class AbstractPlanks extends Block {

        public AbstractPlanks() {
            super(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_ORANGE).strength(2.0F, 3.0F).sound(SoundType.WOOD));
        }

        @Override
        public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
            return 20;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
            return 5;
        }
    }

    public static class AbstractSapling extends SaplingBlock {

        public AbstractSapling(Tree p_i48337_1_) {
            super(p_i48337_1_, AbstractBlock.Properties.of(Material.PLANT).instabreak().sound(SoundType.CROP).randomTicks().noCollission());
        }
    }

    public static class AbstractSlab extends SlabBlock {

        public AbstractSlab() {
            super(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD));
        }

        @Override
        public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
            return 20;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
            return 5;
        }
    }

    public static class AbstractStairs extends StairsBlock {

        public AbstractStairs(Block planks) {
            super(planks::defaultBlockState, AbstractBlock.Properties.copy(planks));
        }

        @Override
        public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
            return 20;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
            return 5;
        }
    }

    public static class AbstractTree extends Tree {
        private final ConfiguredFeature<BaseTreeFeatureConfig, ?> treeFeature;

        public AbstractTree(String name, Block log, Block leaves, FoliagePlacer foliagePlacer, AbstractTrunkPlacer trunkPlacer, AbstractFeatureSizeType sizeType, boolean vines, Heightmap.Type heightmap) {

            BaseTreeFeatureConfig.Builder builder = new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(log.defaultBlockState()), new SimpleBlockStateProvider(leaves.defaultBlockState()), foliagePlacer, trunkPlacer, sizeType);
            if (!vines)
                builder.ignoreVines();
            if(heightmap != null)
                builder.heightmap(heightmap);

            this.treeFeature =  Features.registerWorldFeature(name+"_tree",Feature.TREE.configured(builder.build()));
        }


        @Nullable
        @Override
        protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getConfiguredFeature(Random p_225546_1_, boolean p_225546_2_) {
            return treeFeature;
        }
    }

    public static class AbstractStrippableLog extends AbstractLog {
        Block stripped;

        public AbstractStrippableLog(Block stripped) {
            super(Properties.copy(stripped));
            this.stripped = stripped;
        }

        @Nullable
        @Override
        public BlockState getToolModifiedState(BlockState state, World world, BlockPos pos, PlayerEntity player, ItemStack stack, ToolType toolType) {
            return stripped.defaultBlockState().setValue(AXIS, state.getValue(AXIS));
        }
    }
}
