package com.viruss.waw.common.objects.blocks.bases;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class AbstractLog extends RotatedPillarBlock {
    private final int fireSpread;
    private final int flammability;

    public AbstractLog(int fireSpread, int flammability) {
        super(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).strength(2.0F).sound(SoundType.WOOD));
        this.fireSpread = fireSpread;
        this.flammability = flammability;
    }

    public AbstractLog() {
        super(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).strength(2.0F).sound(SoundType.WOOD));
        this.fireSpread = 5;
        this.flammability = 5;
    }

    public AbstractLog(Properties properties) {
        super(properties);
        this.fireSpread = 5;
        this.flammability = 5;
    }

    @Override
    public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return flammability;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return fireSpread;
    }
}
