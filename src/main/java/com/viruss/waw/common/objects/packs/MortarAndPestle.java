package com.viruss.waw.common.objects.packs;

import com.viruss.waw.Main;
import com.viruss.waw.client.renders.tile.MortarRenderer;
import com.viruss.waw.common.objects.blocks.MortarBlock;
import com.viruss.waw.common.tile.MortarTE;
import com.viruss.waw.utils.recipes.RecipeTypes;
import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@SuppressWarnings("all")
public class MortarAndPestle {

    private final Block mortar;
    private final Item pestle;
    private final BlockEntityType<MortarTE> mortarTE;

    public MortarAndPestle() {
        this.mortar = new MortarBlock();
        this.pestle = new Item(new Item.Properties().tab(Main.ITEM_GROUP).stacksTo(1));
        this.mortarTE = BlockEntityType.Builder.of(MortarTE::new,mortar).build(null);

        ModRegistry.MDR.register("mortar",new com.viruss.waw.utils.registration.Block.Builder().setBlockSup(() -> mortar).needItem(Main.ITEM_GROUP));
        ModRegistry.MDR.register("pestle",()->pestle, ForgeRegistries.ITEMS);
        ModRegistry.MDR.register("mortar",()->mortarTE,ForgeRegistries.BLOCK_ENTITIES);

        Main.CLIENT_RENDERER.addTileEntityRenderer(mortarTE, MortarRenderer::new);
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
}
