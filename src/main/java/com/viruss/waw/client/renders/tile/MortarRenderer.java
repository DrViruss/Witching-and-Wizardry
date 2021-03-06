package com.viruss.waw.client.renders.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.viruss.waw.common.tile.MortarTE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@SuppressWarnings("all")
@OnlyIn(Dist.CLIENT)
public class MortarRenderer extends AbstractTER<MortarTE> {
    public MortarRenderer(BlockEntityRendererProvider.Context context) {super();}

    @Override
    public void render(MortarTE tileEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        super.render(tileEntity,partialTicks,poseStack,bufferSource,combinedLight,combinedOverlay);
        for(int i=0; i< tileEntity.getInventory().getContainerSize();i++) {
            ItemStack stack = tileEntity.getInventory().getItem(i);
            if(stack == ItemStack.EMPTY) continue;

            poseStack.pushPose();
            Random random = new Random(stack.getItem().hashCode());
            poseStack.translate(0.5,0.2 + random.nextFloat()/16,0.5);
            poseStack.mulPose(Vector3f.YN.rotationDegrees(random.nextInt(random.nextInt(360))));
            poseStack.scale(0.375F, 0.375F, 0.375F);
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.FIXED,combinedLight,combinedOverlay,poseStack,bufferSource,0);
            poseStack.popPose();
        };
    }

}
