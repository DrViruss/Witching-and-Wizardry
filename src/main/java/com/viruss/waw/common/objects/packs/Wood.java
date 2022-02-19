package com.viruss.waw.common.objects.packs;

import com.viruss.waw.Main;
import com.viruss.waw.client.renders.CustomBoatRenderer;
import com.viruss.waw.common.entities.CustomBoatEntity;
import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.food.Foods;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ForgeRegistries;

public class Wood {
    private final BlockEntityType<SignPack.AbstractSignTileEntity> SIGN_TILE;
    private final EntityType<CustomBoatEntity> BOAT = registerBoat();

    public static final WoodenPack ASH = new WoodenPack("ash", Main.ITEM_GROUP,true, null,true);
    public static final WoodenPack SAMBUCUS = new WoodenPack("sambucus", Main.ITEM_GROUP,true, Foods.POISONOUS_POTATO,true);

    public Wood() {
        this.SIGN_TILE = registerSign(ASH,SAMBUCUS);
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

    public BlockEntityType<SignPack.AbstractSignTileEntity> getSignTileEntity() {
        return SIGN_TILE;
    }
    public EntityType<CustomBoatEntity> getBoatEntity() {
        return BOAT;
    }
}
