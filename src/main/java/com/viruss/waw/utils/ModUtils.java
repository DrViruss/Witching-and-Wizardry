package com.viruss.waw.utils;

import com.viruss.waw.Main;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

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

}
