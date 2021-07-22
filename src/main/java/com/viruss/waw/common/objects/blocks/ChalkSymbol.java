package com.viruss.waw.common.objects.blocks;

import com.viruss.waw.common.objects.items.Chalk;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.MobEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ChalkSymbol extends Block {
    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    public static final IntegerProperty SIGN = IntegerProperty.create("sign", 0, 19);

    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 0.02, 16);
    private final Chalk.Type type;
    public ChalkSymbol(Chalk.Type type) {
        super(AbstractBlock.Properties.of(Material.CLOTH_DECORATION).strength(0.3f).sound(SoundType.BASALT).noDrops().noCollission());
    this.type = type;
    }

    public int getColor() {
        return type.getColor();
    }

    @Override
    public boolean canBeReplaced(BlockState p_196253_1_, BlockItemUseContext p_196253_2_) {
        return true;
    }

    @Override
    public boolean canBeReplaced(BlockState p_225541_1_, Fluid p_225541_2_) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState()
                .setValue(FACING,context.getHorizontalDirection().getOpposite())
                .setValue(SIGN, RANDOM.nextInt(SIGN.getPossibleValues().size()))
        ;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(SIGN, BlockStateProperties.HORIZONTAL_FACING);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if((fromPos.above().getY() == pos.getY()|| fromPos.above().getY() == pos.getY()) &&  state.isAir())
            worldIn.removeBlock(pos, false);
    }

    @Nullable
    @Override
    public PathNodeType getAiPathNodeType(BlockState state, IBlockReader world, BlockPos pos, @Nullable MobEntity entity) {
        return PathNodeType.OPEN;
    }
}
