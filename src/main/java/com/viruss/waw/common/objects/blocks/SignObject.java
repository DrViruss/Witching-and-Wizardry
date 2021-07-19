package com.viruss.waw.common.objects.blocks;

import com.viruss.waw.common.objects.blocks.bases.AbstractSignTileEntity;
import com.viruss.waw.common.objects.blocks.bases.AbstractStandingSign;
import com.viruss.waw.common.objects.blocks.bases.AbstractWallSign;
import com.viruss.waw.utils.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.WoodType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SignItem;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ForgeRegistries;

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

}
