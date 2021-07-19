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
    private final TileEntityType<AbstractSignTileEntity> tile;
    private final Block sign;
    private final Block wall_sign;
    private final Item item;

    public SignObject(String name, WoodType wood, ItemGroup group) {
        this.sign = new AbstractStandingSign(wood,this);
        this.wall_sign = new AbstractWallSign(wood,this);
        this.tile = TileEntityType.Builder.of(()-> new AbstractSignTileEntity(this),sign,wall_sign).build(null);
        this.item = new SignItem((new Item.Properties()).stacksTo(16).tab(group), sign, wall_sign);

        RegistryHandler.MDR.register(name+"_sign_tile",() -> tile,ForgeRegistries.TILE_ENTITIES);
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

    public TileEntityType<AbstractSignTileEntity> getTile() {
        return tile;
    }



            /*~  ~Additional Classes~  ~*/

    public static class AbstractSignTileEntity extends SignTileEntity {
        private final TileEntityType<?> signObject;
        public AbstractSignTileEntity(SignObject  signObject) {
            this.signObject = signObject.getTile();
        }
        public AbstractSignTileEntity(TileEntityType<?>  type) {
            this.signObject = type;
        }



        @Override
        public TileEntityType<?> getType() {
            return signObject;
        }
    }

    public static class AbstractStandingSign extends StandingSignBlock {
        private final SignObject signObject;

        public AbstractStandingSign(WoodType woodType, SignObject signObject) {
            super(AbstractBlock.Properties.of(Material.WOOD).noCollission().strength(1.0F).sound(SoundType.WOOD), woodType);
            this.signObject = signObject;
        }

        @Override
        public boolean hasTileEntity(BlockState state) {
            return true;
        }

        @Nullable
        @Override
        public TileEntity createTileEntity(BlockState state, IBlockReader world) {
            return new AbstractSignTileEntity(signObject.getTile());
        }
    }

    public static class AbstractWallSign extends WallSignBlock {
        private final SignObject signObject;

        public AbstractWallSign(WoodType p_i225766_2_,SignObject signObject) {
            super(AbstractBlock.Properties.of(Material.WOOD).noCollission().strength(1.0F).sound(SoundType.WOOD).lootFrom(signObject::getSign), p_i225766_2_);
            this.signObject = signObject;
        }

        @Override
        public boolean hasTileEntity(BlockState state) {
            return true;
        }

        @Nullable
        @Override
        public TileEntity createTileEntity(BlockState state, IBlockReader world) {
            return new AbstractSignTileEntity(signObject.getTile());
        }
    }
}
