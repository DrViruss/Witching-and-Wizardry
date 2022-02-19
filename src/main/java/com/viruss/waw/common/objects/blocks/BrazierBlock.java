package com.viruss.waw.common.objects.blocks;

import com.viruss.waw.common.tile.BrazierTE;
import com.viruss.waw.utils.ModUtils;
import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Random;

@SuppressWarnings("all")
public class BrazierBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 15, 14); //TODO: make brazier 1.25 block h

    public BrazierBlock() {
        super(Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(5F).sound(SoundType.STONE));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BrazierTE(pos,state);
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(LIT, false).setValue(WATERLOGGED,context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        if(fluidState.getType() == Fluids.WATER){
            level.setBlock(pos, state.setValue(WATERLOGGED, true),11);
            return true;
        }
        return SimpleWaterloggedBlock.super.placeLiquid(level, pos, state, fluidState);
    }

    @Override
    public BlockState updateShape(BlockState p_60541_, Direction p_60542_, BlockState p_60543_, LevelAccessor p_60544_, BlockPos p_60545_, BlockPos p_60546_) {
        if (p_60541_.getValue(WATERLOGGED)) {
            p_60544_.getLiquidTicks().scheduleTick(p_60545_, Fluids.WATER, Fluids.WATER.getTickDelay(p_60544_));
        }
        return super.updateShape(p_60541_, p_60542_, p_60543_, p_60544_, p_60545_, p_60546_);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!entity.fireImmune() && state.getValue(LIT) && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entity) && entity.blockPosition().getX() == pos.getX() && entity.blockPosition().getZ() == pos.getZ())
            entity.hurt(DamageSource.IN_FIRE, 1);
        super.entityInside(state, level, pos, entity);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(LIT,WATERLOGGED));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(!level.isClientSide()){
            BrazierTE tile = ((BrazierTE)level.getBlockEntity(pos));
            assert tile != null;
            ItemStack stack = player.getItemInHand(hand);
            if(stack.is(Items.FLINT_AND_STEEL)) {
                if (tile.tryLit(state, level, pos, (ServerPlayer) player))
                    ModUtils.Inventory.damageItem(player.isCreative(), stack);
            }
            else if(stack == ItemStack.EMPTY && state.getValue(LIT) && hand == InteractionHand.MAIN_HAND ) {
                level.setBlock(pos, state.setValue(LIT, false),11);
                player.hurt(DamageSource.ON_FIRE, 0.25F);
                tile.clearRecipe();
                level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.addParticle(ParticleTypes.SMOKE, pos.getX()+0.4, pos.getY()+0.3, pos.getZ()+0.5, 0.0D, 0.0D, 0.0D);
            }
            else {
                tile.use(state, level, pos, player, hand);
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
        if(state.getValue(LIT)){
            if (random.nextInt(10) == 0)
                level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 1.3, pos.getZ() + 0.5D, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.6F, false);
            level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 1.3, pos.getZ() + 0.5D, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.6F, false);

            double rand = random.nextDouble() * 6.0D / 16.0D;
            level.addParticle(ParticleTypes.SMOKE, pos.getX()+0.4+(rand/4), pos.getY()+rand+1, pos.getZ()+0.5+(rand/4), 0.0D, 0.0D, 0.0D);
            level.addParticle(ParticleTypes.FLAME, pos.getX()+0.4+(rand/4), pos.getY()+rand+0.95, pos.getZ()+0.5+(rand/4), 0.0D, 0.0D, 0.0D);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide() ? null : createTickerHelper(type, ModRegistry.GADGETS.getBrazierTE(), BrazierTE::tick);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        if(state.getValue(LIT)) return 10;
        return super.getLightEmission(state, world, pos);
    }

    @Override
    public void onProjectileHit(Level level, BlockState state, BlockHitResult hitResult, Projectile projectile) {
        BlockPos blockpos = hitResult.getBlockPos();
        Entity archer = projectile.getOwner();
        if (!level.isClientSide && projectile.isOnFire() && projectile.mayInteract(level, blockpos) && !state.getValue(LIT) && !state.getValue(WATERLOGGED))
            ((BrazierTE)level.getBlockEntity(blockpos)).tryLit(state,level,blockpos, archer instanceof Player ? (ServerPlayer) archer : null );

    }
}
