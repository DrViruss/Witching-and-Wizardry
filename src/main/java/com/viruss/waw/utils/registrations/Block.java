package com.viruss.waw.utils.registrations;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;

import javax.annotation.Nullable;
import java.util.function.Function;
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

        @Nullable
        private Function<Properties,? extends net.minecraft.block.Block> block;
        @Nullable
        private Supplier<? extends net.minecraft.block.Block> block_supplier;

        private boolean needItem;
        @Nullable
        private ItemGroup group;

        public Block.Builder withProperties(net.minecraft.block.Block.Properties properties) {
            this.properties = properties;
            return this;
        }
        public <R extends net.minecraft.block.Block> Block.Builder setBlock(Function<Properties,R>  blockConstructor) {
            this.block = blockConstructor;
            return this;
        }

        public <R extends net.minecraft.block.Block> Block.Builder setBlockSup(Supplier<R>  blockConstructor) {
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

        @Nullable
        public Function<Properties,? extends net.minecraft.block.Block> getBlock() {
            return block;
        }

        public boolean isNeedItem() {
            return needItem;
        }

        public Supplier<? extends net.minecraft.block.Block> build() {
            if(block == null && block_supplier ==null)
                return () -> new net.minecraft.block.Block(properties);
            if(block!= null)
                return () -> block.apply(properties);
            return block_supplier;
        }

        private static <T extends AbstractBlock.Properties, R extends net.minecraft.block.Block> Supplier<R> bind(Function<T,R> fn, T val) {
            return () -> fn.apply(val);
        }
    }
}
