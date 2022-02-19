package com.viruss.waw.utils.registries;

import com.viruss.waw.Main;
import com.viruss.waw.common.objects.packs.*;
import com.viruss.waw.common.rituals.actions.AbstractRitualAction;
import com.viruss.waw.utils.recipes.RecipeTypes;
import com.viruss.waw.utils.registration.MultyDeferredRegister;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public class ModRegistry {
    public static final DeferredRegister<AbstractRitualAction> RITUAL_REGISTER = DeferredRegister.create(AbstractRitualAction.class, Main.MOD_ID);

    public static final MultyDeferredRegister MDR = new MultyDeferredRegister(Main.MOD_ID,new IForgeRegistry[]{
            ForgeRegistries.BLOCKS,
            ForgeRegistries.ITEMS,
            ForgeRegistries.ENTITIES,
            ForgeRegistries.BLOCK_ENTITIES,
            ForgeRegistries.SOUND_EVENTS,
    });

    public static void init(IEventBus bus) {
        RITUAL_REGISTER.makeRegistry("ritual_registry", RegistryBuilder::new); // => IForgeRegistry<AbstractRitualAction>
        RITUAL_REGISTER.register(bus);
        MDR.register(bus);
        bus.addGenericListener(RecipeSerializer.class, RecipeTypes::registerRecipes);
    }

    public static final Wood WOOD = new Wood();
    public static final Gadgets GADGETS = new Gadgets();
    public static final ChalkSet CHALKS = new ChalkSet();
    public static final EntityRegistry ENTITIES = new EntityRegistry();
    public static final Ingredients INGREDIENTS = new Ingredients();
    public static final Rituals RITUALS = new Rituals();

}
