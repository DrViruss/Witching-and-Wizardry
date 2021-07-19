package com.viruss.waw.utils.registrations;

import com.viruss.waw.WitchingAndWizardry;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;

import javax.annotation.Nullable;

public class Item extends net.minecraft.item.Item
{
    public Item(ItemGroup itemGroup)
    {
        super(new net.minecraft.item.Item.Properties().tab(itemGroup));
    }

    public Item() { super(new Properties().tab(WitchingAndWizardry.ITEM_GROUP)); }

    public Item(Properties props)
    {
        super(props);
    }

    public static class Builder //TODO: Make this thing more useful
    {
        @Nullable
        private ItemGroup itemGroup;
        @Nullable
        private Block block;

        public Item.Builder itemGroup(ItemGroup itemGroup)
        {
            this.itemGroup = itemGroup;
            return this;
        }
        public Item.Builder block(Block block)
        {
            this.block = block;
            return this;
        }

        public net.minecraft.item.Item build()
        {
            if(block == null)
            {
                if (itemGroup == null)
                    return new net.minecraft.item.Item(new Properties().tab(ItemGroup.TAB_MISC));
                return new net.minecraft.item.Item(new Properties().tab(itemGroup));
            }
            return new BlockItem(this.block,new Properties().tab(itemGroup));
        }
    }
}
