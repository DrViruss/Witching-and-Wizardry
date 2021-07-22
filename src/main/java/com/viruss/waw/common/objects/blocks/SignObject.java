package com.viruss.waw.common.objects.blocks;

import com.viruss.waw.utils.RegistryHandler;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SignItem;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class SignObject {
    private final Block sign;
    private final Block wall_sign;
    private final Item item;

    public SignObject(String name, WoodType wood, ItemGroup group) {
        this.sign = new AbstractStandingSign(wood);
        this.wall_sign = new AbstractWallSign(wood,sign);
        this.item = new SignItem((new Item.Properties()).stacksTo(16).tab(group), sign, wall_sign);

        RegistryHandler.MDR.register(name+"_sign",() ->item,ForgeRegistries.ITEMS);
        RegistryHandler.MDR.register(name+"_sign",() ->sign,ForgeRegistries.BLOCKS);
        RegistryHandler.MDR.register(name+"_wall_sign",() ->wall_sign,ForgeRegistries.BLOCKS);
    }

    public Block getSign() {
        return sign;
    }

    public Block getWallSign() {
        return wall_sign;
    }

    public Item getItem() {
        return item;
    }



            /*~  ~Additional Classes~  ~*/

    public static class AbstractSignTileEntity extends SignTileEntity {
        private final TileEntityType<?> type;

        public AbstractSignTileEntity() {
            this.type = RegistryHandler.SIGN_TE;
        }

        @Override
        public TileEntityType<?> getType() {
            return type;
        }
    }

    public static class AbstractStandingSign extends StandingSignBlock {

        public AbstractStandingSign(WoodType woodType) {
            super(AbstractBlock.Properties.of(Material.WOOD).noCollission().strength(1.0F).sound(SoundType.WOOD), woodType);
        }

        @Override
        public boolean hasTileEntity(BlockState state) {
            return true;
        }

        @Nullable
        @Override
        public TileEntity createTileEntity(BlockState state, IBlockReader world) {
            return new AbstractSignTileEntity();
        }
    }

    public static class AbstractWallSign extends WallSignBlock {

        public AbstractWallSign(WoodType p_i225766_2_, Block sign) {
            super(AbstractBlock.Properties.of(Material.WOOD).noCollission().strength(1.0F).sound(SoundType.WOOD).lootFrom(()->sign), p_i225766_2_);
        }

        @Override
        public boolean hasTileEntity(BlockState state) {
            return true;
        }

        @Nullable
        @Override
        public TileEntity createTileEntity(BlockState state, IBlockReader world) {
            return new AbstractSignTileEntity();
        }
    }
}
