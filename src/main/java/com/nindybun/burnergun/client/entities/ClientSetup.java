package com.nindybun.burnergun.client.entities;

import com.nindybun.burnergun.BurnerGun;
import com.nindybun.burnergun.client.entities.testArrowEntity.TestArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = BurnerGun.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void doSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntities.TEST_ARROW_ENTITY.get(), TestArrowRenderer::new);
    }
}
