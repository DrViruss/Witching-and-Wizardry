package com.viruss.waw.utils.datagen;

import com.viruss.waw.Main;
import com.viruss.waw.common.objects.blocks.chalk.BasicSymbol;
import com.viruss.waw.common.objects.blocks.chalk.CentralSymbol;
import com.viruss.waw.common.objects.packs.SignPack;
import com.viruss.waw.common.objects.packs.WoodenPack;
import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Objects;
import java.util.Set;

@SuppressWarnings("all")
public class BlockStateProvider extends net.minecraftforge.client.model.generators.BlockStateProvider {
    private final Set<WoodenPack> woods;
    public BlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper,Set<WoodenPack> woodenPacks) {
        super(gen, Main.MOD_ID, exFileHelper);
        this.woods = woodenPacks;
    }

    @Override
    protected void registerStatesAndModels() {
        woods(woods.toArray(WoodenPack[]::new));
        chalks();
        ingredients();
        complexBlocks();
    }

    private void complexBlocks() {
        customBlockAndItem(ModRegistry.GADGETS.getBrazier(),"gadgets/ancient_brazier");

        customBlockAndItem(ModRegistry.GADGETS.getMortar(), "gadgets/mortar");
        generatedItem(ModRegistry.GADGETS.getPestle(),modLoc("items/pestle"));
    }

    private void ingredients(){
        generatedItem(ModRegistry.INGREDIENTS.getCalcosvis(),modLoc("items/magic_dust"));
        generatedItem(ModRegistry.INGREDIENTS.getMagicAsh(),modLoc("items/magic_dust"));
    }

    private void chalks(){
        generatedItem(ModRegistry.CHALKS.getChalk(),modLoc("items/chalk"));
        symbolBlock(ModRegistry.CHALKS.getBasicBlock(), BasicSymbol.SIGN,"basic");
        symbolBlock(ModRegistry.CHALKS.getCentralBlock(), CentralSymbol.STAGE,"central");
    }

    private void woods(WoodenPack... woods){
        for(WoodenPack wood : woods)
        {
            String treeName = wood.getWoodType().name().split(":")[1];

            basicWoodBlockAndItem(wood.getPlanks().getPrimary());
            axisBlockAndItem(wood.getLog().getPrimary(), woodBlocksLocation(treeName,"log"));
            doorAndItem(wood.getDoor().getPrimary(), woodBlocksLocation(treeName,"door_bottom"), woodBlocksLocation(treeName,"door_top"));
            fenceGateBlockAndItem(wood.getGate().getPrimary(), woodBlocksLocation(treeName,"planks"));
            slabBlockAndItem(wood.getSlab().getPrimary(),wood.getPlanks().getPrimary(), woodBlocksLocation(treeName,"planks"));
            stairsBlockAndItem(wood.getStairs().getPrimary(), woodBlocksLocation(treeName,"planks"));
            trapdoorBlockAndItem(wood.getTrapdoor().getPrimary(), woodBlocksLocation(treeName,"trapdoor"),true);
            signBlock(wood.getSign(),treeName);
            buttonBlockAndItem(wood.getButton().getPrimary(),treeName);
            pressurePlateAndItem(wood.getPressurePlate().getPrimary(),treeName);
            woodBlockAndItem(wood.getWood().getPrimary(),"log_side",treeName);
            fenceBlockAndItem(wood.getFence().getPrimary(),wood.getPlanks().getPrimary() ,woodBlocksLocation(treeName,"planks"));
            generatedItem(wood.getBoat().get(),woodItemsLocation(treeName,"boat"));

            if(wood.getSapling()!= null) {
                saplingAndItem(wood.getSapling().getPrimary(),wood.getPottedSapling().get(),treeName);
                leavesAndItem(wood.getLeaves().getPrimary());
            }

            if(wood.getStrippedLog()!= null){
                axisBlockAndItem(wood.getStrippedLog().getPrimary(), woodBlocksLocation(treeName,"stripped"));
                woodBlockAndItem(wood.getStrippedWood().getPrimary(),"stripped_side",treeName);
            }

            if(wood.getFruit() != null)
                generatedItem(wood.getFruit().get(),woodItemsLocation(treeName,wood.getFruit().getId().getPath().split("_")[1]));
        }
    }

    private void customBlockAndItem(Block block, String path){
        ModelFile.ExistingModelFile model = this.models().getExistingFile(modLoc("block/"+path));
        this.simpleBlock(block, model);
        this.simpleBlockItem(block,model);
    }
    private void saplingAndItem(Block sapling, Block pottedBlock, String treeName) {
        simpleBlock(sapling, models().cross(name(sapling), woodBlocksLocation(treeName,"sapling")));
        simpleBlock(pottedBlock, models().singleTexture(name(pottedBlock),
                new ResourceLocation(ModelProvider.BLOCK_FOLDER + "/flower_pot_cross"), "plant", woodBlocksLocation(treeName,"sapling")));
//        basicBlockItem(sapling); I like it!
        generatedItem(sapling,woodBlocksLocation(treeName,"sapling"));
    }
    private void leavesAndItem(Block block) {
        simpleBlock(block,
                models().singleTexture(name(block), new ResourceLocation(ModelProvider.BLOCK_FOLDER + "/leaves"), "all",
                        blockTexture(Blocks.OAK_LEAVES)));
        basicBlockItem(block);
    }
    private void signBlock(SignPack sign, String treeName) {
        ModelFile model = models().getBuilder(name(sign.getSign())).texture("particle", woodBlocksLocation(treeName,"planks"));
        simpleBlock(sign.getSign(), model);
        simpleBlock(sign.getWallSign(), model);
        generatedItem(sign.getSign(),woodItemsLocation(sign.getSign()));
    }
    private void pressurePlateAndItem(Block block, String treeName) {
        ModelFile up = models()
                .singleTexture(name(block), new ResourceLocation(ModelProvider.BLOCK_FOLDER + "/pressure_plate_up"),
                        "texture", woodBlocksLocation(treeName,"planks"));
        ModelFile down = models().singleTexture(name(block) + "_down",
                new ResourceLocation(ModelProvider.BLOCK_FOLDER + "/pressure_plate_down"), "texture",
                woodBlocksLocation(treeName,"planks"));
        getVariantBuilder(block).partialState().with(PressurePlateBlock.POWERED, false)
                .addModels(new ConfiguredModel(up)).partialState().with(PressurePlateBlock.POWERED, true)
                .addModels(new ConfiguredModel(down));
        basicBlockItem(block);
    }
    private void buttonBlockAndItem(Block block, String treeName) {
        ModelFile normal = models()
                .singleTexture(name(block), new ResourceLocation(ModelProvider.BLOCK_FOLDER + "/button"), "texture",
                        woodBlocksLocation(treeName,"planks"));
        ModelFile pressed = models().singleTexture(name(block) + "_pressed",
                new ResourceLocation(ModelProvider.BLOCK_FOLDER + "/button_pressed"), "texture",
                woodBlocksLocation(treeName,"planks"));
        VariantBlockStateBuilder builder = getVariantBuilder(block);

        for (Direction d : Direction.Plane.HORIZONTAL) {
            for (AttachFace f : AttachFace.values()) {
                int rotX = switch (f) {
                    case CEILING -> 180;
                    case WALL -> 90;
                    default -> 0;
                };
                int rotY = switch (d) {
                    case EAST -> 90;
                    case WEST -> 270;
                    case NORTH -> 0;
                    default -> 180;
                };
                builder.partialState().with(ButtonBlock.FACE, f).with(ButtonBlock.FACING, d)
                        .with(ButtonBlock.POWERED, true).modelForState().rotationX(rotX).rotationY(rotY)
                        .modelFile(pressed).addModel().partialState().with(ButtonBlock.FACE, f)
                        .with(ButtonBlock.FACING, d).with(ButtonBlock.POWERED, false)
                        .modelForState().rotationX(rotX).rotationY(rotY).modelFile(normal).addModel();
            }
        }

        simpleBlockItem(block, models().singleTexture(name(block) + "_inventory",
                new ResourceLocation(ModelProvider.BLOCK_FOLDER + "/button_inventory"), "texture",
                woodBlocksLocation(treeName,"planks")));
    }
    private void woodBlockAndItem(Block block, String textureName, String treeName) {
        ResourceLocation tex = woodBlocksLocation(treeName,textureName);
        axisBlock((RotatedPillarBlock)block, models().cubeColumn(name(block), tex, tex),
                models().cubeColumnHorizontal(name(block), tex, tex));
        basicBlockItem(block);
    }
    private void slabBlockAndItem(Block block, Block planks, ResourceLocation planksTexture) {
        this.slabBlock((SlabBlock) block,planks.getRegistryName(), planksTexture);
        this.basicBlockItem(block);
    }
    private void fenceBlockAndItem(Block block, Block planks,ResourceLocation planksTexture) {
        this.fenceBlock((FenceBlock) block, planksTexture);
        this.simpleBlockItem(block,models().fenceInventory(block.getRegistryName().getPath()+"_inventory",woodBlocksLocation(planks)));
    }
    private void trapdoorBlockAndItem(Block block, ResourceLocation texture, boolean orientable) {
        this.trapdoorBlock((TrapDoorBlock) block, texture,orientable);
        this.simpleBlockItem(block,models().getBuilder(block.getRegistryName().getPath()+"_bottom"));
    }
    private void stairsBlockAndItem(Block block, ResourceLocation planksTexture) {
        this.stairsBlock((StairBlock) block, planksTexture);
        this.basicBlockItem(block);
    }
    private void fenceGateBlockAndItem(Block block, ResourceLocation planksTexture) {
        this.fenceGateBlock((FenceGateBlock) block, planksTexture);
        this.simpleBlockItem(block,models().fenceGate(block.getRegistryName().getPath()+"_inventory",planksTexture));
    }
    private void doorAndItem(Block door, ResourceLocation door_bottom, ResourceLocation door_top) {
        this.doorBlock((DoorBlock)door, door_bottom, door_top);
        this.generatedItem(door,woodItemsLocation(door));
    }
    private void axisBlockAndItem(Block block, ResourceLocation texture) {
        this.axisBlock((RotatedPillarBlock)block, texture);
        this.basicBlockItem(block);
    }
    public void basicBlockItem(Block block){
        this.simpleBlockItem(block,models().getBuilder(block.getRegistryName().getPath()));
    }
    public void basicWoodBlockAndItem(Block block){
        this.simpleBlock(block,cubeAll(block,woodBlocksLocation(block)));
        this.basicBlockItem(block);
    }
    private void generatedItem(ItemLike block, ResourceLocation texture) {
        itemModels().getBuilder(Objects.requireNonNull(block.asItem().getRegistryName()).getPath())
                .parent(itemModels().getExistingFile(new ResourceLocation("item/generated")))
                .texture("layer0", texture);
    }

    public ModelFile cubeAll(Block block,ResourceLocation location) {
        return models().cubeAll(name(block), location);
    }

    protected void symbolBlock(Block block, Property<Integer> property,String name) {
        ModelFile.ExistingModelFile parent = this.models()
                .getExistingFile(modLoc("block/symbols/symbols"));
        this.getVariantBuilder(block)
                .forAllStates(state -> {
                    int value = state.getValue(property);
                    ModelFile subModel = this.models().getBuilder("block/symbols/"+name+"_"+property.getName()+"_" + value).parent(parent)
                            .texture("texture", modLoc("blocks/symbols/"+name+"_" + value));

                    return ConfiguredModel.builder()
                            .modelFile(subModel)
                            .rotationY((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING)
                                    .toYRot())
                            .build();
                });
    }

    private String name(Block block) {
        return block.getRegistryName().getPath();
    }
    private ResourceLocation woodItemsLocation(String treeName, String name){
        return modLoc("items/trees/"+treeName+"/"+name);
    }
    private ResourceLocation woodBlocksLocation(Block block){
        String[] name = block.getRegistryName().getPath().split("_");
        return modLoc("blocks/trees/"+name[0]+"/"+name[1]);
    }
    private ResourceLocation woodBlocksLocation(String treeName, String name){
        return modLoc("blocks/trees/"+treeName+"/"+name);
    }
    private ResourceLocation woodItemsLocation(Block block){
        String[] name = block.getRegistryName().getPath().split("_");
        return modLoc("items/trees/"+name[0]+"/"+name[1]);
    }
}