package com.viruss.waw.common.objects.packs;

import com.viruss.waw.Main;
import com.viruss.waw.common.objects.items.CustomBoatItem;
import com.viruss.waw.common.worldgen.Features;
import com.viruss.waw.utils.EventHandler;
import com.viruss.waw.utils.registration.DoubleRegisteredObject;
import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Random;

@SuppressWarnings("all")
public class WoodenPack {

    private final Tag.Named<Item> logsItemTag;
    private final Tag.Named<Block> logsBlockTag;

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
    private final SignPack sign;
    private final RegistryObject<Item> boat;

    private RegistryObject<Item> fruit;


    public WoodenPack(String name, CreativeModeTab group, boolean isStrippable) {
        com.viruss.waw.utils.registration.Block.Builder builder = new com.viruss.waw.utils.registration.Block.Builder().needItem(group);
        this.woodType = WoodType.register(WoodType.create(/*Main.MOD_ID+":"+*/name));    //TODO: move wood ( ResourceLocationException not fixed :c )

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
        this.door = ModRegistry.MDR.register(name+"_door",builder.setBlockSup(() -> new DoorBlock(BlockBehaviour.Properties.of(Material.WOOD, planks.getPrimary().defaultMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion())));
        this.trapdoor = ModRegistry.MDR.register(name+"_trapdoor",builder.setBlockSup(()-> new TrapDoorBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(3.0F).sound(SoundType.WOOD).noOcclusion().isValidSpawn(WoodenPack::never))));
        this.button = ModRegistry.MDR.register(name+"_button",builder.setBlockSup(()-> new WoodButtonBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.WOOD))));
        this.pressure_plate = ModRegistry.MDR.register(name+"_pressure_plate",builder.setBlockSup(()-> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.of(Material.WOOD, planks.getPrimary().defaultMaterialColor()).noCollission().strength(0.5F).sound(SoundType.WOOD))));
        this.sign = new SignPack(name,this.woodType,group);
        this.boat = ModRegistry.MDR.register(name + "_boat", CustomBoatItem::new, ForgeRegistries.ITEMS);

        this.logsItemTag = ItemTags.bind(Main.MOD_ID + ":" + name + "_logs");
        this.logsBlockTag = BlockTags.bind(Main.MOD_ID + ":" + name + "_logs");

        EventHandler.addWood(this);
        Main.CLIENT_RENDERER.addBlockRenderer(door, RenderType.translucent());
        Main.CLIENT_RENDERER.addBlockRenderer(trapdoor, RenderType.translucent());
    }

    public WoodenPack(String name, CreativeModeTab group, boolean isStrippable, FoodProperties fruit) {
        this(name,group, isStrippable);
        com.viruss.waw.utils.registration.Block.Builder builder = new com.viruss.waw.utils.registration.Block.Builder().needItem(group);
        this.leaves = ModRegistry.MDR.register(name + "_leaves", builder.setBlockSup(AbstractLeaves::new));
        this.sapling = ModRegistry.MDR.register(name + "_sapling", builder.setBlockSup(() -> new SaplingBlock(new AbstractTree(name), BlockBehaviour.Properties.of(Material.PLANT).instabreak().sound(SoundType.CROP).randomTicks().noCollission())));
        this.potted_sapling = ModRegistry.MDR.register(name + "_potted", () -> new FlowerPotBlock(()-> (FlowerPotBlock) Blocks.FLOWER_POT, this.sapling::getPrimary, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)), ForgeRegistries.BLOCKS);

        if(fruit != null){
            String type;

            if(fruit == Foods.APPLE)
                type = "_apple";
            else
                type = "_berries";

            this.fruit =ModRegistry.MDR.register(name+type,()->new Item(new Item.Properties().tab(group).food(fruit)),ForgeRegistries.ITEMS);
        }

        Main.CLIENT_RENDERER.addBlockRenderer(sapling, RenderType.cutout());
        Main.CLIENT_RENDERER.addBlockRenderer(potted_sapling, RenderType.cutout());
        EventHandler.addWood(this);
    }


             /*~  Spawn predicates  ~*/

    public static Boolean never(BlockState state, BlockGetter worldIn, BlockPos pos, EntityType<?> entityType) {
        return false;
    }

    public static Boolean parrotOrOcelot(BlockState state, BlockGetter worldIn, BlockPos pos, EntityType<?> entityType) {
        return entityType == EntityType.PARROT || entityType == EntityType.OCELOT;
    }

            /*~  ~Additional Classes~  ~*/

    public static class AbstractFence extends FenceBlock {
        public AbstractFence(MaterialColor color) {
            super(Properties.of(Material.WOOD, color).strength(2.0F, 3.0F).sound(SoundType.WOOD));
        }

        @Override
        public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
            return 20;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
            return 5;
        }

    }
    public static class AbstractFenceGate extends FenceGateBlock {
        public AbstractFenceGate(MaterialColor color) {
            super(Properties.of(Material.WOOD, color).strength(2.0F, 3.0F).sound(SoundType.WOOD));
        }

        @Override
        public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
            return 20;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
            return 5;
        }
    }
    public static class AbstractLeaves extends LeavesBlock {
        public AbstractLeaves() {
            super(Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(WoodenPack::parrotOrOcelot));
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
            return 60;
        }

        @Override
        public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
            return 30;
        }
    }
    public static class AbstractLog extends RotatedPillarBlock {

        public AbstractLog() {
            super(Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).strength(2.0F).sound(SoundType.WOOD));
        }

        public AbstractLog(Properties p_i48339_1_) {
            super(p_i48339_1_);
        }

        @Override
        public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
            return 5;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
            return 5;
        }
    }
    public static class AbstractPlanks extends Block {

        public AbstractPlanks() {
            super(Properties.of(Material.WOOD, MaterialColor.COLOR_ORANGE).strength(2.0F, 3.0F).sound(SoundType.WOOD));
        }

        @Override
        public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
            return 20;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
            return 5;
        }
    }
    public static class AbstractSlab extends SlabBlock {

        public AbstractSlab() {
            super(Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD));
        }

        @Override
        public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
            return 20;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
            return 5;
        }
    }
    public static class AbstractStairs extends StairBlock {

        public AbstractStairs(Block planks) {
            super(planks::defaultBlockState, Properties.copy(planks));
        }

        @Override
        public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
            return 20;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
            return 5;
        }
    }
    public static class AbstractTree extends AbstractTreeGrower {
        Field t;
        public AbstractTree(String name) {
            try {
                t = Features.Trees.class.getField(name.toUpperCase());
            }catch (Exception ignored){}
        }

        @Nullable
        @Override
        protected ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(Random p_225546_1_, boolean p_225546_2_) {
            ConfiguredFeature<TreeConfiguration, ?> feature=null;
            try {
                feature = (ConfiguredFeature<TreeConfiguration, ?>) t.get(feature);
            } catch (Exception ignored){}
            System.out.println(feature.toString());
            return feature;
        }
    }
    public static class AbstractStrippableLog extends AbstractLog {
        Block stripped;

        public AbstractStrippableLog(Block stripped) {
            super(BlockBehaviour.Properties.copy(stripped));
            this.stripped = stripped;
        }

        @Nullable
        @Override
        public BlockState getToolModifiedState(BlockState state, Level world, BlockPos pos, Player player, ItemStack stack, ToolAction toolAction) {
            return stripped.defaultBlockState().setValue(AXIS, state.getValue(AXIS));
        }
    }



           /*~  ~Getters~  ~*/

    public DoubleRegisteredObject<Block, Item> getButton() {
        return button;
    }
    public DoubleRegisteredObject<Block, Item> getPressurePlate() {
        return pressure_plate;
    }
    public DoubleRegisteredObject<Block, Item> getStrippedWood() {
        return stripped_wood;
    }
    public DoubleRegisteredObject<Block, Item> getWood() {
        return wood;
    }
    public DoubleRegisteredObject<Block, Item> getLog() {
        return log;
    }
    public DoubleRegisteredObject<Block, Item> getStrippedLog() {
        return stripped_log;
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
    public DoubleRegisteredObject<Block, Item> getTrapdoor() {
        return trapdoor;
    }
    public DoubleRegisteredObject<Block, Item> getDoor() {
        return door;
    }
    public WoodType getWoodType() {
        return woodType;
    }
    public RegistryObject<Item> getBoat() {
        return boat;
    }
    public SignPack getSign() {
        return sign;
    }
    public DoubleRegisteredObject<Block, Item> getLeaves() {
        return leaves;
    }
    public DoubleRegisteredObject<Block, Item> getSapling() {
        return sapling;
    }
    public RegistryObject<Block> getPottedSapling() {
        return potted_sapling;
    }
    public RegistryObject<Item> getFruit() {
        return fruit;
    }

    public Tag.Named<Item> getLogsItemTag() {
        return logsItemTag;
    }
    public Tag.Named<Block> getLogsBlockTag() {
        return logsBlockTag;
    }
}
