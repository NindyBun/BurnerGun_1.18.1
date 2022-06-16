package com.nindybun.burnergun.client.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

public class ModScreens {
    public static void openGunSettingsScreen(ItemStack gun) { Minecraft.getInstance().setScreen(new burnergunSettingsScreen(gun)); }
    public static void openColorScreen(ItemStack gun){
        Minecraft.getInstance().setScreen(new colorScreen(gun));
    }
    public static void openUtilInventorScreen(ItemStack utilInv) {
        Minecraft.getInstance().setScreen(new utilInventoryScreen(utilInv));
    }
}
