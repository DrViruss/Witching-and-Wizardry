package com.viruss.waw.common.objects.items;

import com.viruss.waw.Main;
import com.viruss.waw.common.objects.blocks.chalk.BasicSymbol;
import com.viruss.waw.common.objects.blocks.chalk.CentralSymbol;
import com.viruss.waw.common.objects.packs.ChalkSet;
import com.viruss.waw.utils.ModUtils;
import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

@SuppressWarnings("all")
public class Chalk extends Item {
    public Chalk() {
        super(new Item.Properties().setNoRepair().defaultDurability(128).tab(Main.ITEM_GROUP));
    }
    public Chalk(int durability) {
        super(new Item.Properties().setNoRepair().defaultDurability(durability).tab(Main.ITEM_GROUP));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltips, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltips, flag);
//        CompoundTag tag = stack.getTag();
//        if(tag != null && tag.contains("color"))
//            tooltips.add(new TranslatableComponent("waw.tooltip.?????").append(" "));
    }



    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        if(!world.isClientSide()) {
            BlockPos pos = context.getClickedPos();
            BlockState state = world.getBlockState(pos);
            Player player = context.getPlayer();
            BlockPlaceContext placeContext = new BlockPlaceContext(context);
            boolean isReplacing = world.getBlockState(pos).getBlock().canBeReplaced(state, placeContext);
            if ((context.getClickedFace() == Direction.UP && world.getBlockState(pos.above()).isAir()) || isReplacing) {
                BlockState newState = player.isShiftKeyDown() && context.getItemInHand().getTag().contains("color") && context.getItemInHand().getTag().getInt("color") == Color.LIGHT_GRAY.getRGB()  ? //TODO: think how to make it better
                    ((CentralSymbol) ModRegistry.CHALKS.getCentralBlock()).getStateForPlacement(placeContext, context.getItemInHand().getTag())
                        :
                    ((BasicSymbol) ModRegistry.CHALKS.getBasicBlock()).getStateForPlacement(placeContext, context.getItemInHand().getTag());


                world.setBlockAndUpdate(isReplacing ? pos : pos.above(), newState);
                world.playSound(null, pos, ModRegistry.CHALKS.getSound(), SoundSource.PLAYERS, 3f, 3f);

                ModUtils.Inventory.damageItem(player.isCreative(), context.getItemInHand());

                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(context);
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> stacks) {
        if (!this.allowdedIn(tab)) return;

        for(int color : ChalkSet.COLORS) {
            ItemStack stack = new ItemStack(this);
            CompoundTag tag = new CompoundTag();
            tag.putInt("color", color);
            stack.setTag(tag);
            stacks.add(stack);
        }
    }
}
