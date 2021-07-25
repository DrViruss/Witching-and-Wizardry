package com.viruss.waw.utils.registries;

import com.viruss.waw.WitchingAndWizardry;
import com.viruss.waw.client.renders.CustomBoatRenderer;
import com.viruss.waw.common.entities.CustomBoatEntity;
import com.viruss.waw.common.objects.packs.SignObject;
import com.viruss.waw.common.objects.packs.WoodenObject;
import com.viruss.waw.utils.ModRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.SignTileEntityRenderer;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntities {

    private final TileEntityType<SignObject.AbstractSignTileEntity> SIGN_TILE;
    private final EntityType<CustomBoatEntity> BOAT;

    public ModEntities(WoodenObject... woods) {
        this.SIGN_TILE = registerSign(woods);
        this.BOAT = registerBoat();
    }

    private TileEntityType<SignObject.AbstractSignTileEntity> registerSign(WoodenObject... woods)
    {
        Block[] blocks = new Block[woods.length*2];
        int j=0;
        for (int i =0; i<woods.length;i++)
        {
            SignObject sign =woods[i].getSign();
            blocks[j] = sign.getSign();
            blocks[j+1] = sign.getWallSign();
            j=j+2;
        }
        TileEntityType<SignObject.AbstractSignTileEntity> te = TileEntityType.Builder.of(SignObject.AbstractSignTileEntity::new,blocks).build(null);
        ModRegistry.MDR.register("sign_te",()->te, ForgeRegistries.TILE_ENTITIES);
        WitchingAndWizardry.CLIENT_RENDERER.addTileEntityRenderer(te, SignTileEntityRenderer::new);

        return te;
    }

    private EntityType<CustomBoatEntity> registerBoat()
    {
        EntityType<CustomBoatEntity> te = EntityType.Builder.<CustomBoatEntity>of(CustomBoatEntity::new, EntityClassification.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10).build("boat");
        ModRegistry.MDR.register("boat",()->te, ForgeRegistries.ENTITIES);
        WitchingAndWizardry.CLIENT_RENDERER.addEntityRender(te, CustomBoatRenderer::new);
        return te;
    }


    public TileEntityType<SignObject.AbstractSignTileEntity> getSignTileEntity() {
        return SIGN_TILE;
    }

    public EntityType<CustomBoatEntity> getBoatEntity() {
        return BOAT;
    }
}
