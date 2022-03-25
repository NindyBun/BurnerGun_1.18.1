package com.nindybun.burnergun.common.items.burnergunmk1;

import com.google.common.collect.ArrayListMultimap;
import com.nindybun.burnergun.client.Keybinds;
import com.nindybun.burnergun.common.blocks.Light;
import com.nindybun.burnergun.common.blocks.ModBlocks;
import com.nindybun.burnergun.common.items.BurnerGunNBT;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.common.items.upgrades.UpgradeCard;
import com.nindybun.burnergun.util.UpgradeUtil;
import com.nindybun.burnergun.util.WorldUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BurnerGunMK1 extends Item {
    private static final double base_use = 100;
    public static final double base_use_buffer = 20_000;
    private static final Logger LOGGER = LogManager.getLogger();

    public BurnerGunMK1() {
        super(new Properties().stacksTo(1).setNoRepair().tab(com.nindybun.burnergun.common.BurnerGun.itemGroup));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        IItemHandler handler = getHandler(stack);
        if (!(handler.getStackInSlot(0).getItem() instanceof UpgradeCard)){
            tooltip.add(new TranslatableComponent("Feed me fuel!").withStyle(ChatFormatting.YELLOW));
        }else if (handler.getStackInSlot(0).getItem() instanceof UpgradeCard){
            tooltip.add(new TranslatableComponent("Collecting heat from nearby sources!").withStyle(ChatFormatting.YELLOW));
        }
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_77661_1_) {
        return UseAnim.NONE;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Nonnull
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag oldCapNbt) {
        return new BurnerGunMK1Provider();
    }

    public static IItemHandler getHandler(ItemStack itemStack) {
        return itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
    }

    public static ItemStack getGun(Player player) {
        ItemStack heldItem = player.getMainHandItem();
        if (!(heldItem.getItem() instanceof BurnerGunMK1)) {
            heldItem = player.getOffhandItem();
            if (!(heldItem.getItem() instanceof BurnerGunMK1)) {
                return ItemStack.EMPTY;
            }
        }
        return heldItem;
    }

    public void refuel(ItemStack gun){
        IItemHandler handler = getHandler(gun);
        while (handler.getStackInSlot(0).getCount() > 0 && ForgeHooks.getBurnTime(handler.getStackInSlot(0), RecipeType.SMELTING) > 0){
            if (BurnerGunNBT.getFuelValue(gun) + ForgeHooks.getBurnTime(handler.getStackInSlot(0), RecipeType.SMELTING) > base_use_buffer)
                break;
            BurnerGunNBT.setFuelValue(gun, BurnerGunNBT.getFuelValue(gun) + ForgeHooks.getBurnTime(handler.getStackInSlot(0), RecipeType.SMELTING));
            ItemStack containerItem = handler.getStackInSlot(0).copy();
            handler.getStackInSlot(0).shrink(1);
            if (containerItem.hasContainerItem()){
                handler.insertItem(0, Items.BUCKET.getDefaultInstance(), false);
            }
        }
    }

    public void useFuel(ItemStack gun, List<Upgrade> upgrades, Player player){
        if (ForgeHooks.getBurnTime(getHandler(gun).getStackInSlot(0), RecipeType.SMELTING) > 0)
            refuel(gun);
        BurnerGunNBT.setFuelValue(gun, BurnerGunNBT.getFuelValue(gun) - getUseValue(upgrades));
    }

    public double getUseValue(List<Upgrade> upgrades){
        int extraUse = 0;
        if (!upgrades.isEmpty()){
            extraUse = upgrades.stream().mapToInt(upgrade -> upgrade.lazyIs(Upgrade.LIGHT) ? 0 : upgrade.getCost()).sum();
        }
        return (base_use + extraUse) * (1.0 - ((UpgradeUtil.containsUpgradeFromList(upgrades, Upgrade.FUEL_EFFICIENCY_1)) ? UpgradeUtil.getUpgradeFromListByUpgrade(upgrades, Upgrade.FUEL_EFFICIENCY_1).getExtraValue() : 0));
    }

    public boolean canMine(ItemStack gun, Level world, BlockPos pos, BlockState state, Player player, List<Upgrade> upgrades){
        if (    state.getDestroySpeed(world, pos) < 0
                || state.getBlock() instanceof Light
                || !world.mayInteract(player, pos)
                || !player.getAbilities().mayBuild
                || BurnerGunNBT.getFuelValue(gun) < getUseValue(upgrades)
                || state.getBlock().equals(Blocks.AIR.defaultBlockState())
                || state.getBlock().equals(Blocks.CAVE_AIR.defaultBlockState())
                || (state.getFluidState().isSource() && !state.hasProperty(BlockStateProperties.WATERLOGGED))
                || (state.getFluidState().getAmount() > 0 && !state.hasProperty(BlockStateProperties.WATERLOGGED))
                || world.isEmptyBlock(pos))
            return false;
        return true;
    }

    public void mineVein(Level world, BlockHitResult ray, List<BlockPos> blockPosList, List<BlockPos> minedBlockList, int count, ItemStack gun, List<Upgrade> activeUpgrades, List<Item> smeltFilter, List<Item> trashFilter, Player player){
        if (blockPosList.isEmpty() || count <= 0)
            return;
        BlockState blockState = world.getBlockState(blockPosList.get(0));
        BlockPos blockPos = blockPosList.get(0);
        blockPosList.remove(0);
        minedBlockList.add(blockPos);

        if (canMine(gun, world, blockPos, blockState, player, activeUpgrades)){
            blockPosList = UpgradeUtil.collectBlocks(minedBlockList, blockPosList, blockPos, blockState.getBlock().defaultBlockState(), world);
            mineBlock(world, ray, gun, activeUpgrades, smeltFilter, trashFilter, blockPos, blockState, player, true);
        }

        count -= 1;
        mineVein(world, ray, blockPosList, minedBlockList, count, gun, activeUpgrades, smeltFilter, trashFilter, player);
    }

    public void mineBlock(Level world, BlockHitResult ray, ItemStack gun, List<Upgrade> activeUpgrades, List<Item> smeltFilter, List<Item> trashFilter, BlockPos blockPos, BlockState blockState, Player player, Boolean isVein){
        if (canMine(gun, world, blockPos, blockState, player, activeUpgrades)){
            useFuel(gun, activeUpgrades, player);
            List<ItemStack> blockDrops = blockState.getDrops(new LootContext.Builder((ServerLevel) world)
                    .withParameter(LootContextParams.TOOL, gun)
                    .withParameter(LootContextParams.ORIGIN, new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ()))
                    .withParameter(LootContextParams.BLOCK_STATE, blockState)
            );
            world.destroyBlock(blockPos, false);
            int blockXP = blockState.getExpDrop(world, blockPos, UpgradeUtil.containsUpgradeFromList(activeUpgrades, Upgrade.FORTUNE_1) ? UpgradeUtil.getUpgradeFromListByUpgrade(activeUpgrades, Upgrade.FORTUNE_1).getTier() : 0, UpgradeUtil.containsUpgradeFromList(activeUpgrades, Upgrade.SILK_TOUCH) ? 1 : 0);
            if (!blockDrops.isEmpty()){
                blockDrops.forEach(drop -> {
                    if (UpgradeUtil.containsUpgradeFromList(activeUpgrades, Upgrade.AUTO_SMELT))
                        drop = UpgradeUtil.smeltItem(world, smeltFilter, drop.copy(), BurnerGunNBT.getSmeltWhitelist(gun));
                    if (UpgradeUtil.containsUpgradeFromList(activeUpgrades, Upgrade.TRASH))
                        drop = UpgradeUtil.trashItem(trashFilter, drop.copy(), BurnerGunNBT.getTrashWhitelist(gun));
                    if (UpgradeUtil.containsUpgradeFromList(activeUpgrades, Upgrade.MAGNET)){
                        if (!player.getInventory().add(drop.copy()))
                            player.drop(drop.copy(), true);
                    }else{
                        world.addFreshEntity(new ItemEntity(world, (isVein ? ray.getBlockPos() : blockPos).getX()+0.5, (isVein ? ray.getBlockPos() : blockPos).getY()+0.5, (isVein ? ray.getBlockPos() : blockPos).getZ()+0.5, drop.copy()));
                    }
                });
            }
            if (UpgradeUtil.containsUpgradeFromList(activeUpgrades, Upgrade.MAGNET))
                player.giveExperiencePoints(blockXP);
            else
                blockState.getBlock().popExperience((ServerLevel) world, blockPos, blockXP);
            if (UpgradeUtil.containsUpgradeFromList(activeUpgrades, Upgrade.LIGHT)){
                UpgradeUtil.spawnLight(world, ray, gun);
            }
        }
    }

    public void mineArea(Level world, BlockHitResult ray, ItemStack gun, List<Upgrade> activeUpgrades, List<Item> smeltFilter, List<Item> trashFilter, BlockPos blockPos, BlockState blockState, Player player){
        int xRad = BurnerGunNBT.getHorizontal(gun);
        int yRad = BurnerGunNBT.getVertical(gun);
        Vec3 size = WorldUtil.getDim(ray, xRad, yRad, player);
        for (int xPos = blockPos.getX() - (int)size.x(); xPos <= blockPos.getX() + (int)size.x(); ++xPos){
            for (int yPos = blockPos.getY() - (int)size.y(); yPos <= blockPos.getY() + (int)size.y(); ++yPos){
                for (int zPos = blockPos.getZ() - (int)size.z(); zPos <= blockPos.getZ() + (int)size.z(); ++zPos){
                    BlockPos thePos = new BlockPos(xPos, yPos, zPos);
                    if (thePos.equals(blockPos))
                        continue;
                    BlockState theState = world.getBlockState(thePos);
                    mineBlock(world, ray, gun, activeUpgrades, smeltFilter, trashFilter, thePos, theState, player, false);
                }
            }
        }
        mineBlock(world, ray, gun, activeUpgrades, smeltFilter, trashFilter, blockPos, blockState, player, false);
    }

    @Override
    public void inventoryTick(ItemStack gun, Level world, Entity entity, int slot, boolean held) {
        super.inventoryTick(gun, world, entity, slot, held);
        boolean heldgun = ((Player)entity).getMainHandItem().getItem() instanceof BurnerGunMK1 || ((Player)entity).getOffhandItem().getItem() instanceof BurnerGunMK1 ? true : false;
        if (heldgun && entity instanceof Player && gun.getItem() instanceof BurnerGunMK1){
            IItemHandler handler = getHandler(gun);
            if (handler.getStackInSlot(0).getItem() instanceof UpgradeCard){
                double fuel = BurnerGunNBT.getFuelValue(gun)+((UpgradeCard)handler.getStackInSlot(0).getItem()).getUpgrade().getExtraValue();
                if (world.getMaxLocalRawBrightness((entity.blockPosition())) >= 8)
                    BurnerGunNBT.setFuelValue(gun, fuel >= base_use_buffer ? base_use_buffer : fuel);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack gun = player.getItemInHand(hand);
        List<Upgrade> activeUpgrades = UpgradeUtil.getActiveUpgrades(gun);
        BlockHitResult blockRayTraceResult = WorldUtil.getLookingAt(world, player, ClipContext.Fluid.NONE, BurnerGunNBT.getRaycast(gun));
        BlockPos blockPos = blockRayTraceResult.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        List<Item> smeltFilter = BurnerGunNBT.getSmeltFilter(gun);
        List<Item> trashFilter = BurnerGunNBT.getTrashFilter(gun);
        if (world.isClientSide)
            player.playSound(SoundEvents.FIRECHARGE_USE, BurnerGunNBT.getVolume(gun)*0.5f, 1.0f);
        if (!world.isClientSide){
            refuel(gun);
            if (canMine(gun, world, blockPos, blockState, player, activeUpgrades)){
                gun.enchant(Enchantments.BLOCK_FORTUNE, UpgradeUtil.containsUpgradeFromList(activeUpgrades, Upgrade.FORTUNE_1) ? UpgradeUtil.getUpgradeFromListByUpgrade(activeUpgrades, Upgrade.FORTUNE_1).getTier() : 0);
                gun.enchant(Enchantments.SILK_TOUCH, UpgradeUtil.containsUpgradeFromList(activeUpgrades, Upgrade.SILK_TOUCH) ? 1 : 0);
                if (UpgradeUtil.containsUpgradeFromList(activeUpgrades, Upgrade.VEIN_MINER_1) && Keybinds.burnergun_veinMiner_key.isDown()){
                    List<BlockPos> blocks = new ArrayList<>();
                    blocks.add(blockPos);
                    mineVein(world, blockRayTraceResult, blocks, new ArrayList<>(), BurnerGunNBT.getCollectedBlocks(gun), gun, activeUpgrades, BurnerGunNBT.getSmeltFilter(gun), BurnerGunNBT.getTrashFilter(gun), player);
                }else if (player.isCrouching())
                    mineBlock(world, blockRayTraceResult, gun, activeUpgrades, smeltFilter, trashFilter, blockPos, blockState, player, false);
                else
                    mineArea(world, blockRayTraceResult, gun, activeUpgrades, smeltFilter, trashFilter, blockPos, blockState, player);
            }
        }
        UpgradeUtil.removeEnchantment(gun, Enchantments.BLOCK_FORTUNE);
        UpgradeUtil.removeEnchantment(gun, Enchantments.SILK_TOUCH);
        return InteractionResultHolder.consume(gun);
    }


}
