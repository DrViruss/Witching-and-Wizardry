package com.viruss.waw.utils;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.serialization.JsonOps;
import com.viruss.waw.common.objects.blocks.chalk.BasicSymbol;
import com.viruss.waw.common.objects.packs.ChalkSet;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

//@SuppressWarnings("all")
public class ModUtils {

    public static class Colors{
        public static float getRed(int color){
            return (color >> 16 & 255) / 255.0F;
        }
        public static float getGreen(int color){
            return (color >> 8 & 255) / 255.0F;
        }
        public static float getBlue(int color){
            return (color & 255) / 255.0F;
        }

        public static int propertyToColor(int property)
        {
            return ChalkSet.COLORS[property];
        }
        public static int colorToProperty(int color) {
            for(int i=0; i<ChalkSet.COLORS.length; i++)
                if(ChalkSet.COLORS[i] == color)
                    return i;
            return 0;
        }
    }

    public static class Resources {

        public static SoundEvent loadSound(String id, String name) {
            return new SoundEvent(new ResourceLocation(id,name));
        }
    }

    public static class Inventory {

        public static int getSlotByItem(Container container, ItemStack itemStack) {
            for(int i=0; i<container.getContainerSize(); i++) {
                if(container.getItem(i).is(itemStack.getItem()))
                    return i;
            }

            return Integer.MIN_VALUE;
        }

        public static int getSlotByItem(Container container, NonNullList<net.minecraft.world.item.crafting.Ingredient> list) {
            for(net.minecraft.world.item.crafting.Ingredient ingredient : list){
                for(int i=0; i<container.getContainerSize(); i++) {
                    if(ingredient.test(container.getItem(i)))
                        return i;
                }
            }

            return Integer.MIN_VALUE;
        }

        public static void damageItem(boolean isCreative,ItemStack stack) {
            if (isCreative) return;

            stack.setDamageValue(stack.getDamageValue() + 1);
            if (stack.getDamageValue() >= stack.getMaxDamage())
                stack.shrink(1);
        }
    }

    public static class Json{
        public static JsonObject serializeItemStack(ItemStack stack){
            CompoundTag nbt = stack.serializeNBT();
            byte c = nbt.getByte("Count");
            if (c != 1)
                nbt.putByte("count", c);
            nbt.remove("Count");
            renameTag(nbt, "id", "item");
            renameTag(nbt, "tag", "nbt");
            return NbtOps.INSTANCE.convertTo(JsonOps.INSTANCE,nbt).getAsJsonObject();
        }

        public static void renameTag(CompoundTag nbt, String oldName, String newName) {
            if (!nbt.contains(oldName)) return;

            Tag tag = nbt.get(oldName);
            nbt.remove(oldName);
            nbt.put(newName, tag);
        }
    }

    public static class Ingredient{

        /**
         * Helpful when SAME ITEMS has different tags
         * [Ex: itemSalt, dustSalt, foodSalt, etc.]
         * @param tags tags of items for Ingredient
         * @return Ingredient which contain ALL item from ALL tags
         */
        public static net.minecraft.world.item.crafting.Ingredient fromTags(net.minecraft.tags.Tag<Item>... tags) {
            List<net.minecraft.world.item.crafting.Ingredient.TagValue> values = new ArrayList<>();
            for(net.minecraft.tags.Tag<Item> tag : tags)
                values.add(new net.minecraft.world.item.crafting.Ingredient.TagValue(tag));
            return net.minecraft.world.item.crafting.Ingredient.fromValues(values.stream());
        }

        public static boolean testItem(net.minecraft.world.item.crafting.Ingredient ingredient, ItemStack item ) {
            for(ItemStack stack : ingredient.getItems())
                if(stack.sameItem(item) && stack.getCount()==item.getCount())
                    return true;
            return false;
        }
    }

    public static class Keyboard{
        private static final long Window = Minecraft.getInstance().getWindow().getWindow();

        @OnlyIn(Dist.CLIENT)
        public static boolean isHoldingShift() {
            return InputConstants.isKeyDown(Window , GLFW.GLFW_KEY_LEFT_SHIFT) || InputConstants.isKeyDown(Window,GLFW.GLFW_KEY_RIGHT_SHIFT);
        }

        @OnlyIn(Dist.CLIENT)
        public static boolean isHoldingCtrl() {
            return InputConstants.isKeyDown(Window , GLFW.GLFW_KEY_LEFT_CONTROL) || InputConstants.isKeyDown(Window, GLFW.GLFW_KEY_RIGHT_CONTROL);
        }
    }

    public static class Rituals{
        public static final Pair<Integer, Integer> sCircle_data = new Pair<>(3,16);
        public static final Pair<Integer, Integer> mCircle_data = new Pair<>(5,28);
        public static final Pair<Integer, Integer> lCircle_data = new Pair<>(7,40);

        public static BlockPos[] getBorderCoords(BlockPos center, Pair<Integer, Integer> circle_data){
            BlockPos[] result = new BlockPos[circle_data.getB()]; //TODO: something more useful

            for (int i = 0; i < circle_data.getB(); i++) {
                double angle = Math.toRadians(((double) (i + 1) / circle_data.getB()) * 360);
                double x = Math.round(center.getX() + circle_data.getA() * Math.cos(angle));
                double z = Math.round(center.getZ() + circle_data.getA() * Math.sin(angle));

                result[i] = new BlockPos(x, center.getY(), z);
            }
            if(circle_data==lCircle_data) {
                result[12]=new BlockPos(result[12].getX(),result[12].getY(),result[12].getZ()+1);
                result[6]=new BlockPos(result[6].getX(),result[6].getY(),result[6].getZ()+1);

                result[2]=new BlockPos(result[2].getX()+1,result[2].getY(),result[2].getZ());
                result[36]=new BlockPos(result[36].getX()+1,result[36].getY(),result[36].getZ());

                result[32]=new BlockPos(result[32].getX(),result[32].getY(),result[32].getZ()-1);
                result[26]=new BlockPos(result[26].getX(),result[26].getY(),result[26].getZ()-1);

                result[22]=new BlockPos(result[22].getX()-1,result[22].getY(),result[22].getZ());
                result[16]=new BlockPos(result[16].getX()-1,result[16].getY(),result[16].getZ());
            }

                return result;
        }

        public static boolean checkChalk(Level level, BlockPos[] blocks, int color){
            for(BlockPos pos : blocks){
                BlockState state = level.getBlockState(pos);
                if(!(state.getBlock() instanceof BasicSymbol) || (Colors.propertyToColor(state.getValue(BasicSymbol.COLOR))!=color))
                    return false;
            }
            return true;
        }

        public static boolean checkCandles(Level level, List<BlockPos> blocks, List<Pair<Integer,Integer>> candles){
            ArrayList<BlockState> states = new ArrayList<>();
            for(BlockPos pos : blocks){
                BlockState state = level.getBlockState(pos);
                if(state.getBlock() instanceof CandleBlock && state.getValue(CandleBlock.LIT))
                    states.add(state);
            }

            for(Pair<Integer,Integer> candle : candles){
                AtomicInteger amount = new AtomicInteger(candle.getB());
                for(int i=0; i< candle.getB(); i++)
                    if(!states.removeIf((state)-> {
                        if(state.getBlock().defaultMaterialColor().col == candle.getA()) {
                            amount.set(amount.get() - 1);
                            return true;
                        }
                        return false;
                    }))
                        if(amount.intValue() > 0)
                            return false;
            }

            return true;
        }

        public static BlockPos[] candlesPoses(BlockPos[] chalkPoses){
            return switch (chalkPoses.length) {
                case (40) -> calcCandles(3, 4, chalkPoses,11,9);
                case (28) -> calcCandles(2, 3, chalkPoses,8,6);
                default -> calcCandles(1, 2, chalkPoses,5,3);
            };
        }

        private static BlockPos[] calcCandles(int c_starts, int c_amount,BlockPos[]chalkPoses,int w_mod,int h_mod  ){
            int r_counter=0;
            int z_mod=-1;

            BlockPos[] result = new BlockPos[c_amount*4];
            for(int i=0; i<4; i++){
                for (int j =0; j<c_amount;j++){
                    result[r_counter]= new BlockPos(chalkPoses[c_starts+j].getX(),chalkPoses[0].getY(),chalkPoses[c_starts+j].getZ()+z_mod);
                    r_counter= r_counter+1;
                }

                if(i==1) {
                    z_mod = 1;
                    c_starts= c_starts+w_mod;
                }
                else
                    c_starts= c_starts+h_mod;

            }
            return result;
        }

        public static String nameByCircle(Pair<Integer, Integer> circle_data){
            return switch (circle_data.getA()) {
                case (7) -> "large";
                case (5) -> "medium";
                default -> "small";
            };
        }

        public static Pair<Integer, Integer> circleByName(String name){
            return switch (name) {
                case ("large") -> lCircle_data;
                case ("medium") -> mCircle_data;
                default -> sCircle_data;
            };
        }
    }
}
