package com.nindybun.burnergun.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.nindybun.burnergun.BurnerGun;
import com.nindybun.burnergun.client.screens.ModScreens;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import org.lwjgl.glfw.GLFW;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.net.ssl.KeyManager;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = BurnerGun.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvents {

    private static boolean keyWasDown = false;

    @SubscribeEvent
    public static void handleKeys(TickEvent.ClientTickEvent event){
        if (event.phase != TickEvent.Phase.START)
            return;

        if (Minecraft.getInstance().screen == null){
            boolean keyIsDown = Keybinds.burnergun_testScreen_key.isDown();
            if (keyIsDown && !keyWasDown){
                while (Keybinds.burnergun_testScreen_key.consumeClick()) {
                    if (Minecraft.getInstance().screen == null){
                        ItemStack hand = Minecraft.getInstance().player.getMainHandItem();
                        ModScreens.openTestScreen(Keybinds.burnergun_testScreen_key);
                    }
                }
            }
            keyWasDown = keyIsDown;
        }else{
            keyWasDown = true;
        }

    }

    public static boolean isKeyDown(KeyMapping keybind){
        if (keybind.isUnbound())
            return false;
        boolean isDown = switch (keybind.getKey().getType()){
            case KEYSYM -> InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), keybind.getKey().getValue());
            case MOUSE -> GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), keybind.getKey().getValue()) == GLFW.GLFW_PRESS;
            default -> false;
        };
        return isDown && keybind.getKeyConflictContext().isActive() && keybind.getKeyModifier().isActive(keybind.getKeyConflictContext());
    }

    public static void wipeOpen(){
        while(Keybinds.burnergun_testScreen_key.consumeClick()){

        }
    }
}
