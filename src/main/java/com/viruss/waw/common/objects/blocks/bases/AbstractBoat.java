package com.viruss.waw.common.objects.blocks.bases;

import com.viruss.waw.utils.RegistryHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class AbstractBoat extends BoatEntity {
    public AbstractBoat(EntityType<? extends BoatEntity> p_i50129_1_, World p_i50129_2_) {
        super(p_i50129_1_, p_i50129_2_);
    }

    public AbstractBoat(EntityType<? extends BoatEntity> type,World worldIn, double x, double y, double z) {
        super(type, worldIn);
        this.setPos(x, y, z);
        this.setDeltaMovement(Vector3d.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    public AbstractBoat(World worldIn, double x, double y, double z) {
        super(worldIn,x,y,z);
    }

    @Override
    public Item getDropItem() {
        return RegistryHandler.ASH.getBoat().getItem();
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
