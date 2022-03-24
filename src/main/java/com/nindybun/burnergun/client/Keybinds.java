package com.nindybun.burnergun.client;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import org.lwjgl.glfw.GLFW;


public class Keybinds {

    public static KeyMapping burnergun_gui_key = new KeyMapping("key.burnergun_gui_key", GLFW.GLFW_KEY_C, "key.categories.burnergun");
    public static KeyMapping burnergun_light_key = new KeyMapping("key.burnergun_light_key", GLFW.GLFW_KEY_R, "key.categories.burnergun");
    public static KeyMapping burnergun_lightPlayer_key = new KeyMapping("key.burnergun_lightplayer_key", GLFW.GLFW_KEY_G, "key.categories.burnergun");
    public static KeyMapping burnergun_veinMiner_key = new KeyMapping("key.burnergun_veinminer_key", GLFW.GLFW_KEY_LEFT_ALT, "key.categories.burnergun");

    public static void register()
    {
        ClientRegistry.registerKeyBinding(burnergun_gui_key);
        ClientRegistry.registerKeyBinding(burnergun_light_key);
        ClientRegistry.registerKeyBinding(burnergun_lightPlayer_key);
        ClientRegistry.registerKeyBinding(burnergun_veinMiner_key);
    }


}
