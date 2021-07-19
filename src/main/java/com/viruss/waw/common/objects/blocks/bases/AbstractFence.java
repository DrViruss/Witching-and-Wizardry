package com.viruss.waw.common.objects.blocks.bases;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class AbstractFence extends FenceBlock {
    public AbstractFence(MaterialColor color) {
        super(AbstractBlock.Properties.of(Material.WOOD, color).strength(2.0F, 3.0F).sound(SoundType.WOOD));
    }

    @Override
    public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return 20;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return 5;
    }

    @Override
    public boolean connectsTo(BlockState blockState, boolean b, Direction direction) {
        Block block = blockState.getBlock();
        return !isExceptionForConnection(block) && b || block instanceof FenceBlock || block instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(blockState, direction);
    }
}
