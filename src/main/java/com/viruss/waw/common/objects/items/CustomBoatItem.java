package com.viruss.waw.common.objects.items;

import com.viruss.waw.Main;
import com.viruss.waw.common.entities.CustomBoatEntity;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@SuppressWarnings("all")
public class CustomBoatItem extends Item {
    private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);

    public CustomBoatItem() {
        super(new Item.Properties().stacksTo(1).tab(Main.ITEM_GROUP));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        HitResult raytraceresult = getPlayerPOVHitResult(worldIn, playerIn, ClipContext.Fluid.ANY);
        if (raytraceresult.getType() == HitResult.Type.MISS)
            return InteractionResultHolder.pass(itemstack);
        else {
            Vec3 vector3d = playerIn.getViewVector(1.0F);
            List<Entity> list = worldIn.getEntities(playerIn, playerIn.getBoundingBox().expandTowards(vector3d.scale(5.0D)).inflate(1.0D), ENTITY_PREDICATE);
            if (!list.isEmpty()) {
                Vec3 vector3d1 = playerIn.getEyePosition(1.0F);

                for (Entity entity : list) {
                    AABB axisalignedbb = entity.getBoundingBox().inflate(entity.getPickRadius());
                    if (axisalignedbb.contains(vector3d1))
                        return InteractionResultHolder.pass(itemstack);
                }
            }

            if (raytraceresult.getType() == HitResult.Type.BLOCK) {
                CustomBoatEntity boat = new CustomBoatEntity(this,worldIn, raytraceresult.getLocation().x, raytraceresult.getLocation().y, raytraceresult.getLocation().z);
                boat.setYRot(playerIn.getYRot());
                boat.setDataType(Objects.requireNonNull(getRegistryName()).getPath().split("_")[0]);

                if (!worldIn.noCollision(boat, boat.getBoundingBox().inflate(-0.1D)))
                    return InteractionResultHolder.fail(itemstack);
                else {
                    if (!worldIn.isClientSide) {
                        worldIn.addFreshEntity(boat);
                        if (!playerIn.getAbilities().instabuild)
                            itemstack.shrink(1);
                    }

                    playerIn.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResultHolder.sidedSuccess(itemstack, worldIn.isClientSide());
                }
            } else
                return InteractionResultHolder.pass(itemstack);
        }
    }
}