package com.viruss.waw.common.objects.packs;

import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("all")
public class SignPack {
    private final Block sign;
    private final Block wall_sign;
    private final Item item;

    public SignPack(String name, WoodType wood, CreativeModeTab group) {
        this.sign = new AbstractStandingSign(wood);
        this.wall_sign = new AbstractWallSign(wood,sign);
        this.item = new SignItem((new Item.Properties()).stacksTo(16).tab(group), sign, wall_sign);

        ModRegistry.MDR.register(name+"_sign",() ->item, ForgeRegistries.ITEMS);
        ModRegistry.MDR.register(name+"_sign",() ->sign,ForgeRegistries.BLOCKS);
        ModRegistry.MDR.register(name+"_wall_sign",() ->wall_sign,ForgeRegistries.BLOCKS);
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

    public static class AbstractSignTileEntity extends SignBlockEntity {
        private final BlockEntityType<?> type;

        public AbstractSignTileEntity(BlockPos pos, BlockState state) {
            super(pos,state);
            this.type = ModRegistry.WOOD.getSignTileEntity();
        }

        @Override
        public BlockEntityType<?> getType() {
            return type;
        }
    }
    public static class AbstractStandingSign extends StandingSignBlock {

        public AbstractStandingSign(WoodType woodType) {
            super(Properties.of(Material.WOOD).noCollission().strength(1.0F).sound(SoundType.WOOD), woodType);
        }

        @Override
        public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
            return new AbstractSignTileEntity(pos,state);
        }
    }
    public static class AbstractWallSign extends WallSignBlock {

        public AbstractWallSign(WoodType woodType, Block sign) {
            super(Properties.of(Material.WOOD).noCollission().strength(1.0F).sound(SoundType.WOOD).lootFrom(()->sign), woodType);
        }

        @Override
        public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
            return new AbstractSignTileEntity(pos,state);
        }
    }
}
