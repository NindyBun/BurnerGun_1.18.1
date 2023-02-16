package com.nindybun.burnergun.client.screens;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.swing.text.JTextComponent;

public class ModScreens {
    public static void openGunSettingsScreen(ItemStack gun) { Minecraft.getInstance().setScreen(new burnergunSettingsScreen(gun)); }
    public static void openColorScreen(ItemStack gun){
        Minecraft.getInstance().setScreen(new colorScreen(gun));
    }
    public static void openTestScreen(KeyMapping key, Player player){ Minecraft.getInstance().setScreen(new testScreen(key, player));}
}
