package com.viruss.waw.common.entities;

import com.viruss.waw.utils.ModRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class CustomBoatEntity extends BoatEntity {
    private static final DataParameter<String> DATA_TYPE = EntityDataManager.defineId(BoatEntity.class, DataSerializers.STRING);

    private Item boatItem;

    public CustomBoatEntity(EntityType<? extends BoatEntity> p_i50129_1_, World p_i50129_2_) {
        super(p_i50129_1_, p_i50129_2_);
    }

    public CustomBoatEntity(Item drop,EntityType<? extends BoatEntity> p_i50129_1_, World p_i50129_2_) {
        this(p_i50129_1_, p_i50129_2_);
        this.boatItem = drop;
    }

    public CustomBoatEntity(Item drop,World worldIn, double x, double y, double z) {
        this(drop,ModRegistry.ENTITIES.getBoatEntity(), worldIn);
        this.setPos(x, y, z);
        this.setDeltaMovement(Vector3d.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_TYPE, "pattern");
    }

    public String getDataType() {
        return entityData.get(DATA_TYPE);
    }

    public void setDataType(String type) {
        entityData.set(DATA_TYPE,type);
    }

    @Override
    public Item getDropItem() {
        Item drop = boatItem;
        if(drop== null)
            drop = super.getDropItem();
        return drop;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
