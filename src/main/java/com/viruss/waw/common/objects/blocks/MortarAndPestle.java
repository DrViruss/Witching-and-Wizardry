package com.viruss.waw.common.objects.blocks;

import com.viruss.waw.WitchingAndWizardry;
import com.viruss.waw.client.renders.MortarRenderer;
import com.viruss.waw.common.tile.MortarTE;
import com.viruss.waw.utils.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class MortarAndPestle {
    private final Block mortar;
    private final Item pestle;
    private final TileEntityType<MortarTE> mortarTE;

    public MortarAndPestle() {
        this.mortar = new Mortar();
        this.pestle = new Item(new Item.Properties().tab(WitchingAndWizardry.ITEM_GROUP).stacksTo(1));
        this.mortarTE = TileEntityType.Builder.of(MortarTE::new,mortar).build(null);


        RegistryHandler.MDR.register("mortar",new com.viruss.waw.utils.registration.Block.Builder().setBlockSup(() -> mortar).needItem(WitchingAndWizardry.ITEM_GROUP));
        RegistryHandler.MDR.register("pestle",()->pestle, ForgeRegistries.ITEMS);
        RegistryHandler.MDR.register("mortar",()->mortarTE,ForgeRegistries.TILE_ENTITIES);
        WitchingAndWizardry.CLIENT_RENDERER.addTileEntityRenderer(mortarTE, MortarRenderer::new);
    }

    public TileEntityType<MortarTE> getMortarTE() {
        return mortarTE;
    }

    public Block getMortar() {
        return mortar;
    }

    public Item getPestle() {
        return pestle;
    }

    public static class Mortar extends Block {
        private static final VoxelShape SHAPE = Block.box(4, 1, 4, 12, 6, 12);

        public Mortar() {
            super(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.STONE));
        }

        @Override
        public VoxelShape getShape(BlockState state, IBlockReader iBlockReader, BlockPos pos, ISelectionContext context) {
            return SHAPE;
        }

        @Override
        public boolean hasTileEntity(BlockState state) {
            return true;
        }

        @Nullable
        @Override
        public TileEntity createTileEntity(BlockState state, IBlockReader world) {
            return new MortarTE();
        }

        @Override
        public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult traceResult) {
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
                                world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundCategory.PLAYERS, 3f,3f);
                                return;
                            }
                        }

                    if(item == ItemStack.EMPTY)
                        return;

                    if(itemStackHandler.getStackInSlot(itemStackHandler.getSlots()-1) != ItemStack.EMPTY && item.getItem() == RegistryHandler.MORTAR_AND_PESTLE.pestle) {
                        System.out.println("Crafting!!");
                        world.playSound(null, pos, SoundEvents.STONE_HIT, SoundCategory.PLAYERS, 3f,3f);
                        return;
                    }

                    for(int i=0; i<itemStackHandler.getSlots();i++) {
                        if(itemStackHandler.getStackInSlot(i).getItem() == RegistryHandler.MORTAR_AND_PESTLE.pestle)
                            break;
                        if (itemStackHandler.getStackInSlot(i) == ItemStack.EMPTY) {
                            itemStackHandler.insertItem(i, item,false);
                            playerEntity.getItemInHand(hand).shrink(1);
                            world.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundCategory.PLAYERS, 0.5f,3f);
                            return;
                        }
                    }
                });
            }
            return ActionResultType.SUCCESS;
        }

        @Override
        public void onRemove(BlockState state, World world, BlockPos pos, BlockState p_196243_4_, boolean p_196243_5_) {
            if(!world.isClientSide())
            {
                ((MortarTE)world.getBlockEntity(pos)).getInventory().ifPresent(itemStackHandler -> {
                    for (int i = 0; i < itemStackHandler.getSlots(); i++) {
                        InventoryHelper.dropItemStack(world, pos.getX(), pos.getY() + 0.5, pos.getZ(), itemStackHandler.getStackInSlot(i));
                    }
                });
            }
            super.onRemove(state, world, pos, p_196243_4_, p_196243_5_);
        }
    }
}
