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
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player playerEntity, InteractionHand hand, BlockHitResult traceResult) {
        if(!world.isClientSide()){
            ItemStack item = playerEntity.getItemInHand(hand).copy();
            item.setCount(1);
            MortarTE te = (MortarTE) world.getBlockEntity(pos);
            te.getInventory().ifPresent(itemStackHandler -> {
                if(playerEntity.isShiftKeyDown() && item == ItemStack.EMPTY)
                    for (int i = itemStackHandler.getSlots()-1; i >=0 ; i--) {
                        ItemStack extracted = itemStackHandler.extractItem(i, 1, false);
                        if (extracted != ItemStack.EMPTY) {
                            playerEntity.addItem(extracted);
                            world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 3f,3f);
                            return;
                        }
                    }

                if(item == ItemStack.EMPTY)
                    return;

                if(itemStackHandler.getStackInSlot(itemStackHandler.getSlots()-1) != ItemStack.EMPTY && item.getItem() == ModRegistry.MORTAR_AND_PESTLE.getPestle()) {
                    System.out.println("Crafting!!");
                    world.playSound(null, pos, SoundEvents.STONE_HIT, SoundSource.PLAYERS, 3f,3f);
                    return;
                }

                for(int i=0; i<itemStackHandler.getSlots();i++) {
                    if(itemStackHandler.getStackInSlot(i).getItem() == ModRegistry.MORTAR_AND_PESTLE.getPestle())
                        break;
                    if (itemStackHandler.getStackInSlot(i) == ItemStack.EMPTY) {
                        itemStackHandler.insertItem(i, item,false);
                        playerEntity.getItemInHand(hand).shrink(1);
                        world.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.PLAYERS, 0.5f,3f);
                        return;
                    }
                }
            });
        }
        return InteractionResult.SUCCESS;
    }



    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState p_196243_4_, boolean p_196243_5_) {
        if(!world.isClientSide())
        {
            ((MortarTE)world.getBlockEntity(pos)).getInventory().ifPresent(itemStackHandler -> {
                for (int i = 0; i < itemStackHandler.getSlots(); i++) {
                    Containers.dropItemStack(world, pos.getX(), pos.getY() + 0.5, pos.getZ(), itemStackHandler.getStackInSlot(i));
                }
            });
        }
        super.onRemove(state, world, pos, p_196243_4_, p_196243_5_);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MortarTE(pos,state);
    }
}
