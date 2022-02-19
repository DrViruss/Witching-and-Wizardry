package com.viruss.waw.common.objects.packs;

import com.viruss.waw.common.objects.items.CircleTalisman;
import com.viruss.waw.common.rituals.actions.AbstractRitualAction;
import com.viruss.waw.common.rituals.actions.BasicWeatherAction;
import com.viruss.waw.common.rituals.actions.MidnightAction;
import com.viruss.waw.common.rituals.actions.NoonAction;
import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

public class Rituals {

       public Rituals() {
       }

       public static final RegistryObject<Item> TEST_TALISMAN = ModRegistry.MDR.register("test_talisman", CircleTalisman::new, ForgeRegistries.ITEMS);


       public static final RegistryObject<AbstractRitualAction> CLEAR = ModRegistry.RITUAL_REGISTER.register("clear_weather_action",() -> new BasicWeatherAction(ParticleTypes.FLAME, (world) ->{
              ((ServerLevel)world).setWeatherParameters(0,6000,false,false);
       }));

       public static final RegistryObject<AbstractRitualAction> RAIN = ModRegistry.RITUAL_REGISTER.register("rain_action",() -> new BasicWeatherAction(ParticleTypes.RAIN, (world) ->{
              ((ServerLevel)world).setWeatherParameters(0,6000,true,false);
       }));

       public static final RegistryObject<AbstractRitualAction> THUNDER = ModRegistry.RITUAL_REGISTER.register("thunder_action",() -> new BasicWeatherAction(ParticleTypes.ELECTRIC_SPARK, (world) ->{
              ((ServerLevel)world).setWeatherParameters(0,6000,true,true);
       }));

       public static final RegistryObject<AbstractRitualAction> NOON = ModRegistry.RITUAL_REGISTER.register("noon_action",() -> new NoonAction(ParticleTypes.GLOW));

       public static final RegistryObject<AbstractRitualAction> MIDNIGHT = ModRegistry.RITUAL_REGISTER.register("midnight_action",() -> new MidnightAction(ParticleTypes.ASH));

}
