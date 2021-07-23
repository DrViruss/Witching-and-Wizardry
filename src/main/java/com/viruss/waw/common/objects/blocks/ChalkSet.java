package com.viruss.waw.common.objects.blocks;

import com.viruss.waw.WitchingAndWizardry;
import com.viruss.waw.common.objects.items.Chalk;
import com.viruss.waw.utils.ModUtils;
import com.viruss.waw.utils.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ChalkSet {
    private final Map<Chalk.Type,ChalkObject> chalkMap;
    private RegistryObject<SoundEvent> sound;

    public ChalkSet(Chalk.Type[] types) {
        this.chalkMap = new HashMap<>();

        for(Chalk.Type type : types)
            this.chalkMap.put(type,new ChalkObject(type));
        initAdditional();
    }

    private void initAdditional() {
        this.chalkMap.forEach((type, chalkObject) -> {
            WitchingAndWizardry.CLIENT_RENDERER.addBlockRenderer(chalkObject.ro, RenderType.cutout());
        });

       sound = RegistryHandler.MDR.register("chalk", ()-> ModUtils.loadSound("chalk"),ForgeRegistries.SOUND_EVENTS);
    }

    public ChalkObject getChalk(Chalk.Type type)
    {
        return this.chalkMap.get(type);
    }

    public void foreach(BiConsumer<? super Chalk.Type, ? super ChalkObject> action)
    {
        this.chalkMap.forEach(action);
    }

    public SoundEvent getSound() {
        return sound.get();
    }

    public static class ChalkObject{

        private final Block symbol;
        private final Item chalk;
        private final RegistryObject<Block> ro;

        public ChalkObject(Chalk.Type type) {
            this.symbol = new ChalkSymbol(type);
            this.chalk = new Chalk(type);

            ro = RegistryHandler.MDR.register("symbol_"+type.getName(),() -> symbol, ForgeRegistries.BLOCKS);
            RegistryHandler.MDR.register("chalk_"+type.getName(),() -> chalk, ForgeRegistries.ITEMS);
        }

        public Block getSymbol() {
            return symbol;
        }

        public Item getChalk() {
            return chalk;
        }

        public RegistryObject<Block> getRo() {
            return ro;
        }
    }
}