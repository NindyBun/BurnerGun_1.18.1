package com.nindybun.burnergun.common.items.burnergunmk2;

import com.nindybun.burnergun.client.Keybinds;
import com.nindybun.burnergun.common.BurnerGun;
import com.nindybun.burnergun.common.blocks.Light;
import com.nindybun.burnergun.common.blocks.ModBlocks;
import com.nindybun.burnergun.common.items.BurnerGunNBT;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.util.UpgradeUtil;
import com.nindybun.burnergun.util.WorldUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerSynchronizer;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BurnerGunMK2 extends Item {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final RecipeType<? extends AbstractCookingRecipe> RECIPE_TYPE = RecipeType.SMELTING;


    public BurnerGunMK2() {
        super(new Properties().stacksTo(1).setNoRepair().fireResistant().tab(BurnerGun.itemGroup));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TextComponent("Was is Worth it? What did it cost?").withStyle(ChatFormatting.GOLD));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Nonnull
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag oldCapNbt) {
        return new BurnerGunMK2Provider();
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
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static boolean canMine(Level world, BlockPos pos, BlockState state, Player player){
        if (    state.getDestroySpeed(world, pos) < 0
                || state.getBlock() instanceof Light
                || !world.mayInteract(player, pos)
                || !player.getAbilities().mayBuild
                || state.getBlock().equals(Blocks.AIR.defaultBlockState())
                || state.getBlock().equals(Blocks.CAVE_AIR.defaultBlockState())
                || (state.getFluidState().isSource() && !state.hasProperty(BlockStateProperties.WATERLOGGED))
                || (state.getFluidState().getAmount() > 0 && !state.hasProperty(BlockStateProperties.WATERLOGGED))
                || world.isEmptyBlock(pos))
            return false;
        return true;
    }

    public void mineBlock(Level world, BlockHitResult ray, ItemStack gun, List<Upgrade> activeUpgrades, List<Item> smeltFilter, List<Item> trashFilter, BlockPos blockPos, BlockState blockState, Player player, boolean isVein){
        if (canMine(world, blockPos, blockState, player)){
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
            if (UpgradeUtil.containsUpgradeFromList(activeUpgrades, Upgrade.LIGHT))
                UpgradeUtil.spawnLight(world, ray, gun);
        }
    }

    public void mineVein(Level world, BlockHitResult ray, List<BlockPos> blockPosList, List<BlockPos> minedBlockList, int count, ItemStack gun, List<Upgrade> activeUpgrades, List<Item> smeltFilter, List<Item> trashFilter, Player player){
        if (blockPosList.isEmpty())
            return;
        BlockState blockState = world.getBlockState(blockPosList.get(0));
        BlockPos blockPos = blockPosList.get(0);
        blockPosList.remove(0);
        minedBlockList.add(blockPos);

        if (canMine(world, blockPos, blockState, player)){
            blockPosList = UpgradeUtil.collectBlocks(minedBlockList, blockPosList, blockPos, blockState, world, gun, player, activeUpgrades);
            mineBlock(world, ray, gun, activeUpgrades, smeltFilter, trashFilter, blockPos, blockState, player, true);
        }

        count -= 1;
        if (count <= 0)
            return;

        mineVein(world, ray, blockPosList, minedBlockList, count, gun, activeUpgrades, smeltFilter, trashFilter, player);
    }

    public void mineArea(Level world, BlockHitResult ray, ItemStack gun, List<Upgrade> activeUpgrades, List<Item> smeltFilter, List<Item> trashFilter, BlockPos blockPos, BlockState blockState, Player player){
        int xRad = BurnerGunNBT.getHorizontal(gun);
        int yRad = BurnerGunNBT.getVertical(gun);
        Vec3 size = WorldUtil.getDim(ray, xRad, yRad, player);
        for (int xPos = blockPos.getX() - (int)size.x(); xPos <= blockPos.getX() + (int)size.x(); ++xPos){
            for (int yPos = blockPos.getY() - (int)size.y(); yPos <= blockPos.getY() + (int)size.y(); ++yPos){
                for (int zPos = blockPos.getZ() - (int)size.z(); zPos <= blockPos.getZ() + (int)size.z(); ++zPos){
                    BlockPos thePos = new BlockPos(xPos, yPos, zPos);
                    BlockState theState = world.getBlockState(thePos);
                    mineBlock(world, ray, gun, activeUpgrades, smeltFilter, trashFilter, thePos, theState, player, false);
                }
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
           if (canMine(world, blockPos, blockState, player)){
               gun.enchant(Enchantments.BLOCK_FORTUNE, UpgradeUtil.containsUpgradeFromList(activeUpgrades, Upgrade.FORTUNE_1) ? UpgradeUtil.getUpgradeFromListByUpgrade(activeUpgrades, Upgrade.FORTUNE_1).getTier() : 0);
               gun.enchant(Enchantments.SILK_TOUCH, UpgradeUtil.containsUpgradeFromList(activeUpgrades, Upgrade.SILK_TOUCH) ? 1 : 0);
               if (UpgradeUtil.containsUpgradeFromList(activeUpgrades, Upgrade.VEIN_MINER_1) && Keybinds.burnergun_veinMiner_key.isDown()){
                   List<BlockPos> blocks = new ArrayList<>();
                   blocks.add(blockPos);
                   mineVein(world, blockRayTraceResult, blocks, new ArrayList<>(), BurnerGunNBT.getCollectedBlocks(gun), gun, activeUpgrades, BurnerGunNBT.getSmeltFilter(gun), BurnerGunNBT.getTrashFilter(gun), player);
               }if (player.isCrouching())
                    mineBlock(world, blockRayTraceResult, gun, activeUpgrades, smeltFilter, trashFilter, blockPos, blockState, player, false);
                else
                    mineArea(world, blockRayTraceResult, gun, activeUpgrades, smeltFilter, trashFilter, blockPos, blockState, player);
            }
        }
        UpgradeUtil.removeEnchantment(gun, Enchantments.BLOCK_FORTUNE);
        UpgradeUtil.removeEnchantment(gun, Enchantments.SILK_TOUCH);
        return InteractionResultHolder.consume(gun);
    }

    public static ItemStack getGun(Player player) {
        ItemStack heldItem = player.getMainHandItem();
        if (!(heldItem.getItem() instanceof BurnerGunMK2)) {
            heldItem = player.getOffhandItem();
            if (!(heldItem.getItem() instanceof BurnerGunMK2)) {
                return ItemStack.EMPTY;
            }
        }
        return heldItem;
    }

}
