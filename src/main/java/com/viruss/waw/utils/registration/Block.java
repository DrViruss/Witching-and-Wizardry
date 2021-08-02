package com.viruss.waw.utils.registration;


import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class Block extends net.minecraft.world.level.block.Block
{
    public Block(BlockBehaviour.Properties props)
    {
        super(props);
    }

    public static class Builder
    {
        private BlockBehaviour.Properties properties = BlockBehaviour.Properties.of(Material.AIR);

        @Nullable
        private Supplier<net.minecraft.world.level.block.Block> block_supplier;

        private boolean needItem;
        @Nullable
        private CreativeModeTab group;

        public Block.Builder withProperties(Properties properties) {
            this.properties = properties;
            return this;
        }

        public Block.Builder setBlockSup(Supplier<net.minecraft.world.level.block.Block>  blockConstructor) {
            this.block_supplier = blockConstructor;
            return this;
        }

        public Block.Builder needItem(CreativeModeTab group) {
            this.needItem = true;
            this.group = group;
            return this;
        }

        public Properties getProperties() {
            return properties;
        }

        @Nullable
        public CreativeModeTab getGroup() {
            return group;
        }


        public boolean isNeedItem() {
            return needItem;
        }

        public Supplier<net.minecraft.world.level.block.Block> build() {
            if(block_supplier ==null)
                return ()-> new net.minecraft.world.level.block.Block(properties);
            return block_supplier;
        }
    }
}
