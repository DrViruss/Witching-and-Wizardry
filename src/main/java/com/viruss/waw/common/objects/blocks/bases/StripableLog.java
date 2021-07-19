package com.viruss.waw.common.objects.blocks.bases;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class StripableLog extends AbstractLog{
    Block stripped;

    public StripableLog(Block stripped) {
        super(Properties.copy(stripped));
        this.stripped = stripped;
    }

    @Nullable
    @Override
    public BlockState getToolModifiedState(BlockState state, World world, BlockPos pos, PlayerEntity player, ItemStack stack, ToolType toolType) {
//        if (toolType == ToolType.AXE)
            return stripped.defaultBlockState().setValue(AXIS, state.getValue(AXIS));
    }
}
