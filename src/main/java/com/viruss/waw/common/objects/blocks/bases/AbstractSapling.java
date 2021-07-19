package com.viruss.waw.common.objects.blocks.bases;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.trees.Tree;

public class AbstractSapling extends SaplingBlock {
    public AbstractSapling(Tree p_i48337_1_) {
        super(p_i48337_1_, AbstractBlock.Properties.of(Material.PLANT).instabreak().sound(SoundType.CROP).randomTicks().noCollission());
    }
}
