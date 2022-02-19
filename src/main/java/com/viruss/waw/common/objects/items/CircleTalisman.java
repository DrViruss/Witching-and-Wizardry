package com.viruss.waw.common.objects.items;

import com.viruss.waw.Main;
import com.viruss.waw.common.objects.blocks.chalk.BasicSymbol;
import com.viruss.waw.common.objects.blocks.chalk.CentralSymbol;
import com.viruss.waw.utils.ModUtils;
import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.Random;

public class CircleTalisman extends Item {

    public CircleTalisman() {
        super(new Item.Properties().tab(Main.ITEM_GROUP));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos center = context.getClickedPos().above();
        if(!world.isClientSide()) {
            world.setBlockAndUpdate(center, ModRegistry.CHALKS.getCentralBlock().defaultBlockState().setValue(CentralSymbol.STAGE,2));
            for(BlockPos pos : ModUtils.Rituals.getBorderCoords(center,ModUtils.Rituals.circleByName(context.getItemInHand().getTag().getString("type"))))
                world.setBlockAndUpdate(pos, ModRegistry.CHALKS.getBasicBlock().defaultBlockState().setValue(BasicSymbol.SIGN, new Random().nextInt(BasicSymbol.SIGN.getPossibleValues().size())).setValue(BasicSymbol.COLOR,0));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> stacks) {
        if (!this.allowdedIn(tab)) return;

        ItemStack s = new ItemStack(this);
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "large");
        s.setTag(tag);
        s.setHoverName(new TextComponent("large_talisman"));
        stacks.add(s);

        s = new ItemStack(this);
        tag = new CompoundTag();
        tag.putString("type", "medium");
        s.setTag(tag);
        s.setHoverName(new TextComponent("medium_talisman"));
        stacks.add(s);

        s = new ItemStack(this);
        tag = new CompoundTag();
        tag.putString("type", "small");
        s.setTag(tag);
        s.setHoverName(new TextComponent("small_talisman"));
        stacks.add(s);
    }
}
