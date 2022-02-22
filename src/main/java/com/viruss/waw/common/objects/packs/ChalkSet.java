package com.viruss.waw.common.objects.packs;

import com.viruss.waw.Main;
import com.viruss.waw.client.renders.tile.CentralSymbolRenderer;
import com.viruss.waw.common.objects.blocks.chalk.BasicSymbol;
import com.viruss.waw.common.objects.blocks.chalk.CentralSymbol;
import com.viruss.waw.common.objects.items.Chalk;
import com.viruss.waw.common.tile.CentralSymbolTE;
import com.viruss.waw.utils.ModUtils;
import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.awt.*;

@SuppressWarnings("all")
public class ChalkSet {
    public static final int[] COLORS = new int[]{
            Color.WHITE.getRGB(),
            Color.RED.getRGB(),     //TODO: think about strings     15-29 ID MaterialColor
            Color.LIGHT_GRAY.getRGB()
    };

    private RegistryObject<SoundEvent> sound;
    private final BlockEntityType<CentralSymbolTE> centerTE;
    private final RegistryObject<Block> basicBlock;
    private final RegistryObject<Block> centralBlock;
    private final Item chalk = new Chalk();

    public ChalkSet() {
        ModRegistry.MDR.register("chalk",() -> chalk, ForgeRegistries.ITEMS);
        Block center = new CentralSymbol();
        centralBlock = ModRegistry.MDR.register("symbol_central",() -> center, ForgeRegistries.BLOCKS);
        basicBlock = ModRegistry.MDR.register("symbol_basic", BasicSymbol::new, ForgeRegistries.BLOCKS);

        this.centerTE = BlockEntityType.Builder.of(CentralSymbolTE::new,center).build(null);
        ModRegistry.MDR.register("center",()->centerTE,ForgeRegistries.BLOCK_ENTITIES);


        initAdditional();
    }

    private void initAdditional() {
        Main.CLIENT_RENDERER.addBlockRenderer(centralBlock, RenderType.translucent());
        Main.CLIENT_RENDERER.addBlockRenderer(basicBlock, RenderType.cutout());
        Main.CLIENT_RENDERER.addTileEntityRenderer(this.centerTE, CentralSymbolRenderer::new);

        sound = ModRegistry.MDR.register("chalk", ()-> ModUtils.Resources.loadSound(Main.MOD_ID,"chalk"),ForgeRegistries.SOUND_EVENTS);
    }

    public SoundEvent getSound() {
        return sound.get();
    }

    public BlockEntityType<CentralSymbolTE> getCenterTE() {
        return centerTE;
    }

    public Block getBasicBlock() {
        return basicBlock.get();
    }

    public Block getCentralBlock() {
        return centralBlock.get();
    }

    public Item getChalk() {
        return chalk;
    }
}
