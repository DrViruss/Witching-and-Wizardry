package com.viruss.waw.common.objects.blocks;

import com.viruss.waw.WitchingAndWizardry;
import com.viruss.waw.client.render.AbstractBoatRenderer;
import com.viruss.waw.common.objects.blocks.bases.*;
import com.viruss.waw.common.worldgen.Features;
import com.viruss.waw.utils.RegistryHandler;
import com.viruss.waw.utils.registrations.DoubleRegisteredObject;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.SignTileEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

public class WoodenObject {
    private final WoodType woodType;
    private final DoubleRegisteredObject<Block, Item> log;
    private DoubleRegisteredObject<Block, Item> stripped_log;
    private DoubleRegisteredObject<Block, Item> stripped_wood;
    private DoubleRegisteredObject<Block, Item> wood;
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
    private final BoatObject boat;

    public WoodenObject(String name, ItemGroup group,boolean isNatural,boolean isStrippable) {
        com.viruss.waw.utils.registrations.Block.Builder builder = new com.viruss.waw.utils.registrations.Block.Builder().needItem(group);
        this.woodType = WoodType.register(WoodType.create(WitchingAndWizardry.MOD_ID+":"+name));
        if (isStrippable) {
            this.stripped_log = RegistryHandler.MDR.register(name+"_stripped_log",builder.setBlockSup(AbstractLog::new));
            this.log = RegistryHandler.MDR.register(name+"_log",builder.setBlockSup(()-> new StripableLog(stripped_log.getPrimary())));
            this.stripped_wood = RegistryHandler.MDR.register(name+"_stripped_wood",builder.setBlockSup(AbstractLog::new));
            this.wood = RegistryHandler.MDR.register(name+"_wood",builder.setBlockSup(()-> new StripableLog(stripped_wood.getPrimary())));
        }
        else{
            this.log = RegistryHandler.MDR.register(name+"_log",builder.setBlockSup(AbstractLog::new));
        }
        this.planks = RegistryHandler.MDR.register(name+"_planks",builder.setBlockSup(AbstractPlanks::new));
        this.stairs = RegistryHandler.MDR.register(name+"_stairs",builder.setBlockSup(() -> new AbstractStairs(planks.getPrimary())));
        this.slab = RegistryHandler.MDR.register(name+"_slab",builder.setBlockSup(AbstractSlab::new));
        this.fence = RegistryHandler.MDR.register(name+"_fence",builder.setBlockSup(() -> new AbstractFence(planks.getPrimary().defaultMaterialColor())));
        this.gate = RegistryHandler.MDR.register(name+"_gate",builder.setBlockSup(() -> new AbstractFenceGate(planks.getPrimary().defaultMaterialColor())));
        this.door = RegistryHandler.MDR.register(name+"_door",builder.setBlockSup(() -> new DoorBlock(AbstractBlock.Properties.of(Material.WOOD, planks.getPrimary().defaultMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion())));
        this.trapdoor = RegistryHandler.MDR.register(name+"_trapdoor",builder.setBlockSup(()-> new TrapDoorBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(3.0F).sound(SoundType.WOOD).noOcclusion().isValidSpawn(RegistryHandler::never))));
        this.button = RegistryHandler.MDR.register(name+"_button",builder.setBlockSup(()-> new WoodButtonBlock(AbstractBlock.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.WOOD))));
        this.pressure_plate = RegistryHandler.MDR.register(name+"_pressure_plate",builder.setBlockSup(()-> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, AbstractBlock.Properties.of(Material.WOOD, planks.getPrimary().defaultMaterialColor()).noCollission().strength(0.5F).sound(SoundType.WOOD))));
        this.sign = new SignObject(name,this.woodType,group);
        this.boat = new BoatObject(name+"_boat",group);
        if(isNatural) {
            this.leaves = RegistryHandler.MDR.register(name + "_leaves", builder.setBlockSup(AbstractLeaves::new));
            this.sapling = RegistryHandler.MDR.register(name + "_sapling", builder.setBlockSup(() -> new AbstractSapling(new AbstractTree(Features.Trees.ASH))));
            this.potted_sapling = RegistryHandler.MDR.register(name + "_potted", () -> new FlowerPotBlock(null, this.sapling::getPrimary, AbstractBlock.Properties.copy(Blocks.FLOWER_POT)), ForgeRegistries.BLOCKS);
        }

        initRenders();
    }
    private void initRenders()
    {
        WitchingAndWizardry.CLIENT_RENDERER.addEntityRender(boat.getType(),AbstractBoatRenderer::new);
        WitchingAndWizardry.CLIENT_RENDERER.addBlockRenderer(door, RenderType.translucent());
        WitchingAndWizardry.CLIENT_RENDERER.addBlockRenderer(trapdoor, RenderType.translucent());
        WitchingAndWizardry.CLIENT_RENDERER.addTileEntityRenderer(sign.getTile(), SignTileEntityRenderer::new);
        if(sapling!=null) {
            WitchingAndWizardry.CLIENT_RENDERER.addBlockRenderer(sapling, RenderType.cutout());
            WitchingAndWizardry.CLIENT_RENDERER.addBlockRenderer(potted_sapling, RenderType.cutout());
        }
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

    public BoatObject getBoat() {
        return boat;
    }
}
