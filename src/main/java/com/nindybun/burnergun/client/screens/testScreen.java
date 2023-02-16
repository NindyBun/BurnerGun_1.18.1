package com.nindybun.burnergun.client.screens;

import com.ibm.icu.number.Precision;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.nindybun.burnergun.BurnerGun;
import com.nindybun.burnergun.client.ClientEvents;
import com.nindybun.burnergun.client.KeyInputHandler;
import com.nindybun.burnergun.client.Keybinds;
import com.nindybun.burnergun.util.StringUtil;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.VanillaGameEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MathUtil;

import javax.swing.text.JTextComponent;
import java.awt.*;

public class testScreen extends Screen {
    private KeyMapping key;
    private int selected;

    protected testScreen(KeyMapping key) {
        super(new TextComponent("Title"));
        this.key = key;
        this.selected = -1;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float ticks_) {
        super.render(matrixStack, mouseX, mouseY, ticks_);
        int numberOfSlices = 12;

        float radiusIn = 30;
        float radiusOut = radiusIn * 2;
        int x = width / 2;
        int y = height / 2;

        matrixStack.pushPose();
        matrixStack.translate(0, 0, 0);

        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        boolean hasMouseOver = false;

        int numberOfRings = (int)Math.ceil(numberOfSlices/9.0D);
        for (int i = 0; i < numberOfRings; i++) {
            int slices = i < numberOfRings-1 ? 9 : numberOfSlices%9 == 0 ? 9 : numberOfSlices%9;
            for (int j = 0; j < slices; j++){
                float s0 = (((0 - 0.5f) / (float) slices) + 0.25f) * 360;
                double angle = Math.toDegrees(Math.atan2(mouseY - y, mouseX - x)); //Angle the mouse makes with the screen's equator
                double distance = Math.sqrt(Math.pow(mouseX - x, 2) + Math.pow(mouseY - y, 2)); //Distance of the mouse from the center of the screen
                if (angle < s0) {
                    angle += 360;
                }
                float start = (((j - 0.5f) / (float) slices) + 0.25f) * 360;
                float end = (((j + 0.5f) / (float) slices) + 0.25f) * 360;
                float addRadius = (radiusIn+5)*i;
                if (angle >= start && angle < end && distance >= (radiusIn+addRadius) && distance < (radiusOut+addRadius)) {
                    selected = j+(i*9);
                    BurnerGun.LOGGER.info(selected);
                    break;
                }
            }
        }

        for (int i = 0; i < numberOfRings; i++) {
            int slices = i < numberOfRings-1 ? 9 : numberOfSlices%9 == 0 ? 9 : numberOfSlices%9;
            for (int j = 0; j < slices; j++){
                float start = (((j - 0.5f) / (float) slices) + 0.25f) * 360;
                float end = (((j + 0.5f) / (float) slices) + 0.25f) * 360;
                float addRadius = (radiusIn+5)*i;
                if (selected == j+(i*9)){
                    drawPieArc(buffer, x, y, 0, radiusIn+addRadius, radiusOut+addRadius, start, end, 255, 255, 255, 64);
                    hasMouseOver = true;
                }else{
                    drawPieArc(buffer, x, y, 0, radiusIn+addRadius, radiusOut+addRadius, start, end, 0, 0, 0, 64);
                }
            }
        }

        /*for (int i = 0; i < numberOfSlices; i++) {
            float s = (((i - 0.5f) / (float) numberOfSlices) + 0.25f) * 360;
            float e = (((i + 0.5f) / (float) numberOfSlices) + 0.25f) * 360;
            if (a >= s && a < e && d >= (radiusIn) && d < (radiusOut)) {
                selected = i;
                break;
            }
        }

        for (int i = 0; i < numberOfSlices; i++){
            float s = (((i - 0.5f) / (float) numberOfSlices) + 0.25f) * 360;
            float e = (((i + 0.5f) / (float) numberOfSlices) + 0.25f) * 360;
            if (selected == i){
                drawPieArc(buffer, x, y, 0, radiusIn, radiusOut, s, e, 255, 255, 255, 64);
                hasMouseOver = true;
            }else{
                drawPieArc(buffer, x, y, 0, radiusIn, radiusOut, s, e, 0, 0, 0, 64);
            }
        }*/

        tesselator.end();
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();

        matrixStack.popPose();

    }

    public void drawPieArc(BufferBuilder buffer, float x, float y, float z, float radiusIn, float radiusOut, float startAngle, float endAngle, int r, int g, int b, int a){
        float angle = endAngle - startAngle;
        int sections = (int)Math.max(1, Math.nextUp(angle / 5.0F));

        startAngle = (float) Math.toRadians(startAngle);
        endAngle = (float) Math.toRadians(endAngle);
        angle = endAngle - startAngle;

        for (int i = 0; i < sections; i++)
        {
            float angle1 = startAngle + (i / (float) sections) * angle;
            float angle2 = startAngle + ((i + 1) / (float) sections) * angle;

            float pos1InX = x + radiusIn * (float) Math.cos(angle1);
            float pos1InY = y + radiusIn * (float) Math.sin(angle1);
            float pos1OutX = x + radiusOut * (float) Math.cos(angle1);
            float pos1OutY = y + radiusOut * (float) Math.sin(angle1);
            float pos2OutX = x + radiusOut * (float) Math.cos(angle2);
            float pos2OutY = y + radiusOut * (float) Math.sin(angle2);
            float pos2InX = x + radiusIn * (float) Math.cos(angle2);
            float pos2InY = y + radiusIn * (float) Math.sin(angle2);

            buffer.vertex(pos1OutX, pos1OutY, z).color(r, g, b, a).endVertex();
            buffer.vertex(pos1InX, pos1InY, z).color(r, g, b, a).endVertex();
            buffer.vertex(pos2InX, pos2InY, z).color(r, g, b, a).endVertex();
            buffer.vertex(pos2OutX, pos2OutY, z).color(r, g, b, a).endVertex();
        }
    }

    @SubscribeEvent
    public static void overlayEvent(RenderGameOverlayEvent.Pre event){
        if (Minecraft.getInstance().screen instanceof testScreen) {
            if (event.getType() == RenderGameOverlayEvent.ElementType.LAYER){
                event.setCanceled(true);
            }
        }
    }

    @Override
    public void removed() {
        super.removed();
        ClientEvents.wipeOpen();
    }

    @Override
    public void tick() {
        super.tick();
        if (!ClientEvents.isKeyDown(key)){
            Minecraft.getInstance().setScreen(null);
            ClientEvents.wipeOpen();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
