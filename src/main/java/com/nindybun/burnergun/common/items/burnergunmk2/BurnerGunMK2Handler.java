package com.nindybun.burnergun.common.items.burnergunmk2;

import com.nindybun.burnergun.common.containers.BurnerGunMK2Container;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.common.items.upgrades.UpgradeCard;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BurnerGunMK2Handler extends ItemStackHandler {
    public static final int MAX_SLOTS = BurnerGunMK2Container.MAX_EXPECTED_GUN_SLOT_COUNT;

    public BurnerGunMK2Handler(int numberOfSlots){
        super(numberOfSlots);
    }

    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (slot < 0 || slot >= MAX_SLOTS) {
            throw new IllegalArgumentException("Invalid slot number: " + slot);
        }
        if (stack.getItem() instanceof UpgradeCard
                && !(Upgrade.AMBIENCE_1.lazyIs(((UpgradeCard) stack.getItem()).getUpgrade()))
                && !(Upgrade.FUEL_EFFICIENCY_1.lazyIs(((UpgradeCard) stack.getItem()).getUpgrade()))
                && getUpgradeByUpgrade(((UpgradeCard) stack.getItem()).getUpgrade()) == null
                && (((UpgradeCard) stack.getItem()).getUpgrade().getType() == "type.burnergun"
                    || ((UpgradeCard) stack.getItem()).getUpgrade().getType() == "type.util"
                    || ((UpgradeCard) stack.getItem()).getUpgrade().getType() == "type.tool")){
            return true;
        }
        return false;
    }

    public List<UpgradeCard> getUpgrades(){
        List<UpgradeCard> upgrades = new ArrayList<>();
        for (int index  = 1; index < MAX_SLOTS; index++){
            if (this.getStackInSlot(index).getItem() != Items.AIR){
                upgrades.add((UpgradeCard)this.getStackInSlot(index).getItem());
            }
        }
        return upgrades;
    }

    public Upgrade getUpgradeByUpgrade(Upgrade upgrade){
        List<UpgradeCard> upgrades = getUpgrades();
        for (UpgradeCard upgradeCard : upgrades) {
            if (upgradeCard.getUpgrade().getBaseName().equals(upgrade.getBaseName())){
                return upgradeCard.getUpgrade();
            }
        }
        return null;
    }

    @Override
    protected void onContentsChanged(int slot) {
        this.validateSlotIndex(slot);
    }

    private final Logger LOGGER = LogManager.getLogger();



}
