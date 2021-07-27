package com.viruss.waw.utils.datagen;

import com.viruss.waw.WitchingAndWizardry;
import com.viruss.waw.common.objects.packs.WoodenObject;
import com.viruss.waw.utils.ModRegistry;
import net.minecraft.block.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Objects;
import java.util.Set;

/*Thanks TreemendousMod (https://github.com/rehwinkel/treemendous)*/
public class BlockStateProvider extends net.minecraftforge.client.model.generators.BlockStateProvider{
    private final Set<WoodenObject> woodenObjects;


    public BlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper,Set<WoodenObject> woods) {
        super(gen, WitchingAndWizardry.MOD_ID, exFileHelper);
        this.woodenObjects = woods;
    }

    @Override
    protected void registerStatesAndModels() {
        for(WoodenObject wood : woodenObjects)
            registerWoods(wood);


        generateChalks();
    }

    private void generateChalks(){
        ModRegistry.CHALKS.foreach((type, chalkObject) -> {
            generatedItem(chalkObject.getChalk(),modLoc("items/chalk"));
        });
    }

    private void registerWoods(WoodenObject... woods){
        for(WoodenObject wood : woods)
        {
            String treeName = wood.getWoodType().name().split(":")[1];

            planks(wood.getPlanks().getPrimary(),treeName);
            this.axisBlock((RotatedPillarBlock) wood.getLog().getPrimary(), woodBlocksLocation(treeName,"log"));
            this.doorBlock((DoorBlock) wood.getDoor().getPrimary(), woodBlocksLocation(treeName,"door_bottom"), woodBlocksLocation(treeName,"door_top"));
            this.fenceGateBlock((FenceGateBlock) wood.getGate().getPrimary(), woodBlocksLocation(treeName,"planks"));
            this.slabBlock((SlabBlock) wood.getSlab().getPrimary(),wood.getPlanks().getPrimary().getRegistryName(), woodBlocksLocation(treeName,"planks"));
            this.stairsBlock((StairsBlock) wood.getStairs().getPrimary(), woodBlocksLocation(treeName,"planks"));
            this.trapdoorBlock((TrapDoorBlock) wood.getTrapdoor().getPrimary(), woodBlocksLocation(treeName,"trapdoor"),true);
            this.signBlock((StandingSignBlock) wood.getSign().getSign(), (WallSignBlock) wood.getSign().getWallSign(), woodBlocksLocation(treeName,"planks"));
            this.buttonBlock((AbstractButtonBlock) wood.getButton().getPrimary(),treeName);
            this.pressurePlate((PressurePlateBlock) wood.getPressure_plate().getPrimary(),treeName);
            this.woodBlock((RotatedPillarBlock) wood.getWood().getPrimary(),"log_side",treeName);

            this.fenceBlockWithItem((FenceBlock) wood.getFence().getPrimary(), woodBlocksLocation(treeName,"planks"));

            if(wood.getSapling()!= null) {
                this.sapling((SaplingBlock) wood.getSapling().getPrimary(), (FlowerPotBlock) wood.getPotted_sapling(),treeName);
                this.leaves((LeavesBlock) wood.getLeaves().getPrimary());

                basicBlockItem(wood.getLeaves().getPrimary());
                generatedItem(wood.getSapling().getPrimary(),woodBlocksLocation(treeName,"sapling"));
//            basicBlockItem(wood.getSapling().getPrimary()); beautiful! :D
            }

            if(wood.getStrippedLog()!= null){
                this.axisBlock((RotatedPillarBlock) wood.getStrippedLog().getPrimary(), woodBlocksLocation(treeName,"stripped"));
                this.woodBlock((RotatedPillarBlock) wood.getStrippedWood().getPrimary(),"stripped_side",treeName);

                basicBlockItem(wood.getStrippedLog().getPrimary());
                basicBlockItem(wood.getStrippedWood().getPrimary());
            }

            basicBlockItem(wood.getWood().getPrimary());
            basicBlockItem(wood.getStairs().getPrimary());
            basicBlockItem(wood.getSlab().getPrimary());
            basicBlockItem(wood.getPlanks().getPrimary());
            basicBlockItem(wood.getPressure_plate().getPrimary());
            buttonItem((AbstractButtonBlock) wood.getButton().getPrimary(),treeName);
            basicBlockItem(wood.getTrapdoor().getPrimary(),"_bottom");
            basicBlockItem(wood.getGate().getPrimary());
            generatedItem(wood.getDoor().getPrimary(),woodItemsLocation(treeName,"door"));
            basicBlockItem(wood.getLog().getPrimary());
            generatedItem(wood.getSign().getSign(),woodItemsLocation(treeName,"sign"));
            generatedItem(wood.getBoat().get(),woodItemsLocation(treeName,"boat"));
            if(wood.getFruit() != null)
                generatedItem(wood.getFruit().get(),woodItemsLocation(treeName,"berries"));
        }
    }

    private void buttonItem(AbstractButtonBlock block, String treeName) {
        ModelFile inventory = models().singleTexture(name(block) + "_inventory",
                new ResourceLocation(ModelProvider.BLOCK_FOLDER + "/button_inventory"), "texture",
                woodBlocksLocation(treeName,"planks"));
        simpleBlockItem(block, inventory);
    }

    private void generatedItem(IItemProvider block, ResourceLocation texture) {
        itemModels().getBuilder(Objects.requireNonNull(block.asItem().getRegistryName()).getPath())
                .parent(itemModels().getExistingFile(new ResourceLocation("item/generated")))
                .texture("layer0", texture);
    }

    private void basicBlockItem(Block block, String suffix) {
        simpleBlockItem(block, models().getExistingFile(modLoc(name(block)+suffix)));
    }

    private void basicBlockItem(Block block) {
        simpleBlockItem(block, models().getExistingFile(modLoc(name(block))));
    }

    private void woodBlock(RotatedPillarBlock block, String textureName,String treeName) {
        ResourceLocation tex = woodBlocksLocation(treeName,textureName);
        axisBlock(block, models().cubeColumn(name(block), tex, tex),
                models().cubeColumnHorizontal(name(block), tex, tex));
    }

    private void pressurePlate(PressurePlateBlock block,String treeName) {
        ModelFile up = models()
                .singleTexture(name(block), new ResourceLocation(ModelProvider.BLOCK_FOLDER + "/pressure_plate_up"),
                        "texture", woodBlocksLocation(treeName,"planks"));
        ModelFile down = models().singleTexture(name(block) + "_down",
                new ResourceLocation(ModelProvider.BLOCK_FOLDER + "/pressure_plate_down"), "texture",
                woodBlocksLocation(treeName,"planks"));
        getVariantBuilder(block).partialState().with(PressurePlateBlock.POWERED, false)
                .addModels(new ConfiguredModel(up)).partialState().with(PressurePlateBlock.POWERED, true)
                .addModels(new ConfiguredModel(down));
    }

    private void sapling(SaplingBlock block, FlowerPotBlock block2,String treeName) {
        simpleBlock(block, models().cross(name(block), woodBlocksLocation(treeName,"sapling")));
        simpleBlock(block2, models().singleTexture(name(block2),
                new ResourceLocation(ModelProvider.BLOCK_FOLDER + "/flower_pot_cross"), "plant", woodBlocksLocation(treeName,"sapling")));
    }

    private void planks(Block block,String treeName) {
        simpleBlock(block,models().cubeAll(name(block), woodBlocksLocation(treeName,"planks")));
    }

    private void leaves(LeavesBlock block) {
        simpleBlock(block,
                models().singleTexture(name(block), new ResourceLocation(ModelProvider.BLOCK_FOLDER + "/leaves"), "all",
                        blockTexture(Blocks.OAK_LEAVES)));
    }

    private void leaves(LeavesBlock block,String treeName) {
        simpleBlock(block,
                models().singleTexture(name(block), new ResourceLocation(ModelProvider.BLOCK_FOLDER + "/leaves"), "all",
                        woodBlocksLocation(treeName,"leaves")));
    }

    private String name(Block block) {
        return Objects.requireNonNull(block.getRegistryName()).getPath();
    }

    private void signBlock(StandingSignBlock sign, WallSignBlock wallSign, ResourceLocation texture) {
        ModelFile model = models().getBuilder(name(sign)).texture("particle", texture);
        simpleBlock(sign, model);
        simpleBlock(wallSign, model);
    }

    private void buttonBlock(AbstractButtonBlock block,String treeName) {
        ModelFile normal = models()
                .singleTexture(name(block), new ResourceLocation(ModelProvider.BLOCK_FOLDER + "/button"), "texture",
                        woodBlocksLocation(treeName,"planks"));
        ModelFile pressed = models().singleTexture(name(block) + "_pressed",
                new ResourceLocation(ModelProvider.BLOCK_FOLDER + "/button_pressed"), "texture",
                woodBlocksLocation(treeName,"planks"));
        VariantBlockStateBuilder builder = getVariantBuilder(block);
        for (Direction d : Direction.Plane.HORIZONTAL) {
            for (AttachFace f : AttachFace.values()) {
                int rotX = 0;
                switch (f) {
                    case CEILING:
                        rotX = 180;
                        break;
                    case WALL:
                        rotX = 90;
                        break;
                }
                int rotY = 180;
                switch (d) {
                    case EAST:
                        rotY = 90;
                        break;
                    case WEST:
                        rotY = 270;
                        break;
                    case NORTH:
                        rotY = 0;
                        break;
                }
                builder.partialState().with(AbstractButtonBlock.FACE, f).with(AbstractButtonBlock.FACING, d)
                        .with(AbstractButtonBlock.POWERED, true).modelForState().rotationX(rotX).rotationY(rotY)
                        .modelFile(pressed).addModel().partialState().with(AbstractButtonBlock.FACE, f)
                        .with(AbstractButtonBlock.FACING, d).with(AbstractButtonBlock.POWERED, false)
                        .modelForState().rotationX(rotX).rotationY(rotY).modelFile(normal).addModel();
            }
        }
    }

    public void fenceBlockWithItem(FenceBlock block, ResourceLocation texture) {
        fenceBlock(block, texture);
        ModelFile inventory = models().singleTexture(name(block) + "_inventory",
                new ResourceLocation(ModelProvider.BLOCK_FOLDER + "/fence_inventory"), "texture", texture);
        simpleBlockItem(block, inventory);
    }

    private ResourceLocation woodBlocksLocation(String treeName, String name){
        return modLoc("blocks/trees/"+treeName+"/"+name);
    }
    private ResourceLocation woodItemsLocation(String treeName, String name){
        return modLoc("items/trees/"+treeName+"/"+name);
    }
}
