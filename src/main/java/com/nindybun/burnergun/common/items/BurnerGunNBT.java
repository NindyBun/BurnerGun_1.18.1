package com.nindybun.burnergun.common.items;

import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.util.UpgradeUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BurnerGunNBT {
    private BurnerGunNBT() {}

    private static final String SMELTING_WHITELIST = "burnergun.tag.smelt_whitelist";
    private static final String SMELTING_FILTER = "burnergun.tag.smelt_filter";
    private static final String TRASH_WHITELIST = "burnergun.tag.trash_whitelist";
    private static final String TRASH_FILTER = "burnergun.tag.trash_filter";
    private static final String FUEL_VALUE = "burnergun.tag.fuel_value";
    private static final String VOLUME = "burnergun.tag.volume";
    private static final String HORIZONTAL = "burnergun.tag.horizontal";
    private static final String MAX_HORIZONTAL = "burnergun.tag.max_horizontal";
    private static final String VERTICAL = "burnergun.tag.vertical";
    private static final String MAX_VERTICAL = "burnergun.tag.max_vertical";
    private static final String RAYCAST = "burnergun.tag.raycast";
    private static final String MAX_RAYCAST = "burnergun.tag.max_raycast";
    public static final String UPGRADES = "burnergun.tag.upgrades";
    private static final String COLOR = "burnergun.tag.color";

    public static final int MIN_RAYCAST = 5;

    public static boolean setSmeltWhitelist(ItemStack gun, Boolean isWhitelist){
        gun.getOrCreateTag().putBoolean(SMELTING_WHITELIST, isWhitelist);
        return isWhitelist;
    }
    public static boolean getSmeltWhitelist(ItemStack gun){
        CompoundTag tag = gun.getOrCreateTag();
        return !tag.contains(SMELTING_WHITELIST) ? setSmeltWhitelist(gun, true) : tag.getBoolean(SMELTING_WHITELIST);
    }

    public static boolean setTrashWhitelist(ItemStack gun, Boolean isWhitelist){
        gun.getOrCreateTag().putBoolean(TRASH_WHITELIST, isWhitelist);
        return isWhitelist;
    }
    public static boolean getTrashWhitelist(ItemStack gun){
        CompoundTag tag = gun.getOrCreateTag();
        return !tag.contains(TRASH_WHITELIST) ? setTrashWhitelist(gun, true) : tag.getBoolean(TRASH_WHITELIST);
    }

    public static List<Item> setSmeltFilter(ItemStack gun, List<Item> items){
        gun.getOrCreateTag().put(SMELTING_FILTER, UpgradeUtil.serializeList(items));
        return items;
    }
    public static List<Item> getSmeltFilter(ItemStack gun){
        CompoundTag tag = gun.getOrCreateTag();
        return !tag.contains(SMELTING_FILTER) ? setSmeltFilter(gun, new ArrayList<>()) : UpgradeUtil.deserializeList(tag.getList(SMELTING_FILTER, Tag.TAG_COMPOUND));
    }

    public static List<Item> setTrashFilter(ItemStack gun, List<Item> items){
        gun.getOrCreateTag().put(TRASH_FILTER, UpgradeUtil.serializeList(items));
        return items;
    }
    public static List<Item> getTrashFilter(ItemStack gun){
        CompoundTag tag = gun.getOrCreateTag();
        return !tag.contains(TRASH_FILTER) ? setTrashFilter(gun, new ArrayList<>()) : UpgradeUtil.deserializeList(tag.getList(TRASH_FILTER, Tag.TAG_COMPOUND));
    }

    public static double setFuelValue(ItemStack gun, double value){
        gun.getOrCreateTag().putDouble(FUEL_VALUE, value);
        return value;
    }
    public static double getFuelValue(ItemStack gun){
        CompoundTag tag = gun.getOrCreateTag();
        return !tag.contains(FUEL_VALUE) ? setFuelValue(gun, 0.0) : tag.getDouble(FUEL_VALUE);
    }

    public static float setVolume(ItemStack gun, float value){
        gun.getOrCreateTag().putFloat(VOLUME, value);
        return value;
    }
    public static float getVolume(ItemStack gun){
        CompoundTag tag = gun.getOrCreateTag();
        return !tag.contains(VOLUME) ? setVolume(gun, 1.0f) : tag.getFloat(VOLUME);
    }

    public static int setHorizontal(ItemStack gun, int value){
        gun.getOrCreateTag().putInt(HORIZONTAL, value);
        return value;
    }
    public static int getHorizontal(ItemStack gun){
        CompoundTag tag = gun.getOrCreateTag();
        return !tag.contains(HORIZONTAL) ? setHorizontal(gun, 0) : tag.getInt(HORIZONTAL);
    }

    public static int setMaxHorizontal(ItemStack gun, int value){
        gun.getOrCreateTag().putInt(MAX_HORIZONTAL, value);
        return value;
    }
    public static int getMaxHorizontal(ItemStack gun){
        CompoundTag tag = gun.getOrCreateTag();
        return !tag.contains(MAX_HORIZONTAL) ? setMaxHorizontal(gun, 0) : tag.getInt(MAX_HORIZONTAL);
    }

    public static int setVertical(ItemStack gun, int value){
        gun.getOrCreateTag().putInt(VERTICAL, value);
        return value;
    }
    public static int getVertical(ItemStack gun){
        CompoundTag tag = gun.getOrCreateTag();
        return !tag.contains(VERTICAL) ? setVertical(gun, 0) : tag.getInt(VERTICAL);
    }

    public static int setMaxVertical(ItemStack gun, int value){
        gun.getOrCreateTag().putInt(MAX_VERTICAL, value);
        return value;
    }
    public static int getMaxVertical(ItemStack gun){
        CompoundTag tag = gun.getOrCreateTag();
        return !tag.contains(MAX_VERTICAL) ? setMaxVertical(gun, 0) : tag.getInt(MAX_VERTICAL);
    }

    public static int setRaycast(ItemStack gun, int value){
        gun.getOrCreateTag().putInt(RAYCAST, value);
        return value;
    }
    public static int getRaycast(ItemStack gun){
        CompoundTag tag = gun.getOrCreateTag();
        return !tag.contains(RAYCAST) ? setRaycast(gun, MIN_RAYCAST) : tag.getInt(RAYCAST);
    }

    public static int setMaxRaycast(ItemStack gun, int value){
        gun.getOrCreateTag().putInt(MAX_RAYCAST, value);
        return value;
    }
    public static int getMaxRaycast(ItemStack gun){
        CompoundTag tag = gun.getOrCreateTag();
        return !tag.contains(MAX_RAYCAST) ? setMaxRaycast(gun, MIN_RAYCAST) : tag.getInt(MAX_RAYCAST);
    }

    public static List<Upgrade> setUprades(ItemStack gun, List<Upgrade> upgrades){
        gun.getOrCreateTag().put(UPGRADES, UpgradeUtil.setUpgradesNBT(upgrades));
        return upgrades;
    }
    public static List<Upgrade> getUpgrades(ItemStack gun){
        CompoundTag tag = gun.getOrCreateTag();
        return !tag.contains(UPGRADES) ? setUprades(gun, new ArrayList<>()) : UpgradeUtil.getUpgradesFromNBT(tag.getList(UPGRADES, Tag.TAG_COMPOUND));
    }

    public static float[] setColor(ItemStack gun, float[] color){
        CompoundTag tag = new CompoundTag();
        tag.putFloat("Red", color[0]);
        tag.putFloat("Green", color[1]);
        tag.putFloat("Blue", color[2]);
        gun.getOrCreateTag().put(COLOR, tag);
        return color;
    }
    public static float[] getColor(ItemStack gun){
        CompoundTag tag = gun.getOrCreateTag();
        return !tag.contains(COLOR) ? setColor(gun, new float[]{0, 0, 0}) : new float[]{
                tag.getList(COLOR, Tag.TAG_COMPOUND).getCompound(0).getFloat("Red"),
                tag.getList(COLOR, Tag.TAG_COMPOUND).getCompound(0).getFloat("Green"),
                tag.getList(COLOR, Tag.TAG_COMPOUND).getCompound(0).getFloat("Blue"),
        };
    }


}
