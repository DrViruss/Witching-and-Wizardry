package com.viruss.waw.utils;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.serialization.JsonOps;
import com.viruss.waw.Main;
import com.viruss.waw.common.objects.packs.ChalkSet;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
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
        public static int colorToProperty(int color)
        {
            for(int i=0; i<ChalkSet.COLORS.length; i++)
                if(ChalkSet.COLORS[i] == color)
                    return i;
            return 0;
        }
    }

    public static class Resources {

        public static SoundEvent loadSound(String name) {
            return new SoundEvent(new ResourceLocation(Main.MOD_ID, name));
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
            Tag tag = nbt.get(oldName);
            if (tag != null) {
                nbt.remove(oldName);
                nbt.put(newName, tag);
            }
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
}
