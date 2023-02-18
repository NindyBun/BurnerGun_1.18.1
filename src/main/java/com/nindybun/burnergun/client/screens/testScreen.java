package com.nindybun.burnergun.client.screens;

import com.ibm.icu.number.Precision;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.nindybun.burnergun.BurnerGun;
import com.nindybun.burnergun.client.ClientEvents;
import com.nindybun.burnergun.client.KeyInputHandler;
import com.nindybun.burnergun.client.Keybinds;
import com.nindybun.burnergun.common.items.testitems.TestItemBag;
import com.nindybun.burnergun.common.items.testitems.TestItemBagHandler;
import com.nindybun.burnergun.common.network.PacketHandler;
import com.nindybun.burnergun.common.network.packets.PacketSaveSelection;
import com.nindybun.burnergun.util.StringUtil;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.VanillaGameEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MathUtil;

import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class testScreen extends Screen {
    private KeyMapping key;
    private int selected;
    private Player player;
    private ItemStack tool;
    private List<ItemStack> containedItems = new ArrayList<ItemStack>();

    protected testScreen(KeyMapping key, Player player) {
        super(new TextComponent("Title"));
        this.key = key;
        this.player = player;
        this.tool = player.getMainHandItem();
        this.selected = -1;

    }

    @Override
    protected void init(){
        IItemHandler handler = tool.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        for (int i = 0; i < handler.getSlots(); i++){
            if (handler.getStackInSlot(i) != ItemStack.EMPTY){
                if (!doesContainInList(containedItems, handler.getStackInSlot(i)))
                    containedItems.add(handler.getStackInSlot(i));
            }
        }
    }

    public boolean doesContainInList(List<ItemStack> list, ItemStack itemStack){
        for (ItemStack stack : list){
            if (stack.equals(itemStack, false))
                return true;
    }
        return false;
    }

    @Override
    public boolean mouseReleased(double x, double y, int mouseButton) {
        processClick(true);
        return super.mouseReleased(x, y, mouseButton);
    }

    private void processClick(boolean mousePressed){
        processSlot();
    }

    private void processSlot(){
        if (selected != -1)
            PacketHandler.sendToServer(new PacketSaveSelection(containedItems.get(selected)));
        onClose();
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float ticks_) {
        super.render(matrixStack, mouseX, mouseY, ticks_);
        int numberOfSlices = containedItems.size();
        if (numberOfSlices == 0)
            return;

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
                }else{
                    drawPieArc(buffer, x, y, 0, radiusIn+addRadius, radiusOut+addRadius, start, end, 0, 0, 0, 64);
                }
            }
        }

        tesselator.end();
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        matrixStack.popPose();

        matrixStack.pushPose();
        PoseStack poseStack = RenderSystem.getModelViewStack();
        poseStack.pushPose();
        poseStack.mulPoseMatrix(matrixStack.last().pose());
        poseStack.translate(-8, -8, 0);
        RenderSystem.applyModelViewMatrix();
        for (int i = 0; i < numberOfRings; i++) {
            int slices = i < numberOfRings-1 ? 9 : numberOfSlices%9 == 0 ? 9 : numberOfSlices%9;
            for (int j = 0; j < slices; j++) {
                float start = (((j - 0.5f) / (float) slices) + 0.25f) * 360;
                float end = (((j + 0.5f) / (float) slices) + 0.25f) * 360;
                float addRadius = (radiusIn+5)*i;
                float itemRadius = (radiusIn+radiusOut+(addRadius*2))/2;
                float middle = (float) Math.toRadians(start+end)/2;
                float midX = x + itemRadius * (float) Math.cos(middle);
                float midY = y + itemRadius * (float) Math.sin(middle);
                this.itemRenderer.renderAndDecorateItem(containedItems.get(j+(i*9)), (int)midX, (int)midY);
                this.itemRenderer.renderGuiItemDecorations(this.font, containedItems.get(j+(i*9)), (int)midX, (int)midY, "");
            }
        }
        poseStack.popPose();
        RenderSystem.applyModelViewMatrix();
        matrixStack.popPose();

        matrixStack.pushPose();
        for (int i = 0; i < numberOfRings; i++) {
            int slices = i < numberOfRings-1 ? 9 : numberOfSlices%9 == 0 ? 9 : numberOfSlices%9;
            for (int j = 0; j < slices; j++) {
                float start = (((j - 0.5f) / (float) slices) + 0.25f) * 360;
                float end = (((j + 0.5f) / (float) slices) + 0.25f) * 360;
                float addRadius = (radiusIn+5)*i;
                float itemRadius = (radiusIn+radiusOut+(addRadius*2))/2;
                float middle = (float) Math.toRadians(start+end)/2;
                float midX = x + itemRadius * (float) Math.cos(middle);
                float midY = y + itemRadius * (float) Math.sin(middle);
                if (selected == j+(i*9))
                    this.renderTooltip(matrixStack, containedItems.get(j+(i*9)), (int)midX, (int)midY);
            }
        }
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
