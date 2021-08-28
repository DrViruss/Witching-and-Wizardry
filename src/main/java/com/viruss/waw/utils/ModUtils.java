package com.viruss.waw.utils;

import com.google.gson.JsonObject;
import com.mojang.realmsclient.dto.Ops;
import com.mojang.serialization.*;
import com.viruss.waw.Main;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

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

            return Integer.MAX_VALUE;
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
}
