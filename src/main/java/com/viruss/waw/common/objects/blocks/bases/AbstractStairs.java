package com.viruss.waw.common.objects.blocks.bases;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import java.util.function.Supplier;

public class AbstractStairs extends StairsBlock {
    public AbstractStairs() {
        super((Supplier<BlockState>) null, null);
    }

    public AbstractStairs(Block planks) {
        super(planks::defaultBlockState, AbstractBlock.Properties.copy(planks));
    }

    @Override
    public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return 20;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return 5;
    }
}
