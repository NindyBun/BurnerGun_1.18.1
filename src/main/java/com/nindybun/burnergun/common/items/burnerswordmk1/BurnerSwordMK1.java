package com.nindybun.burnergun.common.items.burnerswordmk1;

import com.nindybun.burnergun.BurnerGun;
import com.nindybun.burnergun.common.items.abstractItems.AbstractBurnerSword;
import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1Provider;
import com.nindybun.burnergun.common.items.upgrades.UpgradeCard;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class BurnerSwordMK1 extends AbstractBurnerSword {
    public BurnerSwordMK1() {
        super(new Properties().stacksTo(1).setNoRepair().tab(BurnerGun.itemGroup));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TextComponent("Now with +5 reach!").withStyle(ChatFormatting.YELLOW));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Nonnull
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag oldCapNbt) {
        return new BurnerSwordMK1Provider();
    }
}
