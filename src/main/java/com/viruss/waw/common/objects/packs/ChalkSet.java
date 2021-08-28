package com.viruss.waw.common.objects.packs;

import com.viruss.waw.Main;
import com.viruss.waw.client.renders.tile.CentralSymbolRenderer;
import com.viruss.waw.common.objects.blocks.chalk.BasicSymbol;
import com.viruss.waw.common.objects.blocks.chalk.CentralSymbol;
import com.viruss.waw.common.objects.items.Chalk;
import com.viruss.waw.common.tile.CentralSymbolTE;
import com.viruss.waw.utils.ModUtils;
import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ChalkSet {
    private final Map<Chalk.Type,ChalkObject> chalkMap;
    private RegistryObject<SoundEvent> sound;
    private final BlockEntityType<CentralSymbolTE> centerTE;

    public ChalkSet(Chalk.Type... types) {
        this.chalkMap = new HashMap<>();

        //TODO: use 1 chalk with colorTag. See: 'Item#fillItemCategory'

        for(Chalk.Type type : types)
            this.chalkMap.put(type,new ChalkObject(type));

        this.chalkMap.put(Chalk.Type.CENTRAL,new ChalkObject(Chalk.Type.CENTRAL, new CentralSymbol(Chalk.Type.CENTRAL)));

        this.centerTE = BlockEntityType.Builder.of(CentralSymbolTE::new,chalkMap.get(Chalk.Type.CENTRAL).getSymbol()).build(null);
        ModRegistry.MDR.register("center",()->centerTE,ForgeRegistries.BLOCK_ENTITIES);


        initAdditional();
    }

    private void initAdditional() {
        this.chalkMap.forEach((type, chalkObject) -> {
            if(chalkObject.getSymbol() instanceof CentralSymbol)
                Main.CLIENT_RENDERER.addBlockRenderer(chalkObject.ro, RenderType.translucent());
            else
                Main.CLIENT_RENDERER.addBlockRenderer(chalkObject.ro, RenderType.cutout());
        });
        Main.CLIENT_RENDERER.addTileEntityRenderer(this.centerTE, CentralSymbolRenderer::new);

        sound = ModRegistry.MDR.register("chalk", ()-> ModUtils.Resources.loadSound("chalk"),ForgeRegistries.SOUND_EVENTS);
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

    public BlockEntityType<CentralSymbolTE> getCenterTE() {
        return centerTE;
    }

    public static class ChalkObject{

        private final Block symbol;
        private final Item chalk;
        private final RegistryObject<Block> ro;

        public ChalkObject(Chalk.Type type) {
            this(type,new BasicSymbol(type));
        }

        public ChalkObject(Chalk.Type type, Block customSymbol) {
            this.symbol = customSymbol;
            this.chalk = new Chalk(type);

            ro = ModRegistry.MDR.register("symbol_"+type.getName(),() -> symbol, ForgeRegistries.BLOCKS);
            ModRegistry.MDR.register("chalk_"+type.getName(),() -> chalk, ForgeRegistries.ITEMS);
        }

        public Block getSymbol() {
            return symbol;
        }

        public Item getChalk() {
            return chalk;
        }
    }
}
