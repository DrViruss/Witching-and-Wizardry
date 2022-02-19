package com.viruss.waw.utils.registries;

import com.viruss.waw.Main;
import com.viruss.waw.client.renders.entity.BroomRenderer;
import com.viruss.waw.common.entities.BroomEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("all")
public class EntityRegistry {
    private final EntityType<BroomEntity> BROOM = registerBroom();
//    private final EntityType<OwlEntity> OWL = registerOwl();

//    private EntityType<OwlEntity> registerOwl() {
//        EntityType<OwlEntity> te = EntityType.Builder.<OwlEntity>of(OwlEntity::new, MobCategory.MISC).sized(0.5f, 1f).clientTrackingRange(10).build("owl");
//        ModRegistry.MDR.register("owl",()->te, ForgeRegistries.ENTITIES);
//        Main.CLIENT_RENDERER.addEntityRender(te, OwlRenderer::new);
//        return te;
//    }


    private EntityType<BroomEntity> registerBroom()
    {
        EntityType<BroomEntity> te = EntityType.Builder.<BroomEntity>of(BroomEntity::new, MobCategory.MISC).sized(.3f, .3f).clientTrackingRange(10).build("broom");
        ModRegistry.MDR.register("broom",()->te, ForgeRegistries.ENTITIES);
        Main.CLIENT_RENDERER.addEntityRender(te, BroomRenderer::new);
        return te;
    }

    public EntityType<BroomEntity> getBroom() {
        return BROOM;
    }
//    public EntityType<OwlEntity> getOwl() {
//        return OWL;
//    }
}
