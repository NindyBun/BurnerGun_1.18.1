package com.nindybun.burnergun.client.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class utilInventoryScreen extends EffectRenderingInventoryScreen<utilInventoryScreen.ChestInventoryMenu> {
    public utilInventoryScreen(ItemStack utilInv) {
        super(new TextComponent("Titile"));
    }

    @Override
    protected void renderBg(PoseStack p_97787_, float p_97788_, int p_97789_, int p_97790_) {

    }

    static class ChestInventoryMenu extends AbstractContainerMenu {

        protected ChestInventoryMenu(Player player) {
            super((MenuType<?>)null, 0);
        }

        @Override
        public boolean stillValid(Player player) {
            return player.isAlive();
        }
    }

}