package com.viruss.waw.common.objects.items;

import com.viruss.waw.WitchingAndWizardry;
import com.viruss.waw.utils.ModRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

public class Chalk extends Item {   /*~ Thanks OccultismMod ~*/
    private final Type type;
    public Chalk(Type type) {
        super(new Item.Properties().setNoRepair().defaultDurability(128).tab(WitchingAndWizardry.ITEM_GROUP));
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public int getColor()
{
    return this.type.getColor();
}

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);
        PlayerEntity player = context.getPlayer();
        boolean isReplacing = world.getBlockState(pos).getBlock().canBeReplaced(state, new BlockItemUseContext(context));
        if (!world.isClientSide()) {
            Block symbol  = ModRegistry.CHALKS.getChalk(type).getSymbol();
            if ((context.getClickedFace() == Direction.UP && symbol.isAir(world.getBlockState(pos.above()), world, pos.above())) || isReplacing) {
                ItemStack heldChalk = context.getItemInHand();
                BlockPos placeAt = isReplacing ? pos : pos.above();
                world.setBlockAndUpdate(placeAt, Objects.requireNonNull(symbol.getStateForPlacement(new BlockItemUseContext(context))));

                world.playSound(null, pos, ModRegistry.CHALKS.getSound(), SoundCategory.PLAYERS, 3f,3f);

                if (!player.isCreative()) {
                    heldChalk.setDamageValue(heldChalk.getDamageValue() + 1);

                    if (heldChalk.getDamageValue() >= heldChalk.getMaxDamage())
                        heldChalk.shrink(1);
                }

            }
        }
        return ActionResultType.SUCCESS;
    }

    public static class Type{
        public static final Type WHITE = new Type("white",0xffffff);
        public static final Type RED = new Type("red",0xd70f0f);
        private final int color;
        private final String name;

        private Type(String name, int color) {
            this.name = name;
            this.color = color;
        }

        public int getColor() {
            return color;
        }

        public String getName() {
            return name;
        }
    }
}
