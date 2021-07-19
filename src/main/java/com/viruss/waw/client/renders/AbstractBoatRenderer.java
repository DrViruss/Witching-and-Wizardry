package com.viruss.waw.client.renders;

import com.viruss.waw.WitchingAndWizardry;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class AbstractBoatRenderer extends BoatRenderer {
    public AbstractBoatRenderer(EntityRendererManager p_i46190_1_) {
        super(p_i46190_1_);
    }

    @Override
    public ResourceLocation getTextureLocation(BoatEntity entity) {
        String[] name = Objects.requireNonNull(entity.getType().getRegistryName()).getPath().split("_");
        return new ResourceLocation(WitchingAndWizardry.MOD_ID,"textures/entity/boats/"+name[0]+".png");
    }
}
