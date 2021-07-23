package com.viruss.waw.utils.registration;

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
        private ItemGroup itemGroup = ItemGroup.TAB_MISC;
        @Nullable
        private Block block;

        private net.minecraft.item.Item.Properties properties = new Properties();

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

        public Item.Builder setProperties(Properties properties) {
            this.properties = properties;
            return this;
        }

        public net.minecraft.item.Item build()
        {
            if(block == null)
                return new net.minecraft.item.Item(properties.tab(itemGroup));
            return new BlockItem(this.block,new Properties().tab(itemGroup));
        }
    }
}
