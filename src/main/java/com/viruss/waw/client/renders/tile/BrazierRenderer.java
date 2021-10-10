package com.viruss.waw.client.renders.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.viruss.waw.common.objects.blocks.BrazierBlock;
import com.viruss.waw.common.tile.BrazierTE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class BrazierRenderer extends AbstractTER<BrazierTE>{
    public BrazierRenderer(BlockEntityRendererProvider.Context context) {super();}

    @Override
    public void render(BrazierTE tileEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        super.render(tileEntity, partialTicks, poseStack, bufferSource, combinedLight, combinedOverlay);
        poseStack.pushPose();
        poseStack.translate(0.3,0.89,0.3);
        poseStack.scale(0.4F, 0.4F, 0.4F);
        if(tileEntity.getBlockState().getValue(BrazierBlock.LIT))
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(Blocks.FIRE.defaultBlockState(),poseStack,bufferSource,combinedLight,combinedOverlay, EmptyModelData.INSTANCE);
        poseStack.popPose();

        for(int i=0; i< tileEntity.getInventory().getContainerSize();i++) {
            ItemStack stack = tileEntity.getInventory().getItem(i);
            if(stack == ItemStack.EMPTY) return;
            poseStack.pushPose();

            poseStack.translate(0.5,1.02,0.5);
            poseStack.scale(0.2F, 0.2F, 0.2F);
            poseStack.mulPose(new Quaternion(90,0,new Random(stack.getItem().hashCode()^i).nextInt(360),true));
            poseStack.translate(0.5,1.02,0.5);
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.FIXED, combinedLight, combinedOverlay, poseStack, bufferSource, 0);

            poseStack.popPose();
        }
    }
}
