package com.viruss.waw.utils.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.viruss.waw.Main;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.fmllegacy.RegistryObject;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public abstract class AbstractLootProvider implements DataProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final DataGenerator generator;
    private final String modid = Main.MOD_ID;
    private final Map<ResourceLocation, LootTable> tables;

    public AbstractLootProvider(DataGenerator generator) {
        this.generator = generator;
        this.tables = new HashMap<>();;
    }

    @Override
    public void run(HashCache cache) {
        addTables();

        tables.forEach((location, table) -> {
            Path lootTablePath = this.generator.getOutputFolder()
                    .resolve("data/" + location.getNamespace() + "/loot_tables/" + location.getPath() + ".json");
            try {
                DataProvider.save(GSON, cache, LootTables.serialize(table), lootTablePath);
            } catch (IOException e) {
                Main.LOGGER.error("Couldn't write loot table {}", lootTablePath, e);
            }
        });
    }

    @Override
    public String getName() {
        return "LootTables: " + this.modid;
    }

    private String name(Block block) {
        return block.getRegistryName().getPath();
    }

    protected void addTables(){
    }

    public void slabBlock(Block slabBlock) {
        slabBlock(name(slabBlock), slabBlock);
    }

    public void slabBlock(String name, Block slabBlock) {
        this.tables.put(new ResourceLocation(this.modid, "blocks/" + name),
            LootTable.lootTable().setParamSet(LootContextParamSets.BLOCK).withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(slabBlock)
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2)).when(
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(slabBlock).setProperties(
                            StatePropertiesPredicate.Builder.properties()
                                .hasProperty(SlabBlock.TYPE, SlabType.DOUBLE))))
                    .apply(ApplyExplosionDecay.explosionDecay()))).build());
    }

    public void doorBlock(Block doorBlock) {
        doorBlock(name(doorBlock), doorBlock);
    }

    public void doorBlock(String name, Block doorBlock) {
        this.tables.put(new ResourceLocation(this.modid, "blocks/" + name),
            LootTable.lootTable().setParamSet(LootContextParamSets.BLOCK).withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(doorBlock)
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(doorBlock).setProperties(
                        StatePropertiesPredicate.Builder.properties()
                            .hasProperty(DoorBlock.HALF, DoubleBlockHalf.LOWER))))
                    .when(ExplosionCondition.survivesExplosion())).build());
    }

    public void leavesBlock(Block block, Block saplingBlock, RegistryObject<Item> apple) {
        leavesBlock(name(block), block, saplingBlock, apple);
    }

    public void leavesBlock(String name, Block block, Block sapling, RegistryObject<Item> apple) {
        LootTable.Builder builder = LootTable.lootTable().setParamSet(LootContextParamSets.BLOCK).withPool(
            LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(
            AlternativesEntry.alternatives(LootItem.lootTableItem(block).when(AlternativeLootItemCondition
                .alternative(MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS)), MatchTool
                    .toolMatches(ItemPredicate.Builder.item().hasEnchantment(
                        new EnchantmentPredicate(Enchantments.SILK_TOUCH,
                            MinMaxBounds.Ints.atLeast(1)))))),
                    LootItem.lootTableItem(sapling).when(ExplosionCondition.survivesExplosion()).when(
                        BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 1f / 20f, 1f / 16f, 1f / 12f, 1f / 10f)))))
                    .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(Items.STICK)
                    .when(BonusLevelTableCondition
                        .bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 1f / 50f, 1f / 45f, 1f / 40f, 1f / 30f, 1f / 10f))
                    .apply(ApplyExplosionDecay.explosionDecay())).when(InvertedLootItemCondition.invert(AlternativeLootItemCondition
                    .alternative(MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS)), MatchTool
                        .toolMatches(ItemPredicate.Builder.item().hasEnchantment(
                            new EnchantmentPredicate(Enchantments.SILK_TOUCH,
                                MinMaxBounds.Ints.atLeast(1))))))));
        if (apple != null) {
        builder.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when(InvertedLootItemCondition.invert(
            AlternativeLootItemCondition.alternative(MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS)), MatchTool
                .toolMatches(ItemPredicate.Builder.item().hasEnchantment(
                    new EnchantmentPredicate(Enchantments.SILK_TOUCH,
                        MinMaxBounds.Ints.atLeast(1))))))).add(
                LootItem.lootTableItem(apple.get()).when(ExplosionCondition.survivesExplosion()).when(
                    BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 1f / 200f, 1f / 180f, 1f / 160f, 1f / 120f,
                        1f / 40f))));
        }
        this.tables.put(new ResourceLocation(this.modid, "blocks/" + name), builder.build());
    }

    public void regularBlock(Block block) {
        regularBlock(name(block), block);
    }

    public void regularBlock(String name, Block block) {
    this.tables.put(new ResourceLocation(this.modid, "blocks/" + name),
        LootTable.lootTable().setParamSet(LootContextParamSets.BLOCK).withPool(
            LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when(ExplosionCondition.survivesExplosion())
                .add(LootItem.lootTableItem(block))).build());
    }

}
