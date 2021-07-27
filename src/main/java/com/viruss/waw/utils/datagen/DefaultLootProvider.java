package com.viruss.waw.utils.datagen;

import com.viruss.waw.WitchingAndWizardry;
import com.viruss.waw.common.objects.packs.WoodenObject;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTable;

import java.util.Map;
import java.util.Set;

public class DefaultLootProvider extends BasicLootProvider{
    BlockLoot blockLoot;
    EntityLoot entityLoot;

    public DefaultLootProvider(DataGenerator generator,Set<WoodenObject> woods,Map<EntityType<?>, LootTable.Builder> entityDrops) {
        super(generator,WitchingAndWizardry.MOD_ID);
        blockLoot = new BlockLoot(woods);
        entityLoot = new EntityLoot(entityDrops);
    }

    @Override
    protected void addTables() {
        this.blockLoot.addTables();
        this.entityLoot.addTables();
    }


    public static class EntityLoot extends EntityLootTables{
        private final Map<EntityType<?>, LootTable.Builder> drops;

        public EntityLoot(Map<EntityType<?>, LootTable.Builder> drops) {
            this.drops = drops;
        }

        @Override
        protected void addTables() {
            drops.forEach(this::add);
        }
    }
    public class BlockLoot extends BlockLootTables{
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
            doorBlock((DoorBlock) wood.getDoor().getPrimary());
            regularBlock(wood.getGate().getPrimary());
            regularBlock(wood.getFence().getPrimary());
            slabBlock((SlabBlock) wood.getSlab().getPrimary());
            regularBlock(wood.getStairs().getPrimary());
            regularBlock(wood.getTrapdoor().getPrimary());
            regularBlock(wood.getSign().getSign());
            regularBlock(wood.getButton().getPrimary());
            regularBlock(wood.getPressure_plate().getPrimary());
            regularBlock(wood.getWood().getPrimary());

            if(wood.getStrippedLog()!= null) {
                regularBlock(wood.getStrippedWood().getPrimary());
                regularBlock(wood.getStrippedLog().getPrimary());
            }
            if(wood.getSapling()!= null) {
                regularBlock(wood.getSapling().getPrimary());
                leavesBlock((LeavesBlock) wood.getLeaves().getPrimary(), (SaplingBlock) wood.getSapling().getPrimary(),wood.getFruit());
            }
        }


    }

}
