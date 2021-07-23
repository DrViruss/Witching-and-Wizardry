package com.viruss.waw.utils.datagen;

import com.viruss.waw.WitchingAndWizardry;
import com.viruss.waw.common.objects.blocks.WoodenObject;
import com.viruss.waw.utils.RegistryHandler;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTable;

import java.util.HashMap;
import java.util.Map;

public class DefaultLootProvider extends BasicLootProvider{

    BlockLoot blockLoot = new BlockLoot();
    EntityLoot entityLoot = new EntityLoot();

    public DefaultLootProvider(DataGenerator generator) {
        super(generator,WitchingAndWizardry.MOD_ID);
    }

    @Override
    protected void addTables() {
        this.blockLoot.addTables();
        this.entityLoot.addTables();
    }


    public static class EntityLoot extends EntityLootTables{
        private static Map<EntityType<?>, LootTable.Builder> drops = new HashMap<>();
        @Override
        protected void addTables() {
            drops.forEach(this::add);
        }

        public static void addLoot(EntityType<?> entityType, LootTable.Builder loot){
            drops.put(entityType,loot);
        }
    }
    public class BlockLoot extends BlockLootTables{

        @Override
        protected void addTables() {
            addWoodDrops(RegistryHandler.ASH);
        }

        private void addWoodDrops(WoodenObject wood){

            regularBlock(wood.getPlanks().getPrimary());
            regularBlock(wood.getLog().getPrimary());
            doorBlock((DoorBlock) wood.getDoor().getPrimary());
            regularBlock(wood.getGate().getPrimary());
            regularBlock(wood.getFence().getPrimary());
            slabBlock((SlabBlock) wood.getSlab().getPrimary());
            regularBlock(wood.getStairs().getPrimary());
            regularBlock(wood.getStrippedLog().getPrimary());
            regularBlock(wood.getTrapdoor().getPrimary());
            regularBlock(wood.getSign().getSign());
            regularBlock(wood.getButton().getPrimary());
            regularBlock(wood.getPressure_plate().getPrimary());
            regularBlock(wood.getWood().getPrimary());
            regularBlock(wood.getStrippedWood().getPrimary());
            if(wood.getSapling()!= null) {
                regularBlock(wood.getSapling().getPrimary());
                leavesBlock((LeavesBlock) wood.getLeaves().getPrimary(), (SaplingBlock) wood.getSapling().getPrimary(),null);
            }

        }


    }

}
