package com.viruss.waw.client.renders;

import com.viruss.waw.WitchingAndWizardry;
import com.viruss.waw.common.entities.CustomBoatEntity;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.util.ResourceLocation;

public class CustomBoatRenderer extends BoatRenderer {

    public CustomBoatRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public ResourceLocation getTextureLocation(BoatEntity entity) {
        CustomBoatEntity boat = (CustomBoatEntity) entity;
        return new ResourceLocation(WitchingAndWizardry.MOD_ID, "textures/entity/boats/" +boat.getDataType()+".png");
    }
}