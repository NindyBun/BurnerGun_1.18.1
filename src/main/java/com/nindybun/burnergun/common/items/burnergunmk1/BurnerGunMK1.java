package com.nindybun.burnergun.common.items.burnergunmk1;

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
import net.minecraft.nbt.Tag;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
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
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class BurnerGunMK1 extends Item {
    private static final double base_use = 100;
    public static final double base_use_buffer = 10_000;
    private static final Logger LOGGER = LogManager.getLogger();
    private static final RecipeType<? extends AbstractCookingRecipe> RECIPE_TYPE = RecipeType.SMELTING;

    public BurnerGunMK1() {
        super(new Properties().stacksTo(1).setNoRepair().tab(com.nindybun.burnergun.common.BurnerGun.itemGroup));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        IItemHandler handler = getHandler(stack);
        if (       !handler.getStackInSlot(0).getItem().equals(Upgrade.AMBIENCE_1.getCard().asItem())
                && !handler.getStackInSlot(0).getItem().equals(Upgrade.AMBIENCE_2.getCard().asItem())
                && !handler.getStackInSlot(0).getItem().equals(Upgrade.AMBIENCE_3.getCard().asItem())
                && !handler.getStackInSlot(0).getItem().equals(Upgrade.AMBIENCE_4.getCard().asItem())
                && !handler.getStackInSlot(0).getItem().equals(Upgrade.AMBIENCE_5.getCard().asItem())){
            tooltip.add(new TranslatableComponent("Feed me fuel!").withStyle(ChatFormatting.YELLOW));
        }else if (  handler.getStackInSlot(0).getItem().equals(Upgrade.AMBIENCE_1.getCard().asItem())
                    || handler.getStackInSlot(0).getItem().equals(Upgrade.AMBIENCE_2.getCard().asItem())
                    || handler.getStackInSlot(0).getItem().equals(Upgrade.AMBIENCE_3.getCard().asItem())
                    || handler.getStackInSlot(0).getItem().equals(Upgrade.AMBIENCE_4.getCard().asItem())
                    || handler.getStackInSlot(0).getItem().equals(Upgrade.AMBIENCE_5.getCard().asItem())){
            tooltip.add(new TranslatableComponent("Collecting heat from nearby sources!").withStyle(ChatFormatting.YELLOW));
        }
        tooltip.add(new TranslatableComponent("Press " + GLFW.glfwGetKeyName(Keybinds.burnergun_gui_key.getKey().getValue(), GLFW.glfwGetKeyScancode(Keybinds.burnergun_gui_key.getKey().getValue())).toUpperCase() + " to open GUI").withStyle(ChatFormatting.GRAY));
        tooltip.add(new TranslatableComponent("Press " + GLFW.glfwGetKeyName(Keybinds.burnergun_light_key.getKey().getValue(), GLFW.glfwGetKeyScancode(Keybinds.burnergun_light_key.getKey().getValue())).toUpperCase() + " to shoot light!").withStyle(ChatFormatting.GRAY));
        tooltip.add(new TranslatableComponent("Press " + GLFW.glfwGetKeyName(Keybinds.burnergun_lightPlayer_key.getKey().getValue(), GLFW.glfwGetKeyScancode(Keybinds.burnergun_lightPlayer_key.getKey().getValue())).toUpperCase() + " to place light at your head!").withStyle(ChatFormatting.GRAY));
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void refuel(ItemStack gun, Player player){
        IItemHandler handler = getHandler(gun);
        if (!handler.getStackInSlot(0).getItem().equals(Upgrade.AMBIENCE_1.getCard().asItem())
                && !handler.getStackInSlot(0).getItem().equals(Upgrade.AMBIENCE_2.getCard().asItem())
                && !handler.getStackInSlot(0).getItem().equals(Upgrade.AMBIENCE_3.getCard().asItem())
                && !handler.getStackInSlot(0).getItem().equals(Upgrade.AMBIENCE_4.getCard().asItem())
                && !handler.getStackInSlot(0).getItem().equals(Upgrade.AMBIENCE_5.getCard().asItem())) {
            while (handler.getStackInSlot(0).getCount() > 0){
                if (BurnerGunNBT.getFuelValue(gun) + handler.getStackInSlot(0).getBurnTime(RecipeType.SMELTING) > base_use_buffer)
                    break;
                BurnerGunNBT.setFuelValue(gun, BurnerGunNBT.getFuelValue(gun) + handler.getStackInSlot(0).getBurnTime(RecipeType.SMELTING));
                ItemStack containerItem = handler.getStackInSlot(0).getContainerItem();
                handler.getStackInSlot(0).shrink(1);
                if (player.getInventory().add(containerItem))
                    player.drop(containerItem, true);
            }
        }
    }

    public void useFuel(ItemStack gun, Player player, List<Upgrade> upgrades){
        if (!getHandler(gun).getStackInSlot(0).getItem().equals(Upgrade.AMBIENCE_1.getCard().asItem())
                && !getHandler(gun).getStackInSlot(0).getItem().equals(Upgrade.AMBIENCE_2.getCard().asItem())
                && !getHandler(gun).getStackInSlot(0).getItem().equals(Upgrade.AMBIENCE_3.getCard().asItem())
                && !getHandler(gun).getStackInSlot(0).getItem().equals(Upgrade.AMBIENCE_4.getCard().asItem())
                && !getHandler(gun).getStackInSlot(0).getItem().equals(Upgrade.AMBIENCE_5.getCard().asItem()))
            refuel(gun, player);
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
        if (    state.getDestroySpeed(world, pos) <= 0
                || state.getBlock() instanceof Light
                || !world.mayInteract(player, pos) || !player.mayBuild()
                || MinecraftForge.EVENT_BUS.post(new BlockEvent.BreakEvent(world, pos, state, player))
                || BurnerGunNBT.getFuelValue(gun) < getUseValue(upgrades)
                || state.getBlock().equals(Blocks.AIR.defaultBlockState())
                || state.getBlock().equals(Blocks.CAVE_AIR.defaultBlockState()))
            return false;
        return true;
    }

    public ItemStack trashItem(List<Item> trashList, ItemStack drop, Boolean trashWhitelist){
        if (trashList.contains(drop.getItem()) && !trashWhitelist)
            return drop;
        else if (!trashList.contains(drop.getItem()) && trashWhitelist)
            return drop;
        return ItemStack.EMPTY;
    }

    public ItemStack smeltItem(Level world, List<Item> smeltList, ItemStack drop, Boolean smeltWhitelist){
        SimpleContainer inv = new SimpleContainer(1);
        inv.setItem(0, drop);
        Optional<? extends AbstractCookingRecipe> recipe = world.getRecipeManager().getRecipeFor(RECIPE_TYPE, inv, world);
        if (recipe.isPresent()){
            ItemStack smelted = recipe.get().getResultItem().copy();
            if (smeltList.contains(smelted.getItem()) && smeltWhitelist)
                return smelted;
            else if (!smeltList.contains(smelted.getItem()) && !smeltWhitelist)
                return smelted;
        }
        return drop;
    }

    public void spawnLight(Level world, BlockHitResult ray, ItemStack gun){
        if (world.getBrightness(LightLayer.BLOCK, ray.getBlockPos().relative(ray.getDirection())) < 8 && ray.getType() == BlockHitResult.Type.BLOCK && BurnerGunNBT.getFuelValue(gun) >= Upgrade.LIGHT.getCost()){
            BurnerGunNBT.setFuelValue(gun, BurnerGunNBT.getFuelValue(gun)-Upgrade.LIGHT.getCost());
            world.setBlockAndUpdate(ray.getBlockPos(), ModBlocks.LIGHT.get().defaultBlockState());
        }
    }

    public void mineBlock(Level world, BlockHitResult ray, ItemStack gun, List<Upgrade> activeUpgrades, List<Item> smeltFilter, List<Item> trashFilter, BlockPos blockPos, BlockState blockState, Player player){
        if (canMine(gun, world, blockPos, blockState, player, activeUpgrades)){
            useFuel(gun, player, activeUpgrades);
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
                        drop = smeltItem(world, smeltFilter, drop.copy(), BurnerGunNBT.getSmeltWhitelist(gun));
                    if (UpgradeUtil.containsUpgradeFromList(activeUpgrades, Upgrade.TRASH))
                        drop = trashItem(trashFilter, drop.copy(), BurnerGunNBT.getTrashWhitelist(gun));
                    if (UpgradeUtil.containsUpgradeFromList(activeUpgrades, Upgrade.MAGNET)){
                        if (!player.getInventory().add(drop.copy()))
                            player.drop(drop.copy(), true);
                    }else{
                        world.addFreshEntity(new ItemEntity(world, blockPos.getX()+0.5, blockPos.getY()+0.5, blockPos.getZ()+0.5, drop.copy()));
                    }
                });
            }
            if (UpgradeUtil.containsUpgradeFromList(activeUpgrades, Upgrade.MAGNET))
                player.giveExperiencePoints(blockXP);
            else
                blockState.getBlock().popExperience((ServerLevel) world, blockPos, blockXP);
            if (UpgradeUtil.containsUpgradeFromList(activeUpgrades, Upgrade.LIGHT)){
                spawnLight(world, ray, gun);
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
                    mineBlock(world, ray, gun, activeUpgrades, smeltFilter, trashFilter, thePos, theState, player);
                }
            }
        }
        mineBlock(world, ray, gun, activeUpgrades, smeltFilter, trashFilter, blockPos, blockState, player);
    }

    @Override
    public void inventoryTick(ItemStack gun, Level world, Entity entity, int slot, boolean held) {
        super.inventoryTick(gun, world, entity, slot, held);
        boolean heldgun = ((Player)entity).getMainHandItem().getItem() instanceof BurnerGunMK1 || ((Player)entity).getOffhandItem().getItem() instanceof BurnerGunMK1 ? true : false;
        if (heldgun && entity instanceof Player && gun.getItem() instanceof BurnerGunMK1){
            IItemHandler handler = getHandler(gun);
            if (handler.getStackInSlot(0).getItem().equals(Upgrade.AMBIENCE_1.getCard().asItem())
                    || handler.getStackInSlot(0).getItem().equals(Upgrade.AMBIENCE_2.getCard().asItem())
                    || handler.getStackInSlot(0).getItem().equals(Upgrade.AMBIENCE_3.getCard().asItem())
                    || handler.getStackInSlot(0).getItem().equals(Upgrade.AMBIENCE_4.getCard().asItem())
                    || handler.getStackInSlot(0).getItem().equals(Upgrade.AMBIENCE_5.getCard().asItem())){
                if (BurnerGunNBT.getFuelValue(gun)+((UpgradeCard)handler.getStackInSlot(0).getItem()).getUpgrade().getExtraValue() < base_use_buffer && world.getMaxLocalRawBrightness((entity.blockPosition())) >= 8)
                    BurnerGunNBT.setFuelValue(gun, BurnerGunNBT.getFuelValue(gun)+((UpgradeCard)handler.getStackInSlot(0).getItem()).getUpgrade().getExtraValue());
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
        if (world.isClientSide)
            player.playSound(SoundEvents.FIRECHARGE_USE, BurnerGunNBT.getVolume(gun)*0.5f, 1.0f);
        if (!world.isClientSide){
            refuel(gun, player);
            if (canMine(gun, world, blockPos, blockState, player, activeUpgrades)){
                gun.enchant(Enchantments.BLOCK_FORTUNE, UpgradeUtil.containsUpgradeFromList(activeUpgrades, Upgrade.FORTUNE_1) ? UpgradeUtil.getUpgradeFromListByUpgrade(activeUpgrades, Upgrade.FORTUNE_1).getTier() : 0);
                gun.enchant(Enchantments.SILK_TOUCH, UpgradeUtil.containsUpgradeFromList(activeUpgrades, Upgrade.SILK_TOUCH) ? 1 : 0);
                if (player.isCrouching())
                    mineBlock(world, blockRayTraceResult, gun, activeUpgrades, BurnerGunNBT.getSmeltFilter(gun), BurnerGunNBT.getTrashFilter(gun), blockPos, blockState, player);
                else
                    mineArea(world, blockRayTraceResult, gun, activeUpgrades, BurnerGunNBT.getSmeltFilter(gun), BurnerGunNBT.getTrashFilter(gun), blockPos, blockState, player);
            }
        }
        UpgradeUtil.removeEnchantment(gun, Enchantments.BLOCK_FORTUNE);
        UpgradeUtil.removeEnchantment(gun, Enchantments.SILK_TOUCH);
        return InteractionResultHolder.consume(gun);
    }


}
