package com.nindybun.burnergun.common.items;

import com.nindybun.burnergun.common.items.burnerswordmk1.BurnerSwordMK1;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.util.UpgradeUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class BurnerGunNBT {
    private BurnerGunNBT() {}

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String SMELTING_WHITELIST = "burnergun.tag.smelt_whitelist";
    public static final String SMELTING_FILTER = "burnergun.tag.smelt_filter";
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
    private static final String BLOCKS_COLLECTED = "burnergun.tag.blocks_collected";
    private static final String MAX_BLOCKS_COLLECTED = "burnergun.tag.max_blocks_collected";
    private static final String DAMAGE = "burnergun.tag.damage";
    private static final String ATTACK_SPEED = "burnergun.tag.attack_speed";
    private static final String ATTACK_COOLDOWN = "burnergun.tag.attack_cooldown";

    public static final int MIN_RAYCAST = 5;
    public static final float BASE_ATKSPEED = 1.6f;
    public static final int MIN_SWORD_MK1_ATK = 6;
    public static final int MIN_SWORD_MK2_ATK = 8;

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
                tag.getCompound(COLOR).getFloat("Red"),
                tag.getCompound(COLOR).getFloat("Green"),
                tag.getCompound(COLOR).getFloat("Blue"),
        };
    }

    public static int setCollectedBlocks(ItemStack gun, int collect){
        gun.getOrCreateTag().putInt(BLOCKS_COLLECTED, collect);
        return collect;
    }
    public static int getCollectedBlocks(ItemStack gun){
        CompoundTag tag = gun.getOrCreateTag();
        return !tag.contains(BLOCKS_COLLECTED) ? setCollectedBlocks(gun, 1) : tag.getInt(BLOCKS_COLLECTED);
    }

    public static int setMaxCollectedBlocks(ItemStack gun, int collect){
        gun.getOrCreateTag().putInt(MAX_BLOCKS_COLLECTED, collect);
        return collect;
    }
    public static int getMaxCollectedBlocks(ItemStack gun){
        CompoundTag tag = gun.getOrCreateTag();
        return !tag.contains(MAX_BLOCKS_COLLECTED) ? setMaxCollectedBlocks(gun, 1) : tag.getInt(MAX_BLOCKS_COLLECTED);
    }

    public static int setAtkDmg(ItemStack tool, int dmg){
        tool.getOrCreateTag().putInt(DAMAGE, dmg);
        return dmg;
    }
    public static int getAtkDmg(ItemStack tool){
        CompoundTag tag = tool.getOrCreateTag();
        return !tag.contains(DAMAGE) ? setAtkDmg(tool, tool.getItem() instanceof BurnerSwordMK1 ? MIN_SWORD_MK1_ATK :  MIN_SWORD_MK2_ATK) : tag.getInt(DAMAGE);
    }

    public static float setAtkSpeed(ItemStack tool, float atkSpeed){
        tool.getOrCreateTag().putFloat(ATTACK_SPEED, atkSpeed);
        return atkSpeed;
    }
    public static float getAtkSpeed(ItemStack tool){
        CompoundTag tag = tool.getOrCreateTag();
        return !tag.contains(ATTACK_SPEED) ? setAtkSpeed(tool, BASE_ATKSPEED) : tag.getFloat(ATTACK_SPEED);
    }

    public static float setAtkCoolDown(ItemStack tool, float atkCoolDown){
        tool.getOrCreateTag().putFloat(ATTACK_COOLDOWN, atkCoolDown);
        return atkCoolDown;
    }
    public static float getAtkCoolDown(ItemStack tool){
        CompoundTag tag = tool.getOrCreateTag();
        return !tag.contains(ATTACK_COOLDOWN) ? setAtkCoolDown(tool, 2f/(BurnerGunNBT.getAtkSpeed(tool))) : tag.getFloat(ATTACK_COOLDOWN);
    }
}
