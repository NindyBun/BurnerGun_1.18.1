package com.nindybun.burnergun.common.items.upgrades.Trash;

import com.nindybun.burnergun.common.containers.TrashContainer;
import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2;
import com.nindybun.burnergun.common.items.upgrades.UpgradeCard;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class TrashHandler extends ItemStackHandler {
    private boolean isDirty = true;
    public static final Logger LOGGER = LogManager.getLogger();

    public TrashHandler(int numberOfSlots){
        super(numberOfSlots);
    }
/*
    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        this.setStackInSlot(slot, ItemStack.EMPTY);
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (slot < 0 || slot >= TrashContainer.MAX_EXPECTED_HANDLER_SLOT_COUNT) {
            throw new IllegalArgumentException("Invalid slot number: " + slot);
        }
        if (stack.getItem() instanceof BurnerGunMK1 || stack.getItem() instanceof BurnerGunMK2 || stack.getItem() instanceof UpgradeCard)
            return false;
        this.setStackInSlot(slot, stack.getItem().getDefaultInstance());
        return false;
    }*/

    protected void onContentsChanged(int slot) {
        this.validateSlotIndex(slot);
    }


}
