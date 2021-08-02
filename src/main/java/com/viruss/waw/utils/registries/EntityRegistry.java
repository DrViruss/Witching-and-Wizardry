package com.viruss.waw.utils.registries;

import com.viruss.waw.Main;
import com.viruss.waw.client.renderers.CustomBoatRenderer;
import com.viruss.waw.common.entities.CustomBoatEntity;
import com.viruss.waw.common.objects.packs.SignObject;
import com.viruss.waw.common.objects.packs.WoodenObject;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("all")
public class EntityRegistry {
    private final BlockEntityType<SignObject.AbstractSignTileEntity> SIGN_TILE;
    private final EntityType<CustomBoatEntity> BOAT;

    public EntityRegistry(WoodenObject... woods) {
        this.SIGN_TILE = registerSign(woods);
        this.BOAT = registerBoat();
    }

    private BlockEntityType<SignObject.AbstractSignTileEntity> registerSign(WoodenObject... woods)
    {
        Block[] blocks = new Block[woods.length*2];
        int j=0;
        for (WoodenObject wood : woods) {
            SignObject sign = wood.getSign();
            blocks[j] = sign.getSign();
            blocks[j + 1] = sign.getWallSign();
            j = j + 2;
        }
        BlockEntityType<SignObject.AbstractSignTileEntity> te = BlockEntityType.Builder.of(SignObject.AbstractSignTileEntity::new,blocks).build(null);
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

    public BlockEntityType<SignObject.AbstractSignTileEntity> getSignTileEntity() {
        return SIGN_TILE;
    }

    public EntityType<CustomBoatEntity> getBoatEntity() {
        return BOAT;
    }
}
