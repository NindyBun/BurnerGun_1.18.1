package com.nindybun.burnergun.client;

import com.mojang.blaze3d.platform.ScreenManager;
import com.nindybun.burnergun.common.BurnerGun;
import com.nindybun.burnergun.common.containers.ModContainers;
import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1Screen;
import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2Screen;
import com.nindybun.burnergun.common.items.upgrades.Auto_Smelt.AutoSmeltScreen;
import com.nindybun.burnergun.common.items.upgrades.Trash.TrashScreen;
import net.minecraft.client.gui.screens.MenuScreens;

public class ClientSetup {
    public static void setup(){
        MenuScreens.register(ModContainers.BURNERGUNMK1_CONTAINER.get(), BurnerGunMK1Screen::new);
        MenuScreens.register(ModContainers.BURNERGUNMK2_CONTAINER.get(), BurnerGunMK2Screen::new);
        MenuScreens.register(ModContainers.TRASH_CONTAINER.get(), TrashScreen::new);
        MenuScreens.register(ModContainers.AUTO_SMELT_CONTAINER.get(), AutoSmeltScreen::new);
    }

}
