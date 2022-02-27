package com.nindybun.burnergun.util;

import com.nindybun.burnergun.common.items.BurnerGunNBT;
import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.common.items.upgrades.UpgradeCard;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UpgradeUtil {
    private static final String KEY_FILTER = "filter";
    private static final String KEY_UPGRADE = "upgrade";
    private static final String KEY_ENABLED = "enabled";

    public static ListTag setUpgradesNBT(List<Upgrade> upgrades) {
        ListTag list = new ListTag();

        upgrades.forEach(upgrade -> {
            CompoundTag compound = new CompoundTag();
            compound.putString(KEY_UPGRADE, upgrade.getName());
            compound.putBoolean(KEY_ENABLED, upgrade.isActive());
            list.add(compound);
        });

        return list;
    }

    public static ListTag serializeList(List<Item> items) {
        ListTag list = new ListTag();

        items.forEach(item -> {
            CompoundTag compound = new CompoundTag();
            compound.putInt(KEY_FILTER, MapItem.getId(item));
            list.add(compound);
        });

        return list;
    }

    public static List<Item> deserializeList(ListTag list){
        List<Item> items = new ArrayList<>();
        if (list.isEmpty())
            return items;
        for (int i = 0; i < list.size(); i++) {
            CompoundTag listNBT = list.getCompound(i);
            Item type = MapItem.byId(listNBT.getInt(KEY_FILTER));
            if (type == null)
                continue;
            items.add(type);
        }
        return items;
    }

    public static void removeEnchantment(ItemStack gun, Enchantment e){
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(gun);
        enchantments.remove(e);
        EnchantmentHelper.setEnchantments(enchantments, gun);
    }



    public static Upgrade getUpgradeByName(String name){
        try {
            Upgrade type = Upgrade.valueOf(name.toUpperCase());
            return type;
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    public static Upgrade getUpgradeFromListByUpgrade(List<Upgrade> upgrades, Upgrade type){
        for (Upgrade upgrade: upgrades) {
            if (upgrade.lazyIs(type))
                return upgrade;
        }
        return null;
    }

    public static List<Upgrade> getUpgradesFromNBT(ListTag upgrades){
        List<Upgrade> upgradeList = new ArrayList<>();
        if (upgrades.isEmpty())
            return upgradeList;
        for (int i = 0; i < upgrades.size(); i++) {
            CompoundTag upgradeNBT = upgrades.getCompound(i);
            Upgrade type = getUpgradeByName(upgradeNBT.getString(KEY_UPGRADE));
            if (type == null)
                continue;
            type.setActive(!upgradeNBT.contains(KEY_ENABLED) || upgradeNBT.getBoolean(KEY_ENABLED));
            upgradeList.add(type);
        }
        return upgradeList;
    }

    public static boolean containsUpgradeFromList(List<Upgrade> upgradeList, Upgrade upgrade){
        for (Upgrade index : upgradeList) {
            if (index.getBaseName().equals(upgrade.getBaseName()))
                return true;
        }
        return false;
    }

    public static List<Upgrade> getToggleableUpgrades(ItemStack gun){
        return BurnerGunNBT.getUpgrades(gun).stream().filter(Upgrade::isToggleable).collect(Collectors.toList());
    }

    public static List<Upgrade> getActiveUpgrades(ItemStack stack){
        return BurnerGunNBT.getUpgrades(stack).stream().filter(Upgrade::isActive).collect(Collectors.toList());
    }

    public static List<Upgrade> getUpgrades(ItemStack stack){
        List<Upgrade> upgrades = new ArrayList<>();
        IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
        int start = stack.getItem() instanceof BurnerGunMK1 ? 1 : 0;
        for (int index = start; index < handler.getSlots(); index++){
            if (handler.getStackInSlot(index).getItem() != Items.AIR){
                upgrades.add(((UpgradeCard)handler.getStackInSlot(index).getItem()).getUpgrade());
            }
        }
        return upgrades;
    }

    //Returns upgrade stacks
    public static List<ItemStack> getUpgradeStacks(ItemStack stack){
        List<ItemStack> upgradeStacks = new ArrayList<>();
        IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
        int start = stack.getItem() instanceof BurnerGunMK1 ? 1 : 0;
        for (int index = start; index < handler.getSlots(); index++){
            if (handler.getStackInSlot(index).getItem() != Items.AIR){
                upgradeStacks.add(handler.getStackInSlot(index));
            }
        }
        return upgradeStacks;
    }

    public static ItemStack getStackByUpgrade(ItemStack stack, Upgrade upgrade){
        List<ItemStack> upgradeStack = getUpgradeStacks(stack);
        List<Upgrade> upgradeCard = BurnerGunNBT.getUpgrades(stack);
        for (int index = 0 ; index < upgradeCard.size() ; index++) {
            if (upgradeCard.get(index).getBaseName().equals(upgrade.getBaseName())){
                return upgradeStack.get(index);
            }
        }
        return null;
    }

    public static void updateUpgrade(ItemStack stack, Upgrade upgrade){
        ListTag upgrades = stack.getOrCreateTag().getList(BurnerGunNBT.UPGRADES, Tag.TAG_COMPOUND);
        upgrades.forEach(e -> {
            CompoundTag compound = (CompoundTag) e;
            String name = compound.getString(KEY_UPGRADE);
            boolean isEnabled = compound.getBoolean(KEY_ENABLED);
            if( (name.contains(Upgrade.FORTUNE_1.getBaseName()) && isEnabled && upgrade.lazyIs(Upgrade.SILK_TOUCH) )
                    || (name.equals(Upgrade.SILK_TOUCH.getBaseName()) && isEnabled && upgrade.lazyIs(Upgrade.FORTUNE_1) ))
                compound.putBoolean(KEY_ENABLED, false);

            if( name.equals(upgrade.getName()) )
                compound.putBoolean(KEY_ENABLED, !compound.getBoolean(KEY_ENABLED));
        });
    }

}
