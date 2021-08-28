package com.viruss.waw.client.renders.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.viruss.waw.common.tile.CentralSymbolTE;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class CentralSymbolRenderer extends AbstractTER<CentralSymbolTE>{

    public CentralSymbolRenderer(BlockEntityRendererProvider.Context context) {
        super();
    }

    @Override
    public void render(CentralSymbolTE tileEntity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        super.render(tileEntity, partialTicks, stack, buffer, combinedLight, combinedOverlay);
    }
}
