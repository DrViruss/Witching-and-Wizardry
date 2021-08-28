package com.viruss.waw.common.objects.items;

import com.viruss.waw.Main;
import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jline.utils.Colors;

import java.awt.*;
import java.util.Objects;

@SuppressWarnings("all")
public class Chalk extends Item {
    private final Type type;
    public Chalk(Type type) {
        super(new Item.Properties().setNoRepair().defaultDurability(128).tab(Main.ITEM_GROUP));
        this.type = type;
    }
    public Chalk(Type type, int durability) {
        super(new Item.Properties().setNoRepair().defaultDurability(durability).tab(Main.ITEM_GROUP));
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
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);
        Player player = context.getPlayer();
        boolean isReplacing = world.getBlockState(pos).getBlock().canBeReplaced(state, new BlockPlaceContext(context));
        if (!world.isClientSide() && ((context.getClickedFace() == Direction.UP && world.getBlockState(pos.above()).isAir()) || isReplacing)) {
//            if () {
                world.setBlockAndUpdate(isReplacing ? pos : pos.above(),ModRegistry.CHALKS.getChalk(type).getSymbol().getStateForPlacement(new BlockPlaceContext(context)));
                world.playSound(null, pos, ModRegistry.CHALKS.getSound(), SoundSource.PLAYERS, 3f,3f);


                if (!player.isCreative()) {
                    ItemStack heldChalk = context.getItemInHand();
                    heldChalk.setDamageValue(heldChalk.getDamageValue() + 1);

                    if (heldChalk.getDamageValue() >= heldChalk.getMaxDamage())
                        heldChalk.shrink(1);
//                }

            }
        }
        return InteractionResult.SUCCESS;
    }

    public record Type(String name, int color) {
        public static final Type WHITE = new Type("white", Color.WHITE.getRGB());
        public static final Type RED = new Type("red", Color.RED.getRGB());

        public static final Type CENTRAL = new Type("central",Color.LIGHT_GRAY.getRGB()+Color.WHITE.getRGB());

        public int getColor() {
            return color;
        }

        public String getName() {
            return name;
        }
    }
}
