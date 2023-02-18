package com.nindybun.burnergun.common.items.testitems;

import com.nindybun.burnergun.BurnerGun;
import com.nindybun.burnergun.common.containers.TestItemBagContainer;
import com.nindybun.burnergun.common.containers.TrashContainer;
import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.upgrades.Trash.TrashHandler;
import com.nindybun.burnergun.common.items.upgrades.Trash.TrashProvider;
import com.nindybun.burnergun.common.items.upgrades.UpgradeCard;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestItemBag extends Item {
    public TestItemBag() {
        super(new Properties().stacksTo(1).setNoRepair().tab(BurnerGun.itemGroup));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains("Bullet_Info"))
            tag.put("Bullet_Info", new CompoundTag());
        List<ItemStack> list = new ArrayList<>();
        for (int i = 0; i < getHandler(stack).getSlots(); i++){
            if (getHandler(stack).getStackInSlot(i) != ItemStack.EMPTY){
                if (!doesContainInList(list, getHandler(stack).getStackInSlot(i)))
                    list.add(getHandler(stack).getStackInSlot(i));
            }
        }
        ItemStack selectedItem = ItemStack.EMPTY;
        for (ItemStack bullet : list) {
            if (bullet.getOrCreateTag().equals(tag.getCompound("Bullet_Info"))){
                selectedItem = bullet;
                break;
            }
        }
        String name = selectedItem.getDisplayName().getString();
        //name.subSequence(name.indexOf('[')+1, name.indexOf(']').toString())
        tooltip.add(new TextComponent("Selected Bullet: ").append(new TextComponent(name)).withStyle(ChatFormatting.AQUA));

        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    public boolean doesContainInList(List<ItemStack> list, ItemStack itemStack){
        for (ItemStack stack : list){
            if (stack.equals(itemStack, false))
                return true;
        }
        return false;
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
