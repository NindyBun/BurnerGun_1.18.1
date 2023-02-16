package com.nindybun.burnergun.client;

import com.nindybun.burnergun.client.screens.ModScreens;
import com.nindybun.burnergun.common.items.abstractItems.AbstractBurnerGun;
import com.nindybun.burnergun.common.items.abstractItems.AbstractBurnerSword;
import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2;
import com.nindybun.burnergun.common.network.PacketHandler;
import com.nindybun.burnergun.common.network.packets.PacketOpenBurnerGunGui;
import com.nindybun.burnergun.common.network.packets.PacketSpawnLightAtPlayer;
import com.nindybun.burnergun.common.network.packets.PacketSpawnLightAtRaycast;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class KeyInputHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event)
    {
        Player player = Minecraft.getInstance().player;
        if (Minecraft.getInstance().screen == null){
            if (Keybinds.burnergun_light_key.isDown() && event.getAction() == 1)
                PacketHandler.sendToServer(new PacketSpawnLightAtRaycast());
            if (Keybinds.burnergun_lightPlayer_key.isDown() && event.getAction() == 1)
                PacketHandler.sendToServer(new PacketSpawnLightAtPlayer());
            if (Keybinds.burnergun_gui_key.isDown() && event.getAction() == 1
                    && (player.getMainHandItem().getItem() instanceof AbstractBurnerGun || player.getOffhandItem().getItem() instanceof AbstractBurnerGun
                    || player.getMainHandItem().getItem() instanceof AbstractBurnerSword || player.getOffhandItem().getItem() instanceof AbstractBurnerSword)){
                PacketHandler.sendToServer(new PacketOpenBurnerGunGui());
            }
        }

    }


}
