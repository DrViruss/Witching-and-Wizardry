package com.viruss.waw.common.objects.packs;

import com.viruss.waw.Main;
import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public class Ingredients {
    private final Item calcosvis;
    private final Item magicAsh;

    public Ingredients() {
        this.calcosvis = basicItem();
        this.magicAsh = basicItem();

        ModRegistry.MDR.register("calcosvis",()->calcosvis, ForgeRegistries.ITEMS);
        ModRegistry.MDR.register("magic_ash",()->magicAsh, ForgeRegistries.ITEMS);
    }
    private Item basicItem(){
        return new Item(new Item.Properties().tab(Main.ITEM_GROUP));
    }

    public Item getCalcosvis() {
        return calcosvis;
    }

    public Item getMagicAsh() {
        return magicAsh;
    }
}
