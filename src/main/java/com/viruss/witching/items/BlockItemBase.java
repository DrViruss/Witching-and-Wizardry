package com.viruss.witching.items;

import com.viruss.witching.WMod;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

public class BlockItemBase extends BlockItem {
    public BlockItemBase(Block blockIn) {
        super(blockIn, new Properties().group(WMod.W_Tab));
    }
}
