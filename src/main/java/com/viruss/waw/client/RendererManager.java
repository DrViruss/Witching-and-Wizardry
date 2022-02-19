package com.viruss.waw.client;

import com.viruss.waw.Main;
import com.viruss.waw.client.models.BroomModel;
import com.viruss.waw.common.objects.blocks.chalk.BasicSymbol;
import com.viruss.waw.utils.ModUtils;
import com.viruss.waw.utils.registration.DoubleRegisteredObject;
import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.RegistryObject;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Main.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RendererManager {
    private final Map<EntityType<? extends Entity>, EntityRendererProvider<Entity>> ENTITY_RENDERERS =new HashMap<>();
    private final Map<RegistryObject<Block>, RenderType> BLOCK_RENDERERS = new HashMap<>();
    private final Map<BlockEntityType<? extends BlockEntity>, BlockEntityRendererProvider<BlockEntity>> TILE_RENDERERS =new HashMap<>();

    public void init(){
        for(BlockEntityType<? extends BlockEntity> type : TILE_RENDERERS.keySet())
            BlockEntityRenderers.register(type, TILE_RENDERERS.get(type));

        for(EntityType<?> type : ENTITY_RENDERERS.keySet())
            EntityRenderers.register(type, ENTITY_RENDERERS.get(type));

        for(RegistryObject<Block> ro : BLOCK_RENDERERS.keySet())
            ItemBlockRenderTypes.setRenderLayer(ro.get(), BLOCK_RENDERERS.get(ro));
    }

    public <T extends Entity> void addEntityRender(EntityType<? extends T> entityType, EntityRendererProvider<T> renderFactory)
    {
        this.ENTITY_RENDERERS.put(entityType, (EntityRendererProvider<Entity>) renderFactory);
    }
    public void addBlockRenderer(DoubleRegisteredObject<Block, Item> block, RenderType renderType)
    {
        this.BLOCK_RENDERERS.put(block.getPrimaryRO(),renderType);
    }
    public void addBlockRenderer(RegistryObject<Block> block, RenderType renderType)
    {
        this.BLOCK_RENDERERS.put(block,renderType);
    }
    public <T extends BlockEntity> void addTileEntityRenderer(BlockEntityType<? extends T> tileEntityType, BlockEntityRendererProvider<T> renderType)
    {
        this.TILE_RENDERERS.put(tileEntityType, (BlockEntityRendererProvider<BlockEntity>) renderType);
    }



                            /*~~~~~ColorHandler~~~~~*/
    @SubscribeEvent
    public static void registerBlockColors(ColorHandlerEvent.Block event) {
        BlockColors colors = event.getBlockColors();

        colors.register((state, blockDisplayReader, pos, i) -> {
            if (blockDisplayReader == null || pos == null) return 0x74ff33;
            return BiomeColors.getAverageFoliageColor(blockDisplayReader,pos)+0x1f420e;
        }, ModRegistry.WOOD.ASH.getLeaves().getPrimary());

        colors.register((state, blockDisplayReader, pos, i) -> {
            if (blockDisplayReader == null || pos == null) return 3473453;
            return BiomeColors.getAverageFoliageColor(blockDisplayReader,pos) & -12779459;
        }, ModRegistry.WOOD.SAMBUCUS.getLeaves().getPrimary());


        /*0x6DAD32 lightblue*/

        colors.register((p_92567_, p_92568_, p_92569_, p_92570_) -> ModUtils.Colors.propertyToColor(p_92567_.getValue(BasicSymbol.COLOR)),ModRegistry.CHALKS.getBasicBlock(),ModRegistry.CHALKS.getCentralBlock());
    }

    @SubscribeEvent
    public static void registerItemColors(ColorHandlerEvent.Item event) {
        ItemColors items = event.getItemColors();
        BlockColors blocks = event.getBlockColors();

        /*~    Chalks     ~*/
        items.register((p_92672_, p_92673_) -> p_92672_.getTag().contains("color") ? p_92672_.getTag().getInt("color") : 0xFFFFFF,ModRegistry.CHALKS.getChalk());

                /*~    Leaves     ~*/
        items.register(((itemStack, i) -> blocks.getColor(((BlockItem)itemStack.getItem()).getBlock().defaultBlockState(),null,null,i)), ModRegistry.WOOD.ASH.getLeaves().getSecondary());
        items.register(((itemStack, i) -> blocks.getColor(((BlockItem)itemStack.getItem()).getBlock().defaultBlockState(),null,null,i)), ModRegistry.WOOD.SAMBUCUS.getLeaves().getSecondary());


        /*~    Dusts     ~*/
        items.register((p_92672_, p_92673_) -> Color.DARK_GRAY.getRGB(),ModRegistry.INGREDIENTS.getMagicAsh());
    }


    /*      ~Layers~     */
    public final static ModelLayerLocation BROOM_LAYER = new ModelLayerLocation(new ResourceLocation(Main.MOD_ID, "broom"), "broom");
//    public final static ModelLayerLocation OWL_LAYER = new ModelLayerLocation(new ResourceLocation(Main.MOD_ID, "owl"), "owl");


    @SubscribeEvent
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BROOM_LAYER, BroomModel::createBodyLayer);
    }
}
