package com.viruss.waw.utils;

import com.viruss.waw.WitchingAndWizardry;
import com.viruss.waw.common.objects.blocks.ChalkSet;
import com.viruss.waw.common.objects.blocks.MortarAndPestle;
import com.viruss.waw.common.objects.blocks.SignObject;
import com.viruss.waw.common.objects.blocks.WoodenObject;
import com.viruss.waw.common.objects.items.Chalk;
import com.viruss.waw.common.worldgen.Features;
import com.viruss.waw.utils.registration.MultyDeferredRegister;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.SignTileEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class RegistryHandler {
    public static final MultyDeferredRegister MDR = new MultyDeferredRegister(WitchingAndWizardry.MOD_ID,new IForgeRegistry[]{
            ForgeRegistries.BLOCKS,
            ForgeRegistries.ITEMS,
            ForgeRegistries.ENTITIES,
            ForgeRegistries.TILE_ENTITIES,
            ForgeRegistries.SOUND_EVENTS
    });

    public static void init(IEventBus bus)
    {
        MDR.register(bus);
    }


                            /*NOTE: add all generatedObjects to BlockStateProvider */
    public static final WoodenObject ASH = new WoodenObject("ash",WitchingAndWizardry.ITEM_GROUP,()-> Features.Trees.ASH,true);
    public static final MortarAndPestle MORTAR_AND_PESTLE = new MortarAndPestle();
    public static final TileEntityType<SignObject.AbstractSignTileEntity> SIGN_TE = registerSign(ASH);
    public static final ChalkSet CHALKS = new ChalkSet(new Chalk.Type[]{
         Chalk.Type.WHITE,
         Chalk.Type.RED
    });

    private static Item registerItem(String name, Item item)
    {
        MDR.register(name,()->item,ForgeRegistries.ITEMS);
        return item;
    }
    private static Block registerBlock(String name, Block block)
    {
        MDR.register(name,()->block,ForgeRegistries.BLOCKS);
        return block;
    }
    private static TileEntityType<SignObject.AbstractSignTileEntity> registerSign(WoodenObject... woods)
    {
        Block[] blocks = new Block[(woods.length)*2];
        for (int i =0; i<woods.length;i++)
        {
            SignObject sign =woods[i].getSign();
            blocks[i] = sign.getSign();
            blocks[i+1] = sign.getWallSign();
        }
        System.out.println("woodsSize="+woods.length+" \t blocksSize="+blocks.length);

        TileEntityType<SignObject.AbstractSignTileEntity> te = TileEntityType.Builder.of(SignObject.AbstractSignTileEntity::new,blocks).build(null);
        MDR.register("sign_te",()->te,ForgeRegistries.TILE_ENTITIES);
        WitchingAndWizardry.CLIENT_RENDERER.addTileEntityRenderer(te, SignTileEntityRenderer::new);

        return te;
    }
}