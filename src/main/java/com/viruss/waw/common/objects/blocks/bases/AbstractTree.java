package com.viruss.waw.common.objects.blocks.bases;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import javax.annotation.Nullable;
import java.util.Random;

public class AbstractTree extends Tree {
    private final ConfiguredFeature<BaseTreeFeatureConfig, ?> treeFeature;

    public AbstractTree(ConfiguredFeature<BaseTreeFeatureConfig, ?> treeFeature) {
        this.treeFeature = treeFeature;
    }


    @Nullable
    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getConfiguredFeature(Random p_225546_1_, boolean p_225546_2_) {
        return treeFeature;
    }
}
