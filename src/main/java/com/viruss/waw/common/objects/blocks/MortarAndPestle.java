package com.viruss.waw.common.objects.blocks;

import com.viruss.waw.WitchingAndWizardry;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.state.DirectionProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class MortarAndPestle extends ContainerBlock {
    //TODO: fix this sht!

    private static final VoxelShape SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 7.0D, 13.0D);
    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    private static final ITextComponent CONTAINER_NAME = new TranslationTextComponent("container." + WitchingAndWizardry.MOD_ID + ".map");

    public MortarAndPestle()
    {
        super(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.STONE));
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE;
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
        return null;
    }
}
