package com.viruss.waw.utils.datagen;

import com.viruss.waw.common.objects.packs.WoodenObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Map;
import java.util.Set;

public class LootProvider extends AbstractLootProvider{
    BlockLoot blockLoot;
    EntityLoot entityLoot;

    public LootProvider(DataGenerator generator,Set<WoodenObject> woods,Map<EntityType<?>, LootTable.Builder> entityDrops) {
        super(generator);
        blockLoot = new BlockLoot(woods);
        this.entityLoot = new EntityLoot(entityDrops);
    }

    @Override
    protected void addTables() {
        this.blockLoot.addTables();
        this.entityLoot.addTables();
    }


    public static class EntityLoot extends net.minecraft.data.loot.EntityLoot{
        private final Map<EntityType<?>, LootTable.Builder> drops;

        public EntityLoot(Map<EntityType<?>, LootTable.Builder> drops) {
            this.drops = drops;
        }

        @Override
        protected void addTables() {
            drops.forEach(this::add);
        }
    }
    public class BlockLoot extends net.minecraft.data.loot.BlockLoot{
        private final Set<WoodenObject> woodenObjects;

        public BlockLoot(Set<WoodenObject> woodenObjects) {
            this.woodenObjects = woodenObjects;
        }

        @Override
        protected void addTables() {
            for(WoodenObject wood : woodenObjects)
                addWoodDrops(wood);
        }

        private void addWoodDrops(WoodenObject wood){
            regularBlock(wood.getPlanks().getPrimary());
            regularBlock(wood.getLog().getPrimary());
            doorBlock(wood.getDoor().getPrimary());
            regularBlock(wood.getGate().getPrimary());
            regularBlock(wood.getFence().getPrimary());
            slabBlock(wood.getSlab().getPrimary());
            regularBlock(wood.getStairs().getPrimary());
            regularBlock(wood.getTrapdoor().getPrimary());
            regularBlock(wood.getSign().getSign());
            regularBlock(wood.getButton().getPrimary());
            regularBlock(wood.getPressurePlate().getPrimary());
            regularBlock(wood.getWood().getPrimary());

            if(wood.getStrippedLog()!= null) {
                regularBlock(wood.getStrippedWood().getPrimary());
                regularBlock(wood.getStrippedLog().getPrimary());
            }
            if(wood.getSapling()!= null) {
                regularBlock(wood.getSapling().getPrimary());
                leavesBlock(wood.getLeaves().getPrimary(),wood.getSapling().getPrimary(),wood.getFruit());
            }
        }
    }
}
