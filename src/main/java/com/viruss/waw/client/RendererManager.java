package com.viruss.waw.client;

import com.viruss.waw.WitchingAndWizardry;
import com.viruss.waw.common.objects.blocks.ChalkSymbol;
import com.viruss.waw.common.objects.items.Chalk;
import com.viruss.waw.utils.RegistryHandler;
import com.viruss.waw.utils.registrations.DoubleRegisteredObject;
import net.minecraft.block.Block;
import net.minecraft.block.ChainBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = WitchingAndWizardry.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RendererManager {
    private final Map<EntityType<? extends Entity>, IRenderFactory<? super Entity>> ENTITY_RENDERERS =new HashMap<>();
    private final Map<RegistryObject<Block>, RenderType> BLOCK_RENDERERS = new HashMap<>();
    private final Map<TileEntityType<? extends TileEntity>, Function<? super TileEntityRendererDispatcher, ? extends TileEntityRenderer<? super TileEntity>>> TILE_RENDERERS =new HashMap<>();

    public void init(){
        for(TileEntityType<? extends TileEntity> type : TILE_RENDERERS.keySet())
            ClientRegistry.bindTileEntityRenderer(type, TILE_RENDERERS.get(type));

        for(EntityType<?> type : ENTITY_RENDERERS.keySet())
            RenderingRegistry.registerEntityRenderingHandler(type, ENTITY_RENDERERS.get(type));

        for(RegistryObject<Block> ro : BLOCK_RENDERERS.keySet())
            RenderTypeLookup.setRenderLayer(ro.get(), BLOCK_RENDERERS.get(ro));
    }

    public <T extends Entity> void addEntityRender(EntityType<T> entityClass, IRenderFactory<? super T> renderFactory)
    {
        this.ENTITY_RENDERERS.put(entityClass, (IRenderFactory<? super Entity>) renderFactory);
    }

    public void addBlockRenderer(DoubleRegisteredObject<Block, Item> block, RenderType renderType)
    {
        this.BLOCK_RENDERERS.put(block.getPrimaryRO(),renderType);
    }
    public void addBlockRenderer(RegistryObject<Block> block, RenderType renderType)
    {
        this.BLOCK_RENDERERS.put(block,renderType);
    }
    public <T extends TileEntity> void addTileEntityRenderer(TileEntityType<T> tileEntityType, Function<? super TileEntityRendererDispatcher, ? extends TileEntityRenderer<? super T>> renderType)
    {
        this.TILE_RENDERERS.put(tileEntityType, (Function<? super TileEntityRendererDispatcher, ? extends TileEntityRenderer<? super TileEntity>>) renderType);
    }



                            /*~~~~~ColorHandler~~~~~*/
    @SubscribeEvent
    public static void registerBlockColors(ColorHandlerEvent.Block event) {
        BlockColors colors = event.getBlockColors();

        colors.register((state, blockDisplayReader, pos, i) -> {
            if (i > 15) return 0xFFFFFF;
            if (blockDisplayReader == null || pos == null) return 0x74ff33;
            return BiomeColors.getAverageFoliageColor(blockDisplayReader,pos)+0x1f420e;
        }, RegistryHandler.ASH.getLeaves().getPrimary());

        RegistryHandler.CHALKS.getTypes((type, chalkObject) -> {
            colors.register((p_getColor_1_, p_getColor_2_, p_getColor_3_, p_getColor_4_) ->
                    ((ChalkSymbol)chalkObject.getSymbol()).getColor(),chalkObject.getSymbol());
        });

    }

    @SubscribeEvent
    public static void registerItemColors(ColorHandlerEvent.Item event) {
        ItemColors items = event.getItemColors();
        BlockColors blocks = event.getBlockColors();
        items.register(((itemStack, i) -> blocks.getColor(((BlockItem)itemStack.getItem()).getBlock().defaultBlockState(),null,null,i)),RegistryHandler.ASH.getLeaves().getSecondary());
        RegistryHandler.CHALKS.getTypes((type, chalkObject) -> {
            items.register((p_getColor_1_, p_getColor_2_) -> ((Chalk)chalkObject.getChalk()).getColor(),chalkObject.getChalk());
        });
    }
}
