package com.viruss.waw.utils.datagen;

import com.viruss.waw.WitchingAndWizardry;
import com.viruss.waw.common.objects.packs.WoodenObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.Set;

public class ItemTagProvider extends ItemTagsProvider {
    private final Set<WoodenObject> woodenObjects;

    public ItemTagProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper,Set<WoodenObject> woods) {
        super(generator, new BlockTagProvider(generator,existingFileHelper,woods), WitchingAndWizardry.MOD_ID, existingFileHelper);
        this.woodenObjects = woods;
    }

    @Override
    protected void addTags() {
        for (WoodenObject wood : woodenObjects)
            addWoodTags(wood);

    }
    
    private void addWoodTags(WoodenObject wood){
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
        tag(ItemTags.WOODEN_PRESSURE_PLATES).add(wood.getPressure_plate().getSecondary());
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
