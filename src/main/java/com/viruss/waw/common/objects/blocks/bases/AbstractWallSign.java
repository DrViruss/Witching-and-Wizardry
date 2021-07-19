package com.viruss.waw.common.objects.blocks.bases;

import com.viruss.waw.common.objects.blocks.SignObject;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class AbstractWallSign extends WallSignBlock {
    private final SignObject signObject;

    public AbstractWallSign(WoodType p_i225766_2_,SignObject signObject) {
        super(AbstractBlock.Properties.of(Material.WOOD).noCollission().strength(1.0F).sound(SoundType.WOOD).lootFrom(signObject::getSign), p_i225766_2_);
        this.signObject = signObject;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AbstractSignTileEntity(signObject.getTile());
    }
}
