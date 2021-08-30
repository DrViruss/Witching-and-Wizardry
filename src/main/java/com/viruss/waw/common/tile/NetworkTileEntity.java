package com.viruss.waw.common.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

@SuppressWarnings("all")
public abstract class NetworkTileEntity extends BlockEntity {

    public NetworkTileEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    @Override
    public void load(CompoundTag nbt) {
        this.loadNetwork(nbt);
        super.load(nbt);
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        this.saveNetwork(nbt);
        return super.save(nbt);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.getBlockPos(), 1, this.saveNetwork(new CompoundTag()));
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.loadNetwork(pkt.getTag());
        super.onDataPacket(net, pkt);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveNetwork(super.getUpdateTag());
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        this.loadNetwork(tag);
    }

    public abstract void loadNetwork(CompoundTag tag);

    public CompoundTag saveNetwork(CompoundTag tag){
        return tag;
    }

    public void updateNetwork() {
        if(level!=null && !level.isClientSide())
            this.level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),2);
    }
}