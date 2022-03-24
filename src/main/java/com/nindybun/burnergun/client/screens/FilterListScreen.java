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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import javax.swing.*;
import java.awt.*;

public class FilterListScreen extends Screen {
    private final static Logger LOGGER = LogManager.getLogger();
    private int red;
    protected FilterListScreen(ItemStack stack) {
        super(new TextComponent("Title"));
    }

    @Override
    protected void init() {
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean mouseScrolled(double scrollX, double scrollY, double scrollDelta) {
            red += (int)scrollDelta;
            if (red >= 200)
                red = 200;
            else if (red <= 10)
                red = 10;
        return super.mouseScrolled(scrollX, scrollY, scrollDelta);
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
        fill(matrixStack, 10, 10, 10+50, 10+200, Color.WHITE.hashCode());
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glTranslatef(0, red, 0);
        GL11.glScissor(10, 10+red, 60, 60);
        fill(matrixStack, 10, 10, 60, 10, new Color(1f, 0f, 0f).hashCode());
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();

//        TranslatableComponent string = new TranslatableComponent("tooltip." + BurnerGun.MOD_ID + ".screen.filter");
//        drawString(matrixStack, Minecraft.getInstance().font, string, (width / 2) - StringUtil.getStringPixelLength(string.getString()) / 2, 20, Color.WHITE.getRGB());
    }
}
