package com.viruss.waw.common.objects.blocks;

import com.viruss.waw.WitchingAndWizardry;
import com.viruss.waw.common.objects.items.Chalk;
import com.viruss.waw.utils.RegistryHandler;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class ChalkSymbol extends Block {
    public static final IntegerProperty SIGN = IntegerProperty.create("sign", 0, 2);
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 15, 0.04, 15);
    private final Chalk.Type type;
    public ChalkSymbol(Chalk.Type type) {
        super(AbstractBlock.Properties.of(Material.CLOTH_DECORATION).strength(0.3f).sound(SoundType.SAND).noDrops().noCollission());
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
        return this.defaultBlockState().setValue(SIGN, RANDOM.nextInt(SIGN.getPossibleValues().size()));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(SIGN, BlockStateProperties.HORIZONTAL_FACING);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if(fromPos.above().getY() == pos.getY()|| fromPos.above().getY() == pos.getY())
            worldIn.removeBlock(pos, false);
    }


    @Override /*???????*/
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        if(ForgeRegistries.ITEMS.containsKey(new ResourceLocation(WitchingAndWizardry.MOD_ID,"chalk_"+type.getName())))
            return new ItemStack(RegistryHandler.CHALKS.getChalk(type).getChalk());
        return ItemStack.EMPTY;
    }

    @Nullable
    @Override
    public PathNodeType getAiPathNodeType(BlockState state, IBlockReader world, BlockPos pos, @Nullable MobEntity entity) {
        return PathNodeType.OPEN;
    }
}
