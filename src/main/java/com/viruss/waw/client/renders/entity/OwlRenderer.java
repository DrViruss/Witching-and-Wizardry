package com.viruss.waw.client.renders.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.viruss.waw.Main;
import com.viruss.waw.common.entities.OwlEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("all")
public class OwlRenderer extends EntityRenderer<OwlEntity> {
    EntityModel<OwlEntity> model;

    public OwlRenderer(EntityRendererProvider.Context context) {
        super(context);
//        model = new OwlModel<>(context.bakeLayer(RendererManager.OWL_LAYER));
    }

    @Override
    public void render(OwlEntity p_114485_, float p_114486_, float p_114487_, PoseStack p_114488_, MultiBufferSource p_114489_, int p_114490_) {
        super.render(p_114485_, p_114486_, p_114487_, p_114488_, p_114489_, p_114490_);
        VertexConsumer vertexconsumer = p_114489_.getBuffer(this.model.renderType(this.getTextureLocation(p_114485_)));
        this.model.renderToBuffer(p_114488_,vertexconsumer,p_114490_, OverlayTexture.NO_OVERLAY,1,1,1,1);
    }

    @Override
    public ResourceLocation getTextureLocation(OwlEntity p_114482_) {
        return new ResourceLocation(Main.MOD_ID, "textures/entity/owls/pattern.png");
    }
}
