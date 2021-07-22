package com.viruss.waw.common.tile;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nullable;

public class NetworkTileEntity extends TileEntity {
    public NetworkTileEntity(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        this.loadNetwork(nbt);
        super.load(state, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        this.saveNetwork(nbt);
        return super.save(nbt);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getBlockPos(), 1, this.saveNetwork(new CompoundNBT()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.loadNetwork(pkt.getTag());
        super.onDataPacket(net, pkt);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.saveNetwork(super.getUpdateTag());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        super.handleUpdateTag(state, tag);
        this.loadNetwork(tag);
    }

    public void loadNetwork(CompoundNBT tag){
    }

    public CompoundNBT saveNetwork(CompoundNBT tag){
        return tag;
    }

    public void updateNetwork()
    {
        if(this.level!=null)
        {
            this.level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),2);
        }
    }
}
