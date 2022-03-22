package com.nindybun.burnergun.client.screens;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.nindybun.burnergun.common.BurnerGun;
import com.nindybun.burnergun.util.StringUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public class FilterListScreen extends Screen {
    protected FilterListScreen(ItemStack stack) {
        super(new TextComponent("Title"));
    }
    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
        InputConstants.Key key = InputConstants.getKey(p_231046_1_, p_231046_2_);
        if (p_231046_1_ == 256 || minecraft.options.keyInventory.isActiveAndMatches(key)) {
            onClose();
            return true;
        }
        return super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float ticks_) {
        //Gives us the darkened background
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, ticks_);
        TranslatableComponent string = new TranslatableComponent("tooltip." + BurnerGun.MOD_ID + ".screen.filter");
        drawString(matrixStack, Minecraft.getInstance().font, string, (width/2)- StringUtil.getStringPixelLength(string.getString())/2, 20, Color.WHITE.getRGB());
        }
}
