package com.nindybun.burnergun.common.items.upgrades.Trash;

import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2;
import com.nindybun.burnergun.common.items.upgrades.UpgradeCard;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

public class TrashHandler extends ItemStackHandler {
    private boolean isDirty = true;
    public static final Logger LOGGER = LogManager.getLogger();

    public TrashHandler(int numberOfSlots){
        super(numberOfSlots);
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        this.setStackInSlot(slot, Items.AIR.getDefaultInstance());
        return ItemStack.EMPTY;
    }

    @Override
    public CompoundTag serializeNBT() {
        super.serializeNBT();
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (stack.getItem() instanceof BurnerGunMK1 || stack.getItem() instanceof BurnerGunMK2 || stack.getItem() instanceof UpgradeCard)
            return false;
        if (this.getStackInSlot(slot).getItem() == Items.AIR){
            this.setStackInSlot(slot, stack.getItem().getDefaultInstance());
        }else if (stack.getItem() == Items.AIR){
            this.setStackInSlot(slot, Items.AIR.getDefaultInstance());
        }else if (stack.getItem() != Items.AIR){
            this.setStackInSlot(slot, Items.AIR.getDefaultInstance());
            this.setStackInSlot(slot, stack.getItem().getDefaultInstance());
        }
        return false;
    }

    protected void onContentsChanged(int slot) {
        this.validateSlotIndex(slot);
    }


}
