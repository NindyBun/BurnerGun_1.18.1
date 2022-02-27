package com.nindybun.burnergun.common.items.burnergunmk2;

import com.nindybun.burnergun.common.containers.BurnerGunMK2Container;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BurnerGunMK2Provider implements ICapabilitySerializable<CompoundTag> {
    private final BurnerGunMK2Handler instance = new BurnerGunMK2Handler(BurnerGunMK2Container.MAX_EXPECTED_GUN_SLOT_COUNT);

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
