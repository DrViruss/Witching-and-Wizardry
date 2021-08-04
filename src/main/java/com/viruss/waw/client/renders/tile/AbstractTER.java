package com.viruss.waw.client.renders.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.viruss.waw.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.EmptyModelData;

public class AbstractTER<T extends BlockEntity> implements BlockEntityRenderer<T> {
    private final Minecraft minecraft;
    public AbstractTER() {
        this.minecraft = Minecraft.getInstance();
    }

    @Override
    public void render(T tileEntity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        renderBlock(tileEntity, stack, buffer, combinedLight, combinedOverlay);

    }
    private void renderBlock(T tileEntity, PoseStack stack, MultiBufferSource buffer, int combinedLight, int combinedOverlay){
        BlockState state = tileEntity.getBlockState();
        BlockRenderDispatcher dispatcher = minecraft.getBlockRenderer();
        int color = minecraft.getBlockColors().getColor(state, null, null, 0);

        dispatcher.getModelRenderer().renderModel(stack.last(), buffer.getBuffer(ItemBlockRenderTypes.getRenderType(state, false)), state, dispatcher.getBlockModel(state), ModUtils.Colors.getRed(color), ModUtils.Colors.getGreen(color), ModUtils.Colors.getBlue(color), combinedLight, combinedOverlay, EmptyModelData.INSTANCE);
    }
}
