package com.viruss.waw.common.objects.blocks.chalk;

import com.viruss.waw.common.objects.packs.ChalkSet;
import com.viruss.waw.utils.ModUtils;
import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

@SuppressWarnings("all")
public class BasicSymbol extends Block {
    public static final IntegerProperty SIGN = IntegerProperty.create("sign", 0, 19);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final IntegerProperty COLOR = IntegerProperty.create("color", 0, ChalkSet.COLORS.length-1);

    protected static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 0.02, 16);

    public BasicSymbol() {
        super(Properties.of(Material.CLOTH_DECORATION).strength(0.3f).sound(SoundType.BASALT).noDrops().noCollission());
    }

    public BlockState getStateForPlacement(BlockPlaceContext context, CompoundTag tag) {
        if(tag.contains("color"))
            return super.getStateForPlacement(context)
                .setValue(SIGN, RANDOM.nextInt(SIGN.getPossibleValues().size()))
                .setValue(FACING,context.getHorizontalDirection().getOpposite())
                .setValue(COLOR, ModUtils.Colors.colorToProperty(tag.getInt("color")));
        return getStateForPlacement(context);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context)
            .setValue(SIGN, RANDOM.nextInt(SIGN.getPossibleValues().size()))
            .setValue(FACING,context.getHorizontalDirection().getOpposite())
            .setValue(COLOR, 0);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(SIGN,COLOR,FACING));
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Override
    public boolean canBeReplaced(BlockState p_196253_1_, BlockPlaceContext p_196253_2_) {
        return true;
    }

    @Override
    public boolean canBeReplaced(BlockState p_225541_1_, Fluid p_225541_2_) {
        return true;
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if((fromPos.above().getY() == pos.getY()|| fromPos.above().getY() == pos.getY()) &&  state.isAir())
            worldIn.removeBlock(pos, false);
    }

    @Nullable
    @Override
    public BlockPathTypes getAiPathNodeType(BlockState state, BlockGetter world, BlockPos pos, @Nullable Mob entity) {
        return BlockPathTypes.OPEN;
    }

    @Override
    public boolean canCreatureSpawn(BlockState state, BlockGetter world, BlockPos pos, SpawnPlacements.Type type, EntityType<?> entityType) {
        return true;
    }

    @Override
    public Item asItem() {
        return ModRegistry.CHALKS.getChalk();
    }
}
