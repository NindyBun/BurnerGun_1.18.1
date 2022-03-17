package com.nindybun.burnergun.common.containers;

import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2;
import com.nindybun.burnergun.common.items.upgrades.UpgradeCard;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class GhostSlot extends SlotItemHandler {
    private static final Logger LOGGER = LogManager.getLogger();
    public GhostSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void set(ItemStack stack) {
        super.set(stack.copy());
    }

    @Override
    public boolean mayPickup(Player player) {
        return false;
    }


    @Override
    public void onTake(Player thePlayer, ItemStack stack) {
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return !(stack.getItem() instanceof BurnerGunMK1) || !(stack.getItem() instanceof BurnerGunMK2) || !(stack.getItem() instanceof UpgradeCard);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }
}
