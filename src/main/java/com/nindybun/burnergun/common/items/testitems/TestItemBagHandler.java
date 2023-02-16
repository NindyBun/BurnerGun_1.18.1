package com.nindybun.burnergun.common.items.testitems;

import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestItemBagHandler extends ItemStackHandler {
    public static final Logger LOGGER = LogManager.getLogger();
    public TestItemBagHandler(int numberOfSlots){
        super(numberOfSlots);
    }

    protected void onContentsChanged(int slot) {
        this.validateSlotIndex(slot);
    }


}
