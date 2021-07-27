package com.viruss.waw.utils.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.viruss.waw.WitchingAndWizardry;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.*;
import net.minecraft.loot.functions.ExplosionDecay;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BasicLootProvider implements IDataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final DataGenerator generator;
    private final String modid;
    private final Map<ResourceLocation, LootTable> tables;

    public BasicLootProvider(DataGenerator gen, String modid) {
        this.tables = new HashMap<>();
        this.modid = modid;
        this.generator = gen;
    }

    @Override
    public void run(DirectoryCache cache) {
        addTables();

        tables.forEach((location, table) -> {
            Path lootTablePath = this.generator.getOutputFolder()
                    .resolve("data/" + location.getNamespace() + "/loot_tables/" + location.getPath() + ".json");
            try {
                IDataProvider.save(GSON, cache, LootTableManager.serialize(table), lootTablePath);
            } catch (IOException e) {
                WitchingAndWizardry.LOGGER.error("Couldn't write loot table {}", lootTablePath, e);
            }
        });
    }

    public void slabBlock(SlabBlock slabBlock) {
        slabBlock(name(slabBlock), slabBlock);
    }

    public void slabBlock(String name, SlabBlock slabBlock) {
        this.tables.put(new ResourceLocation(this.modid, "blocks/" + name),
                LootTable.lootTable().setParamSet(LootParameterSets.BLOCK).withPool(
                        LootPool.lootPool().setRolls(ConstantRange.exactly(1)).add(ItemLootEntry.lootTableItem(slabBlock)
                                .apply(SetCount.setCount(ConstantRange.exactly(2)).when(
                                        BlockStateProperty.hasBlockStateProperties(slabBlock).setProperties(
                                                StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(SlabBlock.TYPE, SlabType.DOUBLE))))
                                .apply(ExplosionDecay.explosionDecay()))).build());
    }

    public void doorBlock(DoorBlock doorBlock) {
        doorBlock(name(doorBlock), doorBlock);
    }

    public void doorBlock(String name, DoorBlock doorBlock) {
        this.tables.put(new ResourceLocation(this.modid, "blocks/" + name),
                LootTable.lootTable().setParamSet(LootParameterSets.BLOCK).withPool(
                        LootPool.lootPool().setRolls(ConstantRange.exactly(1)).add(ItemLootEntry.lootTableItem(doorBlock)
                                .when(BlockStateProperty.hasBlockStateProperties(doorBlock).setProperties(
                                        StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(DoorBlock.HALF, DoubleBlockHalf.LOWER))))
                                .when(SurvivesExplosion.survivesExplosion())).build());
    }

    public void leavesBlock(LeavesBlock block, SaplingBlock saplingBlock, RegistryObject<Item> apple) {
        leavesBlock(name(block), block, saplingBlock, apple);
    }

    public void leavesBlock(String name, LeavesBlock block, SaplingBlock sapling, RegistryObject<Item> apple) {
        LootTable.Builder builder = LootTable.lootTable().setParamSet(LootParameterSets.BLOCK).withPool(
                LootPool.lootPool().setRolls(ConstantRange.exactly(1)).add(AlternativesLootEntry.alternatives(
                        ItemLootEntry.lootTableItem(block).when(Alternative
                                .alternative(MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS)), MatchTool
                                        .toolMatches(ItemPredicate.Builder.item().hasEnchantment(
                                                new EnchantmentPredicate(Enchantments.SILK_TOUCH,
                                                        MinMaxBounds.IntBound.atLeast(1)))))),
                        ItemLootEntry.lootTableItem(sapling).when(SurvivesExplosion.survivesExplosion()).when(
                                TableBonus.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 1f / 20f, 1f / 16f, 1f / 12f, 1f / 10f)))))
                .withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1)).add(ItemLootEntry.lootTableItem(Items.STICK)
                        .when(TableBonus
                                .bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 1f / 50f, 1f / 45f, 1f / 40f, 1f / 30f, 1f / 10f))
                        .apply(ExplosionDecay.explosionDecay())).when(Inverted.invert(Alternative
                        .alternative(MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS)), MatchTool
                                .toolMatches(ItemPredicate.Builder.item().hasEnchantment(
                                        new EnchantmentPredicate(Enchantments.SILK_TOUCH,
                                                MinMaxBounds.IntBound.atLeast(1))))))));
        if (apple != null) {
            builder.withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1)).when(Inverted.invert(
                    Alternative.alternative(MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS)), MatchTool
                            .toolMatches(ItemPredicate.Builder.item().hasEnchantment(
                                    new EnchantmentPredicate(Enchantments.SILK_TOUCH,
                                            MinMaxBounds.IntBound.atLeast(1))))))).add(
                    ItemLootEntry.lootTableItem(apple.get()).when(SurvivesExplosion.survivesExplosion()).when(
                            TableBonus.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 1f / 200f, 1f / 180f, 1f / 160f, 1f / 120f,
                                    1f / 40f))));
        }
        this.tables.put(new ResourceLocation(this.modid, "blocks/" + name), builder.build());
    }

    public void regularBlock(Block block) {
        regularBlock(name(block), block);
    }

    public void regularBlock(String name, Block block) {
        this.tables.put(new ResourceLocation(this.modid, "blocks/" + name),
                LootTable.lootTable().setParamSet(LootParameterSets.BLOCK).withPool(
                        LootPool.lootPool().setRolls(ConstantRange.exactly(1)).when(SurvivesExplosion.survivesExplosion())
                                .add(ItemLootEntry.lootTableItem(block))).build());
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
}
