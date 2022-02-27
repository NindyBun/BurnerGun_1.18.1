package com.nindybun.burnergun.common.items.burnergunmk2;

import com.nindybun.burnergun.common.containers.BurnerGunMK2Container;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.common.items.upgrades.UpgradeCard;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

public class BurnerGunMK2Handler extends ItemStackHandler {
    public static final int MAX_SLOTS = BurnerGunMK2Container.MAX_EXPECTED_GUN_SLOT_COUNT;

    public BurnerGunMK2Handler(int numberOfSlots){
        super(numberOfSlots);
    }

    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (slot < 0 || slot >= MAX_SLOTS || !(stack.getItem() instanceof UpgradeCard)
                || stack.getItem().equals(Upgrade.AMBIENCE_1.getCard().asItem())
                || stack.getItem().equals(Upgrade.AMBIENCE_2.getCard().asItem())
                || stack.getItem().equals(Upgrade.AMBIENCE_3.getCard().asItem())
                || stack.getItem().equals(Upgrade.AMBIENCE_4.getCard().asItem())
                || stack.getItem().equals(Upgrade.AMBIENCE_5.getCard().asItem())) {
            throw new IllegalArgumentException("Invalid slot number: " + slot);
        }
        return true;
    }

    @Override
    protected void onContentsChanged(int slot) {
        this.validateSlotIndex(slot);
    }

    private final Logger LOGGER = LogManager.getLogger();



}
