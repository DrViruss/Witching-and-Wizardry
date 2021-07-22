package com.viruss.waw.client.renders;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.viruss.waw.common.tile.MortarTE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

import java.util.ArrayList;
import java.util.Random;

public class MortarRenderer extends TileEntityRenderer<MortarTE> {

    public MortarRenderer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }

    @Override
    public void render(MortarTE te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int combinedLight, int combinedOverlay) {
        te.getInventory().ifPresent(itemStackHandler -> {
            for(int i=0; i< itemStackHandler.getSlots();i++)
            {
                ItemStack stack = itemStackHandler.getStackInSlot(i);
                if(stack == ItemStack.EMPTY)
                    continue;
                matrixStack.pushPose();
                Random random = new Random(stack.getItem().hashCode());
                int rotation = random.nextInt(random.nextInt(360));
                matrixStack.translate(0.5,0.2 + random.nextFloat()/16,0.5);
                matrixStack.mulPose(Vector3f.YN.rotationDegrees(rotation));
                matrixStack.scale(0.375F, 0.375F, 0.375F);
                Minecraft.getInstance().getItemRenderer().renderStatic(stack,ItemCameraTransforms.TransformType.FIXED,combinedLight,combinedOverlay,matrixStack,iRenderTypeBuffer);
                matrixStack.popPose();
            }
        });
    }
}
