package com.viruss.waw.utils.registration;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class Block extends net.minecraft.block.Block
{
    public Block(Properties props)
    {
        super(props);
    }

    public static class Builder
    {
        private net.minecraft.block.Block.Properties properties = AbstractBlock.Properties.of(Material.AIR);
        private Item.Properties itemProps = null;

        @Nullable
        private Supplier<net.minecraft.block.Block> block_supplier;

        private boolean needItem;
        @Nullable
        private ItemGroup group;

        public Block.Builder withProperties(net.minecraft.block.Block.Properties properties) {
            this.properties = properties;
            return this;
        }

        public Block.Builder setItemProps(Item.Properties itemProps) {
            this.itemProps = itemProps;
            return this;
        }

        public <R extends net.minecraft.block.Block> Block.Builder setBlockSup(Supplier<net.minecraft.block.Block>  blockConstructor) {
            this.block_supplier = blockConstructor;
            return this;
        }

        public Block.Builder needItem(ItemGroup group) {
            this.needItem = true;
            this.group = group;
            return this;
        }

        public Properties getProperties() {
            return properties;
        }

        @Nullable
        public ItemGroup getGroup() {
            return group;
        }


        public boolean isNeedItem() {
            return needItem;
        }

        public Supplier<net.minecraft.block.Block> build() {
            if(block_supplier ==null)
                return ()-> new net.minecraft.block.Block(properties);
            return block_supplier;
        }
    }
}
