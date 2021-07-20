package com.viruss.waw.utils;

import com.viruss.waw.WitchingAndWizardry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class ModUtils {
    public static SoundEvent loadSound(String name)
    {
        return new SoundEvent(new ResourceLocation(WitchingAndWizardry.MOD_ID, name));
    }
}
