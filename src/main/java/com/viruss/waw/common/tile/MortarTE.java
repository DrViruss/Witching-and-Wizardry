package com.viruss.waw.common.tile;

import com.viruss.waw.utils.RegistryHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MortarTE extends NetworkTileEntity  {

    LazyOptional<ItemStackHandler> inventory = LazyOptional.of(() -> new ItemStackHandler(5){
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            MortarTE.this.updateNetwork();
        }
    });

    public MortarTE() {
        super(RegistryHandler.MORTAR_AND_PESTLE.getMortarTE());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && side == Direction.UP)
            return inventory.cast();
        return super.getCapability(cap, side);
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        this.inventory.invalidate();
    }

    @Override
    public void loadNetwork(CompoundNBT tag) {
        super.loadNetwork(tag);
        this.inventory.ifPresent((handler) -> handler.deserializeNBT(tag.getCompound("inventory")));
    }

    @Override
    public CompoundNBT saveNetwork(CompoundNBT tag) {
        this.inventory.ifPresent(handler -> tag.put("inventory", handler.serializeNBT()));
        return super.saveNetwork(tag);
    }

    public LazyOptional<ItemStackHandler> getInventory() {
        return inventory;
    }
}