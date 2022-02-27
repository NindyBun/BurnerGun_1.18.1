package com.nindybun.burnergun.common;

import com.nindybun.burnergun.client.ClientSetup;
import com.nindybun.burnergun.client.KeyInputHandler;
import com.nindybun.burnergun.client.Keybinds;
import com.nindybun.burnergun.client.particles.ModParticles;
import com.nindybun.burnergun.common.blocks.ModBlocks;
import com.nindybun.burnergun.common.containers.ModContainers;
import com.nindybun.burnergun.common.items.ModItems;
import com.nindybun.burnergun.common.network.PacketHandler;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(BurnerGun.MOD_ID)
public class BurnerGun{

    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "burnergun";

    public BurnerGun()
    {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModItems.UPGRADE_ITEMS.register(modEventBus);
        ModContainers.CONTAINERS.register(modEventBus);
        ModParticles.PARTICLE.register(modEventBus);

        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::setupClient);

        MinecraftForge.EVENT_BUS.register(this);

    }

    private void setup(final FMLCommonSetupEvent event)
    {
        PacketHandler.register();
        //CapabilityManager.INSTANCE.register(BurnerGunMK1Info.class, new BurnerGunMK1InfoStorage(), BurnerGunMK1InfoProvider::new);
        //CapabilityManager.INSTANCE.register(BurnerGunMK2Info.class, new BurnerGunMK2InfoStorage(), BurnerGunMK2InfoProvider::new);
    }

    private void setupClient(final FMLClientSetupEvent event)
    {
        ClientSetup.setup();
        Keybinds.register();
        MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
        //RenderingRegistry.registerEntityRenderingHandler(ModEntities.MEGA_BLAZE_PROJECTILE.get(), MegaBlazeProjectileRenderer::new);
        //RenderingRegistry.registerEntityRenderingHandler(ModEntities.MEGA_BLAZE.get(), MegaBlazeRenderer::new);
    }


    public static CreativeModeTab itemGroup = new CreativeModeTab(BurnerGun.MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.BURNER_GUN_MK1.get());
        }
    };
}
