package com.viruss.waw.utils.registrations;

import com.viruss.waw.WitchingAndWizardry;
import com.viruss.waw.common.objects.blocks.WoodenObject;
import com.viruss.waw.common.objects.blocks.bases.*;
import net.minecraft.block.Block;
import net.minecraft.block.WoodType;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MultyDeferredRegister{
    private Map<IForgeRegistry<?>, DeferredRegister<? extends IForgeRegistryEntry<?>>> registerMap;

    public MultyDeferredRegister(IForgeRegistry<?>[] type) {
        this.registerMap = new HashMap<>(type.length);
        for (int i = 0; i < type.length; i++)
            registerMap.put(type[i], DeferredRegister.create(type[i], WitchingAndWizardry.MOD_ID));
    }

    public void register(IEventBus bus) {
        registerMap.forEach((key, value) -> value.register(bus));
    }

    //TODO: Remove type
    public <I extends T, T extends IForgeRegistryEntry<T>> RegistryObject<I> register(String name, Supplier<? extends I> supplier, IForgeRegistry<T> type) {
        DeferredRegister<T> dregister = ((DeferredRegister<T>) registerMap.get(type));
        if(dregister == null)
            throw new RuntimeException("MDR cant find "+type+" register! Check your RegistryHandler class!");
        return dregister.register(name, supplier);
    }

    public DoubleRegisteredObject<Block, Item> register(String name, com.viruss.waw.utils.registrations.Block.Builder blockBuilder)
    {
        RegistryObject<Block> o1 = ((DeferredRegister<Block>) registerMap.get(ForgeRegistries.BLOCKS)).register(name,blockBuilder.build());

        if(blockBuilder.isNeedItem()) {
            if (blockBuilder.getGroup() == null)
                return new DoubleRegisteredObject<>(o1, ((DeferredRegister<Item>) registerMap.get(ForgeRegistries.ITEMS)).register(name, () -> new BlockItem(o1.get(), new Item.Properties().tab(ItemGroup.TAB_MISC))));
            return new DoubleRegisteredObject<>(o1, ((DeferredRegister<Item>) registerMap.get(ForgeRegistries.ITEMS)).register(name, () -> new com.viruss.waw.utils.registrations.Item.Builder().block(o1.get()).itemGroup(blockBuilder.getGroup()).build()));
        }
        return new DoubleRegisteredObject<>(o1,null);
    }
}
