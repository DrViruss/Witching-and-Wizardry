package com.viruss.waw.common.tile;

import com.viruss.waw.common.rituals.Ritual;
import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.block.state.BlockState;

public class CentralSymbolTE extends NetworkTileEntity {

private Ritual ritual;

    final SimpleContainer inventory = new SimpleContainer(6);

    public CentralSymbolTE(BlockPos p_155229_, BlockState p_155230_) {
        super(ModRegistry.CHALKS.getCenterTE(), p_155229_, p_155230_);
    }

    @Override
    public void loadNetwork(CompoundTag tag) {

    }

    public void use() {
        System.out.println("USE!");
    }
}
