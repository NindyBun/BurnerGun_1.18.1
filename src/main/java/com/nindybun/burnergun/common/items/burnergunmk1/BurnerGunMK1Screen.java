package com.nindybun.burnergun.common.items.burnergunmk1;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.nindybun.burnergun.common.BurnerGun;
import com.nindybun.burnergun.common.containers.BurnerGunMK1Container;
import com.nindybun.burnergun.common.network.PacketHandler;
import com.nindybun.burnergun.common.network.packets.PacketUpdateGun;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

public class BurnerGunMK1Screen extends AbstractContainerScreen<BurnerGunMK1Container> {
    private static ItemStack gun;
    public BurnerGunMK1Screen(BurnerGunMK1Container container, Inventory playerInv, Component title) {
        super(container, playerInv, title);
        this.gun = BurnerGunMK1.getGun(playerInv.player);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        //this.minecraft
        this.minecraft.getTextureManager().bindForSetup(DEFAULT_TEXTURE);

        // width and height are the size provided to the window when initialised after creation.
        // xSize, ySize are the expected size of the texture-? usually seems to be left as a default.
        // The code below is typical for vanilla containers, so I've just copied that- it appears to centre the texture within
        //  the available window
        int edgeSpacingX = (this.width - this.getXSize()) / 2;
        int edgeSpacingY = (this.height - this.getYSize()) / 2;
        this.blit(matrixStack, edgeSpacingX, edgeSpacingY, 0, 0, this.getXSize(), this.getYSize());
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        this.font.draw(matrixStack, new TranslatableComponent("tooltip." + BurnerGun.MOD_ID + ".screen.burnergunmk1"), 19, -8, Color.WHITE.getRGB());
    }

    private static final Logger LOGGER = LogManager.getLogger();
    // This is the resource location for the background image
    private static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(BurnerGun.MOD_ID, "textures/gui/burnergunmk1_gui.png");

    @Override
    public void removed() {
        PacketHandler.sendToServer(new PacketUpdateGun(false));
        super.removed();
    }

    @Override
    protected void init() {
        super.init();
        clearWidgets();
        int x = this.width / 2;
        int y = this.height / 2;
        addWidget(new Button(x-45, y+(130/2), 90, 20,
                new TranslatableComponent("tooltip." + BurnerGun.MOD_ID + ".screen.openSettings"), (button) -> {
            PacketHandler.sendToServer(new PacketUpdateGun(true));
        }));
    }

}
