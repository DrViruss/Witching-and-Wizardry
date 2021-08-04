package com.viruss.waw.utils.datagen;

import com.viruss.waw.Main;
import com.viruss.waw.common.objects.packs.WoodenPack;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.Set;

public class BlockTagProvider extends BlockTagsProvider {
    private final Set<WoodenPack> woods;
    public BlockTagProvider(DataGenerator p_126511_, @Nullable ExistingFileHelper existingFileHelper,Set<WoodenPack> woods) {
        super(p_126511_, Main.MOD_ID, existingFileHelper);
        this.woods = woods;
    }

    @Override
    protected void addTags() {
        registerWoods();
    }

    private void registerWoods() {
        for(WoodenPack wood : woods) {

            if(wood.getStrippedLog() == null)
                tag(wood.getLogsBlockTag()).add(wood.getLog().getPrimary()).add(wood.getWood().getPrimary());
            else
                tag(wood.getLogsBlockTag()).add(wood.getLog().getPrimary()).add(wood.getStrippedWood().getPrimary())
                        .add(wood.getStrippedLog().getPrimary()).add(wood.getWood().getPrimary());

            tag(BlockTags.WOODEN_TRAPDOORS).add(wood.getTrapdoor().getPrimary());
            tag(BlockTags.WOODEN_DOORS).add(wood.getDoor().getPrimary());
            tag(BlockTags.WOODEN_FENCES).add(wood.getFence().getPrimary());
            tag(BlockTags.LOGS_THAT_BURN).addTag(wood.getLogsBlockTag());
            tag(BlockTags.PLANKS).add(wood.getPlanks().getPrimary());
            tag(BlockTags.WOODEN_PRESSURE_PLATES).add(wood.getPressurePlate().getPrimary());
            tag(BlockTags.WOODEN_SLABS).add(wood.getSlab().getPrimary());
            tag(BlockTags.WOODEN_STAIRS).add(wood.getStairs().getPrimary());
            tag(BlockTags.WOODEN_BUTTONS).add(wood.getButton().getPrimary());
            tag(BlockTags.SIGNS).add(wood.getSign().getSign()).add(wood.getSign().getWallSign());

            tag(Tags.Blocks.FENCE_GATES_WOODEN).add(wood.getGate().getPrimary());
            tag(Tags.Blocks.FENCES_WOODEN).add(wood.getFence().getPrimary());

            if(wood.getSapling()!= null){
                tag(BlockTags.SAPLINGS).add(wood.getSapling().getPrimary());
                tag(BlockTags.LEAVES).add(wood.getSapling().getPrimary());
            }
        }
    }
}
