package com.viruss.waw.utils.registries;

import com.viruss.waw.Main;
import com.viruss.waw.client.renders.CustomBoatRenderer;
import com.viruss.waw.client.renders.entity.BroomRenderer;
import com.viruss.waw.common.entities.BroomEntity;
import com.viruss.waw.common.entities.CustomBoatEntity;
import com.viruss.waw.common.objects.packs.SignPack;
import com.viruss.waw.common.objects.packs.WoodenPack;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("all")
public class EntityRegistry {
    private final BlockEntityType<SignPack.AbstractSignTileEntity> SIGN_TILE;
    private final EntityType<CustomBoatEntity> BOAT = registerBoat();
    private final EntityType<BroomEntity> BROOM = registerBroom();
//    private final EntityType<OwlEntity> OWL = registerOwl();

//    private EntityType<OwlEntity> registerOwl() {
//        EntityType<OwlEntity> te = EntityType.Builder.<OwlEntity>of(OwlEntity::new, MobCategory.MISC).sized(0.5f, 1f).clientTrackingRange(10).build("owl");
//        ModRegistry.MDR.register("owl",()->te, ForgeRegistries.ENTITIES);
//        Main.CLIENT_RENDERER.addEntityRender(te, OwlRenderer::new);
//        return te;
//    }


    public EntityRegistry(WoodenPack... woods) {
        this.SIGN_TILE = registerSign(woods);
    }

    private BlockEntityType<SignPack.AbstractSignTileEntity> registerSign(WoodenPack... woods)
    {
        Block[] blocks = new Block[woods.length*2];
        int j=0;
        for (WoodenPack wood : woods) {
            SignPack sign = wood.getSign();
            blocks[j] = sign.getSign();
            blocks[j + 1] = sign.getWallSign();
            j = j + 2;
        }
        BlockEntityType<SignPack.AbstractSignTileEntity> te = BlockEntityType.Builder.of(SignPack.AbstractSignTileEntity::new,blocks).build(null);
        ModRegistry.MDR.register("sign_te",()->te, ForgeRegistries.BLOCK_ENTITIES);
        Main.CLIENT_RENDERER.addTileEntityRenderer(te, SignRenderer::new);

        return te;
    }
    private EntityType<CustomBoatEntity> registerBoat()
    {
        EntityType<CustomBoatEntity> te = EntityType.Builder.<CustomBoatEntity>of(CustomBoatEntity::new, MobCategory.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10).build("boat");
        ModRegistry.MDR.register("boat",()->te, ForgeRegistries.ENTITIES);
        Main.CLIENT_RENDERER.addEntityRender(te, CustomBoatRenderer::new);
        return te;
    }
    private EntityType<BroomEntity> registerBroom()
    {
        EntityType<BroomEntity> te = EntityType.Builder.<BroomEntity>of(BroomEntity::new, MobCategory.MISC).sized(.3f, .3f).clientTrackingRange(10).build("broom");
        ModRegistry.MDR.register("broom",()->te, ForgeRegistries.ENTITIES);
        Main.CLIENT_RENDERER.addEntityRender(te, BroomRenderer::new);
        return te;
    }

    public BlockEntityType<SignPack.AbstractSignTileEntity> getSignTileEntity() {
        return SIGN_TILE;
    }
    public EntityType<CustomBoatEntity> getBoatEntity() {
        return BOAT;
    }
    public EntityType<BroomEntity> getBroom() {
        return BROOM;
    }
//    public EntityType<OwlEntity> getOwl() {
//        return OWL;
//    }
}
