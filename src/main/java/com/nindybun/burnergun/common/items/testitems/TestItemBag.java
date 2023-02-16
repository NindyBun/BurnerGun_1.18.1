package com.nindybun.burnergun.common.items.testitems;

import com.nindybun.burnergun.BurnerGun;
import com.nindybun.burnergun.common.containers.TestItemBagContainer;
import com.nindybun.burnergun.common.containers.TrashContainer;
import com.nindybun.burnergun.common.items.upgrades.Trash.TrashHandler;
import com.nindybun.burnergun.common.items.upgrades.Trash.TrashProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class TestItemBag extends Item {
    public TestItemBag() {
        super(new Properties().stacksTo(1).setNoRepair().tab(BurnerGun.itemGroup));
    }
    @Nonnull
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag oldCapNbt) {
        return new TestItemBagProvider();
    }

    public static IItemHandler getHandler(ItemStack itemStack) {
        return itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide)
            player.openMenu(new SimpleMenuProvider(
                    (windowId, playerInv, playerEntity) -> new TestItemBagContainer(windowId, playerInv, (TestItemBagHandler) getHandler(stack)),
                    new TextComponent("")
            ));
        return InteractionResultHolder.success(stack);
    }
}
