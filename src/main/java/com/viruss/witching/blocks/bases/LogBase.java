package com.viruss.witching.blocks.bases;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class LogBase extends RotatedPillarBlock {
    public LogBase() {
        super(AbstractBlock.Properties.create(Material.WOOD)
        .hardnessAndResistance(2f)
        .sound(SoundType.WOOD)
        .harvestLevel(0)
        .harvestTool(ToolType.AXE)
        );
    }

}
