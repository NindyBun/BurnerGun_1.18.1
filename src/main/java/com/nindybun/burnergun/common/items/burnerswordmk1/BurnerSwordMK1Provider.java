package com.nindybun.burnergun.common.items.burnerswordmk1;

import com.nindybun.burnergun.common.containers.BurnerGunMK1Container;
import com.nindybun.burnergun.common.containers.BurnerSwordMK1Container;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BurnerSwordMK1Provider implements ICapabilitySerializable<CompoundTag> {
    private final BurnerSwordMK1Handler instance = new BurnerSwordMK1Handler(BurnerSwordMK1Container.MAX_EXPECTED_SWORD_SLOT_COUNT);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY == cap ? (LazyOptional<T>)(LazyOptional.of(()->instance)) : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return instance.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        instance.deserializeNBT(nbt);
    }
}
