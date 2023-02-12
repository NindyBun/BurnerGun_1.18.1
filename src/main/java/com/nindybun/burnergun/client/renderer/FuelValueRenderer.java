package com.nindybun.burnergun.client.renderer;

import com.nindybun.burnergun.BurnerGun;
import com.nindybun.burnergun.common.items.BurnerGunNBT;
import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.burnerswordmk1.BurnerSwordMK1;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.awt.*;

@Mod.EventBusSubscriber(modid = BurnerGun.MOD_ID, value = Dist.CLIENT)
public class FuelValueRenderer {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final double base_buffer = BurnerGunMK1.base_use_buffer;

    @SubscribeEvent
    public static void renderOverlay(@Nonnull RenderGameOverlayEvent.Post event){
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL){
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            ItemStack stack = BurnerGunMK1.getGun(player);
            if (stack.isEmpty()){
                stack = BurnerSwordMK1.getSword(player);
                if (stack.isEmpty())
                    return;
            }
            if (!stack.isEmpty())
                renderFuel(event, stack);
        }
    }

    public static void renderFuel(RenderGameOverlayEvent.Post event, ItemStack gun){
        Font fontRenderer = Minecraft.getInstance().font;
        int level = (int) BurnerGunNBT.getFuelValue(gun);
        Color color;
        if (level > base_buffer*3/4)
                color = Color.GREEN;
        else if (level > base_buffer*1/4 && level <= base_buffer*3/4)
            color = Color.ORANGE;
        else
            color = Color.RED;
        fontRenderer.draw(event.getMatrixStack(), "Fuel level: ", 6, event.getWindow().getGuiScaledHeight()-12, Color.WHITE.getRGB());
        fontRenderer.draw(event.getMatrixStack(), level+"", 61, event.getWindow().getGuiScaledHeight()-12, color.getRGB());
    }
}
