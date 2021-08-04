package com.viruss.waw.client.renders;

import com.mojang.datafixers.util.Pair;
import com.viruss.waw.Main;
import com.viruss.waw.common.entities.CustomBoatEntity;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;

public class CustomBoatRenderer extends BoatRenderer {
    private final BoatModel boat;

    public CustomBoatRenderer(EntityRendererProvider.Context renderContext) {
        super(renderContext);
        boat = new BoatModel(renderContext.bakeLayer(ModelLayers.createBoatModelName(Boat.Type.OAK)));
    }

    @Override
    public Pair<ResourceLocation, BoatModel> getModelWithLocation(Boat entity) {
        CustomBoatEntity boat = (CustomBoatEntity) entity;
        return Pair.of(new ResourceLocation(Main.MOD_ID, "textures/entity/boats/" +boat.getDataType()+".png"),this.boat);
    }
}