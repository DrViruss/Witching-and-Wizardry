package com.viruss.waw;

import com.viruss.waw.client.RendererManager;
import com.viruss.waw.common.worldgen.Features;
import com.viruss.waw.utils.EventHandler;
import com.viruss.waw.utils.registries.ModRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Main.MOD_ID)
public class Main
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "waw";

    public Main() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::enqueueIMC);
        bus.addListener(this::processIMC);
        bus.addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.addListener(Features::onBiomeLoad);
        MinecraftForge.EVENT_BUS.register(new EventHandler());

        ModRegistry.init(bus);
    }

    public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModRegistry.INGREDIENTS.getCalcosvis());
        }
    };

    @OnlyIn(Dist.CLIENT)
    public static final RendererManager CLIENT_RENDERER = new RendererManager();

    private void doClientStuff(final FMLClientSetupEvent event) {
        CLIENT_RENDERER.init();

//        event.enqueueWork(()->{
//            Sheets.addWoodType(ModRegistry.ASH.getWoodType());
//            Sheets.addWoodType(ModRegistry.SAMBUCUS.getWoodType());
//        });



//        Minecraft.getInstance().getSkinManager().registerTexture(); ??
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo("waw", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
//        LOGGER.info("Got IMC {}", event.getIMCStream().
//                map(m->m.getMessageSupplier().get()).
//                collect(Collectors.toList()));
    }
}
