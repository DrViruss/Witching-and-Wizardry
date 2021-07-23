package com.viruss.waw.common.objects.blocks;

import com.viruss.waw.utils.RegistryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Predicate;

public class  BoatObject { /*TODO: Make boat more flexible!*/
    private final EntityType<BoatEntity> entityType;
    private final Item item;

    public BoatObject(String name, ItemGroup group) {
        this.entityType = EntityType.Builder.<BoatEntity>of(AbstractBoatEntity::new, EntityClassification.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10).build(name);
        this.item = new AbstractBoatItem(entityType, group);

        RegistryHandler.MDR.register(name, () -> item, ForgeRegistries.ITEMS);
        RegistryHandler.MDR.register(name, () -> this.entityType, ForgeRegistries.ENTITIES);
    }

    public EntityType<BoatEntity> getType() {
        return entityType;
    }

    public Item getItem() {
        return item;
    }


    public static class AbstractBoatItem extends Item {
        private static final Predicate<Entity> ENTITY_PREDICATE = EntityPredicates.NO_SPECTATORS.and(Entity::isPickable);
        EntityType<? extends BoatEntity> type;

        public AbstractBoatItem(EntityType<? extends BoatEntity> type, ItemGroup group) {
            super(new Item.Properties().stacksTo(1).tab(group));
            this.type = type;
        }

        @Override
        public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
            ItemStack itemstack = playerIn.getItemInHand(handIn);
            RayTraceResult raytraceresult = getPlayerPOVHitResult(worldIn, playerIn, RayTraceContext.FluidMode.ANY);
            if (raytraceresult.getType() == RayTraceResult.Type.MISS) {
                return ActionResult.pass(itemstack);
            } else {
                Vector3d vector3d = playerIn.getViewVector(1.0F);
                List<Entity> list = worldIn.getEntities(playerIn, playerIn.getBoundingBox().expandTowards(vector3d.scale(5.0D)).inflate(1.0D), ENTITY_PREDICATE);
                if (!list.isEmpty()) {
                    Vector3d vector3d1 = playerIn.getEyePosition(1.0F);

                    for (Entity entity : list) {
                        AxisAlignedBB axisalignedbb = entity.getBoundingBox().inflate(entity.getPickRadius());
                        if (axisalignedbb.contains(vector3d1)) {
                            return ActionResult.pass(itemstack);
                        }
                    }
                }

                if (raytraceresult.getType() == RayTraceResult.Type.BLOCK) {
                    BoatEntity boat = new AbstractBoatEntity(type, worldIn, raytraceresult.getLocation().x, raytraceresult.getLocation().y, raytraceresult.getLocation().z); //
                    boat.yRot = playerIn.yRot;
                    if (!worldIn.noCollision(boat, boat.getBoundingBox().inflate(-0.1D))) {
                        return ActionResult.fail(itemstack);
                    } else {
                        if (!worldIn.isClientSide) {
                            worldIn.addFreshEntity(boat);
                            if (!playerIn.abilities.instabuild) {
                                itemstack.shrink(1);
                            }
                        }

                        playerIn.awardStat(Stats.ITEM_USED.get(this));
                        return ActionResult.sidedSuccess(itemstack, worldIn.isClientSide());
                    }
                } else {
                    return ActionResult.pass(itemstack);
                }
            }
        }
    }

    /*~  ~Additional Classes~  ~*/

    public static class AbstractBoatEntity extends BoatEntity {
        public AbstractBoatEntity(EntityType<? extends BoatEntity> p_i50129_1_, World p_i50129_2_) {
            super(p_i50129_1_, p_i50129_2_);
        }

        public AbstractBoatEntity(EntityType<? extends BoatEntity> type, World worldIn, double x, double y, double z) {
            super(type, worldIn);
            this.setPos(x, y, z);
            this.setDeltaMovement(Vector3d.ZERO);
            this.xo = x;
            this.yo = y;
            this.zo = z;
        }

        public AbstractBoatEntity(World worldIn, double x, double y, double z) {
            super(worldIn, x, y, z);
        }

//        @Override
//        public Item getDropItem() {
//            return RegistryHandler.ASH.getBoat().getItem();
//        }

        @Override
        public IPacket<?> getAddEntityPacket() {
            return NetworkHooks.getEntitySpawningPacket(this);
        }
    }
}
