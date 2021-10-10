package com.viruss.waw.common.objects.blocks.chalk;

import com.viruss.waw.common.tile.CentralSymbolTE;
import com.viruss.waw.utils.ModUtils;
import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;

@SuppressWarnings("all")
public class CentralSymbol extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 2);
    public static final IntegerProperty COLOR = IntegerProperty.create("color", 0, 2);

    protected static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 0.02, 16);

    public CentralSymbol() {
        super(Properties.of(Material.CLOTH_DECORATION).strength(0.3f).sound(SoundType.BASALT).noDrops().noCollission());
        MinecraftForge.EVENT_BUS.register(this);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context, CompoundTag tag) {
        if(tag.contains("color"))
            return super.getStateForPlacement(context)
                    .setValue(STAGE, 0)
                    .setValue(FACING,context.getHorizontalDirection().getOpposite())
                    .setValue(COLOR, ModUtils.Colors.colorToProperty(tag.getInt("color")));
        return getStateForPlacement(context);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context)
                .setValue(STAGE, 0)
                .setValue(FACING,context.getHorizontalDirection().getOpposite())
                .setValue(COLOR, 0xFFFFF);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(STAGE,FACING,COLOR));
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
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(!level.isClientSide() && !player.isShiftKeyDown()) {
            if (player.getItemInHand(hand).is(ModRegistry.INGREDIENTS.getCalcosvis()) && blockState.getValue(STAGE) == 0) {
                level.setBlockAndUpdate(pos, blockState.setValue(STAGE, 1));
                ModUtils.Inventory.damageItem(player.isCreative(),player.getItemInHand(hand));
            }

            CentralSymbolTE center = (CentralSymbolTE) level.getBlockEntity(pos);
            center.use();
        }
        return super.use(blockState, level, pos, player, hand, hitResult);
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event){
        if(event.getEntity() instanceof Villager villager && villager.isBaby()) {
            Level world = villager.getCommandSenderWorld();
            BlockState state = world.getBlockState(villager.blockPosition());
            if(state.getBlock() instanceof CentralSymbol && state.getValue(STAGE) == 1)
                world.setBlockAndUpdate(villager.blockPosition(),state.setValue(STAGE,2));
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CentralSymbolTE(pos,state);
    }

    @Override
    public Item asItem() {
        return ModRegistry.CHALKS.getChalk();
    }
}
