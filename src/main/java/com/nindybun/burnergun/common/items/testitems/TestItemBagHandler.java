package com.nindybun.burnergun.common.items.testitems;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class TestItemBagHandler extends ItemStackHandler {
    public static final Logger LOGGER = LogManager.getLogger();
    public TestItemBagHandler(int numberOfSlots){
        super(numberOfSlots);
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return super.insertItem(slot, stack, simulate);
    }

    protected void onContentsChanged(int slot) {
        this.validateSlotIndex(slot);
    }


}
