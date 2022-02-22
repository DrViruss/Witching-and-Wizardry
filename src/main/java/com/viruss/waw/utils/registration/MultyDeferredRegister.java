package com.viruss.waw.utils.registration;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("all")
public class MultyDeferredRegister{
    private final Map<IForgeRegistry<?>, DeferredRegister<? extends IForgeRegistryEntry<?>>> registerMap;
    private final String id;
    public MultyDeferredRegister(String mod_id,IForgeRegistry<?>[] type) {
        this.registerMap = new HashMap<>(type.length);
        id = mod_id;
        for (IForgeRegistry<?> objects : type) registerMap.put(objects, DeferredRegister.create(objects, id));
    }

    public void register(IEventBus bus) {
        registerMap.forEach((key, value) -> value.register(bus));
    }

    public <T extends IForgeRegistryEntry<T>> RegistryObject<T> register(String name, Supplier<T> supplier, IForgeRegistry<T> type) {
        DeferredRegister<T> dregister = ((DeferredRegister<T>) registerMap.get(type));
        if(dregister == null)
            throw new RuntimeException("MDR cant find "+type.getRegistryName().getPath()+" register! Check your RegistryHandler class!");
        return dregister.register(name, supplier);
    }

    public DoubleRegisteredObject<Block, Item> register(String name, com.viruss.waw.utils.registration.Block.Builder blockBuilder)
    {
        RegistryObject<Block> o1 = ((DeferredRegister<Block>) registerMap.get(ForgeRegistries.BLOCKS)).register(name,blockBuilder.build());

        if(blockBuilder.isNeedItem())
                return new DoubleRegisteredObject<>(o1, ((DeferredRegister<Item>) registerMap.get(ForgeRegistries.ITEMS)).register(name, () -> new BlockItem(o1.get(), new Item.Properties().tab(blockBuilder.getGroup()!= null ? blockBuilder.getGroup() : CreativeModeTab.TAB_MISC))));
        return new DoubleRegisteredObject<>(o1,null);
    }
}
