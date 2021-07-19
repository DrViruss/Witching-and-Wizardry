package com.viruss.waw.common.objects.blocks.bases;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class AbstractLeaves extends LeavesBlock {
    public AbstractLeaves() {
        super(AbstractBlock.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion());
    }

    public AbstractLeaves(Properties p_i48370_1_) {
        super(p_i48370_1_);
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return 60;
    }

    @Override
    public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return 30;
    }

    @Override
    public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos, EntitySpawnPlacementRegistry.PlacementType type, @Nullable EntityType<?> entityType) {
        if(entityType == EntityType.PARROT || entityType == EntityType.OCELOT)
            return true;
        return super.canCreatureSpawn(state, world, pos, type, entityType);
    }
}
