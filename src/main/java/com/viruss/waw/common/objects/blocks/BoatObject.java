package com.viruss.waw.common.objects.blocks;

import com.viruss.waw.common.objects.blocks.bases.AbstractBoat;
import com.viruss.waw.common.objects.blocks.bases.AbstractBoatItem;
import com.viruss.waw.utils.RegistryHandler;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

public class  BoatObject {
    private final EntityType<BoatEntity> entityType;
    private final Item item;

    public BoatObject(String name,ItemGroup group) {
        this.entityType = EntityType.Builder.<BoatEntity>of(AbstractBoat::new, EntityClassification.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10).build(name);
        this.item = new AbstractBoatItem(entityType,group);

        RegistryHandler.MDR.register(name,() -> item,ForgeRegistries.ITEMS);
        RegistryHandler.MDR.register(name,() -> this.entityType,ForgeRegistries.ENTITIES) ;
    }

    public EntityType<BoatEntity> getType() {
        return entityType;
    }

    public Item getItem() {
        return item;
    }
}
