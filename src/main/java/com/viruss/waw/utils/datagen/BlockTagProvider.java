package com.viruss.waw.utils.datagen;

import com.viruss.waw.WitchingAndWizardry;
import com.viruss.waw.common.objects.packs.WoodenObject;
import com.viruss.waw.utils.ModRegistry;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class BlockTagProvider extends BlockTagsProvider {
    public BlockTagProvider(DataGenerator p_i48256_1_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_i48256_1_, WitchingAndWizardry.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        addWoodTags(ModRegistry.ASH);
        addWoodTags(ModRegistry.SAMBUCUS);

    }

    private void addWoodTags(WoodenObject wood){
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
        tag(BlockTags.WOODEN_PRESSURE_PLATES).add(wood.getPressure_plate().getPrimary());
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
