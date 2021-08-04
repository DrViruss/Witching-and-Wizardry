package com.viruss.waw.utils.datagen;

import com.viruss.waw.Main;
import com.viruss.waw.common.objects.packs.WoodenPack;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.Set;

public class ItemTagProvider extends ItemTagsProvider {
    private final Set<WoodenPack> woods;
    public ItemTagProvider(DataGenerator p_126530_, @Nullable ExistingFileHelper existingFileHelper,Set<WoodenPack> woods) {
        super(p_126530_, new BlockTagProvider(p_126530_,existingFileHelper,woods), Main.MOD_ID, existingFileHelper);
        this.woods = woods;
    }

    @Override
    protected void addTags() {
        registerWoods();
    }

    private void registerWoods() {
        for (WoodenPack wood : woods){

            if(wood.getStrippedLog() == null)
                tag(wood.getLogsItemTag()).add(wood.getLog().getSecondary()).add(wood.getWood().getSecondary());
            else
                tag(wood.getLogsItemTag()).add(wood.getLog().getSecondary()).add(wood.getStrippedWood().getSecondary())
                        .add(wood.getStrippedLog().getSecondary()).add(wood.getWood().getSecondary());

            tag(ItemTags.WOODEN_TRAPDOORS).add(wood.getTrapdoor().getSecondary());
            tag(ItemTags.WOODEN_DOORS).add(wood.getDoor().getSecondary());
            tag(ItemTags.WOODEN_FENCES).add(wood.getFence().getSecondary());
            tag(ItemTags.LOGS_THAT_BURN).addTag(wood.getLogsItemTag());
            tag(ItemTags.PLANKS).add(wood.getPlanks().getSecondary());
            tag(ItemTags.WOODEN_PRESSURE_PLATES).add(wood.getPressurePlate().getSecondary());
            tag(ItemTags.WOODEN_SLABS).add(wood.getSlab().getSecondary());
            tag(ItemTags.WOODEN_STAIRS).add(wood.getStairs().getSecondary());
            tag(ItemTags.WOODEN_BUTTONS).add(wood.getButton().getSecondary());
            tag(ItemTags.SIGNS).add(wood.getSign().getItem());
            tag(ItemTags.BOATS).add(wood.getBoat().get());

            tag(Tags.Items.FENCE_GATES_WOODEN).add(wood.getGate().getSecondary());
            tag(Tags.Items.FENCES_WOODEN).add(wood.getFence().getSecondary());

            if(wood.getSapling()!= null){
                tag(ItemTags.SAPLINGS).add(wood.getSapling().getSecondary());
                tag(ItemTags.LEAVES).add(wood.getSapling().getSecondary());
            }
        }
    }
}
