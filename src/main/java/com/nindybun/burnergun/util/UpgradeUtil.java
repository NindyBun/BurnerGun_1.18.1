package com.nindybun.burnergun.util;

import com.nindybun.burnergun.common.blocks.ModBlocks;
import com.nindybun.burnergun.common.items.BurnerGunNBT;
import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.common.items.upgrades.UpgradeCard;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class UpgradeUtil {
    private static final String KEY_FILTER = "filter";
    private static final String KEY_UPGRADE = "upgrade";
    private static final String KEY_ENABLED = "enabled";
    private static final RecipeType<? extends AbstractCookingRecipe> RECIPE_TYPE = RecipeType.SMELTING;
    private static final Logger LOGGER = LogManager.getLogger();

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
    public static List<ItemStack> getUpgradeStacks(ItemStack gun){
        List<ItemStack> upgradeStacks = new ArrayList<>();
        IItemHandler handler = gun.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
        int start = gun.getItem() instanceof BurnerGunMK1 ? 1 : 0;
        for (int index = start; index < handler.getSlots(); index++){
            if (handler.getStackInSlot(index).getItem() != Items.AIR){
                upgradeStacks.add(handler.getStackInSlot(index));
            }
        }
        return upgradeStacks;
    }

    public static ItemStack getStackByUpgrade(ItemStack gun, Upgrade upgrade){
        List<ItemStack> upgradeStack = getUpgradeStacks(gun);
        List<Upgrade> upgradeCard = BurnerGunNBT.getUpgrades(gun);
        for (int index = 0 ; index < upgradeCard.size() ; index++) {
            if (upgradeCard.get(index).getBaseName().equals(upgrade.getBaseName())){
                return upgradeStack.get(index);
            }
        }
        return null;
    }

    public static void updateUpgrade(ItemStack stack, Upgrade upgrade){
        ListTag upgrades = stack.getOrCreateTag().getList(BurnerGunNBT.UPGRADES, Tag.TAG_COMPOUND);
        List<Upgrade> upgradeList = getUpgrades(stack);
        upgradeList.forEach(indexUpgrade -> {
            if ( (indexUpgrade.lazyIs(Upgrade.FORTUNE_1) && indexUpgrade.isActive() && upgrade.lazyIs(Upgrade.SILK_TOUCH))
                    || (indexUpgrade.lazyIs(Upgrade.SILK_TOUCH) && indexUpgrade.isActive() && upgrade.lazyIs(Upgrade.FORTUNE_1)) )
                indexUpgrade.setActive(false);
            if (upgrade.lazyIs(indexUpgrade)){
                indexUpgrade.setActive(!indexUpgrade.isActive());
            }
        });
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

    public static ItemStack trashItem(List<Item> trashList, ItemStack drop, Boolean trashWhitelist){
        if (trashList.contains(drop.getItem()) && !trashWhitelist)
            return drop;
        else if (!trashList.contains(drop.getItem()) && trashWhitelist)
            return drop;
        return ItemStack.EMPTY;
    }

    public static ItemStack smeltItem(Level world, List<Item> smeltList, ItemStack drop, Boolean smeltWhitelist){
        SimpleContainer inv = new SimpleContainer(1);
        inv.setItem(0, drop);
        Optional<? extends AbstractCookingRecipe> recipe = world.getRecipeManager().getRecipeFor(RECIPE_TYPE, inv, world);
        if (recipe.isPresent()){
            ItemStack smelted = recipe.get().getResultItem().copy();
            if (smeltList.contains(drop.getItem()) && smeltWhitelist)
                return smelted;
            else if (!smeltList.contains(drop.getItem()) && !smeltWhitelist)
                return smelted;
        }
        return drop;
    }

    public static void spawnLight(Level world, BlockHitResult ray, ItemStack gun){
        if (world.getBrightness(LightLayer.BLOCK, ray.getBlockPos().relative(ray.getDirection())) < 8 && ray.getType() == BlockHitResult.Type.BLOCK && BurnerGunNBT.getFuelValue(gun) >= Upgrade.LIGHT.getCost()){
            if (gun.getItem() instanceof BurnerGunMK1)
                BurnerGunNBT.setFuelValue(gun, BurnerGunNBT.getFuelValue(gun)-Upgrade.LIGHT.getCost());
            world.setBlockAndUpdate(ray.getBlockPos(), ModBlocks.LIGHT.get().defaultBlockState());
        }
    }

    public static List<BlockPos> collectBlocks(List<BlockPos> minedBlocks, List<BlockPos> blockPosList, BlockPos targetedBlock, BlockState targetedState, Level level){
        for (int y = targetedBlock.getY()-1; y <= targetedBlock.getY()+1; y++){
            for (int x = targetedBlock.getX()-1; x <= targetedBlock.getX()+1; x++){
                for (int z = targetedBlock.getZ()-1; z <= targetedBlock.getZ()+1; z++){
                    BlockPos newPos = new BlockPos(x, y, z);
                    if (minedBlocks.contains(newPos) || blockPosList.contains(newPos))
                        continue;
                    BlockState newState = level.getBlockState(newPos).getBlock().defaultBlockState();
                    if (newState.equals(targetedState))
                        blockPosList.add(newPos);
                }
            }
        }
        return blockPosList;
    }

}
