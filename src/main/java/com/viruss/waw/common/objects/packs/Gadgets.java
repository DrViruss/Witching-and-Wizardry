package com.viruss.waw.common.objects.packs;

import com.viruss.waw.Main;
import com.viruss.waw.client.renders.tile.BrazierRenderer;
import com.viruss.waw.client.renders.tile.MortarRenderer;
import com.viruss.waw.common.objects.blocks.BrazierBlock;
import com.viruss.waw.common.objects.blocks.MortarBlock;
import com.viruss.waw.common.tile.BrazierTE;
import com.viruss.waw.common.tile.MortarTE;
import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("all")
public class Gadgets {

    private final Block mortar;
    private final Item pestle;
    private final BlockEntityType<MortarTE> mortarTE;

    private final Block brazier;
    private final BlockEntityType<BrazierTE> brazierTE;

    public Gadgets() {
        com.viruss.waw.utils.registration.Block.Builder builder = new com.viruss.waw.utils.registration.Block.Builder().needItem(Main.ITEM_GROUP);
        this.mortar = new MortarBlock();
        this.pestle = new Item(new Item.Properties().tab(Main.ITEM_GROUP).stacksTo(1));
        this.mortarTE = BlockEntityType.Builder.of(MortarTE::new,mortar).build(null);
        this.brazier = new BrazierBlock();
        this.brazierTE = BlockEntityType.Builder.of(BrazierTE::new, brazier).build(null);

        ModRegistry.MDR.register("mortar",builder.setBlockSup(() -> mortar));
        ModRegistry.MDR.register("pestle",()->pestle, ForgeRegistries.ITEMS);
        ModRegistry.MDR.register("mortar",()->mortarTE,ForgeRegistries.BLOCK_ENTITIES);
        ModRegistry.MDR.register("ancient_brazier",builder.setBlockSup(() -> brazier));
        ModRegistry.MDR.register("ancient_brazier",()->brazierTE,ForgeRegistries.BLOCK_ENTITIES);

        Main.CLIENT_RENDERER.addTileEntityRenderer(mortarTE, MortarRenderer::new);
        Main.CLIENT_RENDERER.addTileEntityRenderer(brazierTE, BrazierRenderer::new);

    }

    public BlockEntityType<MortarTE> getMortarTE() {
        return mortarTE;
    }

    public Block getMortar() {
        return mortar;
    }

    public Item getPestle() {
        return pestle;
    }

    public Block getBrazier() {
        return brazier;
    }

    public BlockEntityType<BrazierTE> getBrazierTE() {
        return brazierTE;
    }
}
