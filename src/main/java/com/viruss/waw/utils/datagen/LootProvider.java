package com.viruss.waw.utils.datagen;

import com.viruss.waw.common.objects.packs.WoodenPack;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Map;
import java.util.Set;

public class LootProvider extends AbstractLootProvider{
    final BlockLoot blockLoot;
    final EntityLoot entityLoot;

    public LootProvider(DataGenerator generator, Set<WoodenPack> woods, Map<EntityType<?>, LootTable.Builder> entityDrops) {
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
        private final Set<WoodenPack> woods;

        public BlockLoot(Set<WoodenPack> woodenPacks) {
            this.woods = woodenPacks;
        }

        @Override
        protected void addTables() {
            for(WoodenPack wood : woods)
                addWoodDrops(wood);
        }

        private void addWoodDrops(WoodenPack wood){
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
