package com.viruss.waw.utils.registrations;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.Supplier;

public class DoubleRegisteredObject<PRIMARY extends IForgeRegistryEntry<? super PRIMARY>, SECONDARY extends IForgeRegistryEntry<? super SECONDARY>> {
    private final RegistryObject<PRIMARY> primaryRO;
    private final RegistryObject<SECONDARY> secondaryRO;
//    private final Supplier<PRIMARY> primary;
//    private final Supplier<SECONDARY> secondary;

    public DoubleRegisteredObject(RegistryObject<PRIMARY> primaryRO, RegistryObject<SECONDARY> secondaryRO/*,Supplier<PRIMARY> primary,Supplier<SECONDARY> secondary*/) {
        this.primaryRO = primaryRO;
        this.secondaryRO = secondaryRO;
//        this.primary = primary;
//        this.secondary = secondary;
    }

//    public PRIMARY getPrimary() {
//        return primary.get();
//    }
//
//    public SECONDARY getSecondary() {
//        return secondary.get();
//    }

    public RegistryObject<PRIMARY> getPrimaryRO() {
        return primaryRO;
    }

    public RegistryObject<SECONDARY> getSecondaryRO() {
        return secondaryRO;
    }
    public PRIMARY getPrimary() {
        return primaryRO.get();
    }

    public SECONDARY getSecondary() {
        return secondaryRO.get();
    }
}