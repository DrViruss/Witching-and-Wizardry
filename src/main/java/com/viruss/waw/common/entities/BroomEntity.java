package com.viruss.waw.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PlayerRideable;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class BroomEntity extends Entity implements PlayerRideable {
    private UUID OWNER_ID;
    private boolean isMagic;

    int damage=0;

    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYaw;
    private double lerpPitch;

    public BroomEntity(EntityType<? extends Entity> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH,1).add(Attributes.FLYING_SPEED, 0.4F).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }



    //    @Override
//    protected void readAdditionalSaveData(CompoundTag tag) {
//        this.isMagic = tag.getBoolean("isMagic");
//    }

//    @Override
//    protected void addAdditionalSaveData(CompoundTag tag) {
//        tag.putBoolean("isMagic",this.isMagic);
//    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


//    @Override
//    public void tick() {
//        super.tick();
//        if (this.isControlledByLocalInstance()) {
//            lerpSteps = 0;
//            updateTrackedPosition(getX(), getY(), getZ());
//        }
//        if (lerpSteps > 0) {
//            updatePosition(getX() + (lerpX - getX()) / lerpSteps, getY() + (lerpY - getY()) / lerpSteps, getZ() + (lerpZ - getZ()) / lerpSteps);
//            setRotation((float) (getYaw() + MathHelper.wrapDegrees(lerpYaw - getYaw()) / lerpSteps), (float) (getPitch() + (lerpPitch - getPitch()) / lerpSteps));
//            --lerpSteps;
//        }
//        if (isControlledByLocalInstance()) {
//            updateTrackedPosition(getX(), getY(), getZ());
//            Entity passenger = getPrimaryPassenger();
//            if (passenger instanceof PlayerEntity player) {
//                this.setRot(passenger.getXRot(), passenger.getYRot());
//                if (BroomUserComponent.get(player).isPressingForward()) {
//                    addVelocity(passenger.getRotationVector().x / 8 * getSpeed(), passenger.getRotationVector().y / 8 * getSpeed(), passenger.getRotationVector().z / 8 * getSpeed());
//                    setVelocity(MathHelper.clamp(getVelocity().x, -getSpeed(), getSpeed()), MathHelper.clamp(getVelocity().y, -getSpeed(), getSpeed()), MathHelper.clamp(getVelocity().z, -getSpeed(), getSpeed()));
//                }
//            }
//            move(MovementType.SELF, getVelocity());
//            setVelocity(getVelocity().multiply(0.9));
//        }
//        else {
//            setVelocity(Vec3d.ZERO);
//        }
//        if (!level.isClientSide() && damage > 0) {
//            damage -= 1 / 20f;
//            damage = Math.max(damage, 0);
//        }
//    }


    @Override
    public boolean canBeRiddenInWater(Entity rider) {
        return true;
    }

    @Override
    public boolean isAttackable() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (isInvulnerableTo(source)) {
            return false;
        }
        if (!level.isClientSide() && !isRemoved()) {
            System.out.println("HURT");
            damage += amount;
            if (damage > 4) {
                spawnAtLocation(getDroppedStack());
                remove(RemovalReason.DISCARDED);
            }
        }
        return true;
    }

    @Nullable
    @Override
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return getControllingPassenger() == null;
    }

    @Override
    public boolean canCollideWith(Entity other) {
        return Boat.canVehicleCollide(this, other);
    }

    @Override
    public boolean canBeCollidedWith() {
        return !isRemoved();
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_20052_) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_20139_) {

    }


    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void checkFallDamage(double p_19911_, boolean p_19912_, BlockState p_19913_, BlockPos p_19914_) {
    }

    @Override
    public boolean causeFallDamage(float p_146828_, float p_146829_, DamageSource p_146830_) {
        return false;
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
        lerpX = x;
        lerpY = y;
        lerpZ = z;
        yaw = yaw;
        pitch = pitch;
        lerpSteps = 10;
    }

    protected ItemStack getDroppedStack() {
        return new ItemStack(Items.STICK);
    }
}
