package com.viruss.waw.common.objects.blocks;

import com.viruss.waw.common.tile.MortarTE;
import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

@SuppressWarnings("all")
public class MortarBlock extends BaseEntityBlock {
    private static final VoxelShape SHAPE = Block.box(4, 1, 4, 12, 6, 12);

    public MortarBlock() {
        super(Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.STONE));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player playerEntity, InteractionHand hand, BlockHitResult traceResult) {
        if(!world.isClientSide()){
            ItemStack item = playerEntity.getItemInHand(hand).copy();
            item.setCount(1);
            MortarTE te = (MortarTE) world.getBlockEntity(pos);

            if(playerEntity.isShiftKeyDown() && item == ItemStack.EMPTY)
                for (int i = te.getInventory().getContainerSize()-1; i >=0 ; i--) {
                    ItemStack extracted = te.getInventory().removeItem(i, 1);
                    if (extracted != ItemStack.EMPTY) {
                        playerEntity.addItem(extracted);
                        world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 3f,3f);
                        return InteractionResult.SUCCESS;
                    }
                }

                if(item == ItemStack.EMPTY)
                    return InteractionResult.PASS;

                if(item.getItem() == ModRegistry.GADGETS.getPestle()) {
                    te.craft(playerEntity);
                    world.playSound(null, pos, SoundEvents.STONE_HIT, SoundSource.PLAYERS, 3f,3f);
                    return InteractionResult.SUCCESS;
                }
            if(te.getInventory().addItem(item).isEmpty())
                playerEntity.getItemInHand(hand).shrink(1);
            world.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.PLAYERS, 0.5f,3f);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState p_196243_4_, boolean p_196243_5_) {
        if(!world.isClientSide())
            Containers.dropContents(world,pos,((MortarTE) world.getBlockEntity(pos)).getInventory());
        super.onRemove(state, world, pos, p_196243_4_, p_196243_5_);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MortarTE(pos,state);
    }
}
