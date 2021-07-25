package com.viruss.waw.common.objects.packs;

import com.viruss.waw.WitchingAndWizardry;
import com.viruss.waw.client.renders.MortarRenderer;
import com.viruss.waw.common.objects.blocks.MortarBlock;
import com.viruss.waw.common.tile.MortarTE;
import com.viruss.waw.utils.ModRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ForgeRegistries;

public class MortarAndPestle {
    private final Block mortar;
    private final Item pestle;
    private final TileEntityType<MortarTE> mortarTE;

    public MortarAndPestle() {
        this.mortar = new MortarBlock();
        this.pestle = new Item(new Item.Properties().tab(WitchingAndWizardry.ITEM_GROUP).stacksTo(1));
        this.mortarTE = TileEntityType.Builder.of(MortarTE::new,mortar).build(null);


        ModRegistry.MDR.register("mortar",new com.viruss.waw.utils.registration.Block.Builder().setBlockSup(() -> mortar).needItem(WitchingAndWizardry.ITEM_GROUP));
        ModRegistry.MDR.register("pestle",()->pestle, ForgeRegistries.ITEMS);
        ModRegistry.MDR.register("mortar",()->mortarTE,ForgeRegistries.TILE_ENTITIES);
        WitchingAndWizardry.CLIENT_RENDERER.addTileEntityRenderer(mortarTE, MortarRenderer::new);
    }

    public TileEntityType<MortarTE> getMortarTE() {
        return mortarTE;
    }

    public Block getMortar() {
        return mortar;
    }

    public Item getPestle() {
        return pestle;
    }
}
