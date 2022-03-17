package com.nindybun.burnergun.common.network.packets;

import com.nindybun.burnergun.client.screens.ModScreens;
import com.nindybun.burnergun.client.screens.burnergunSettingsScreen;
import com.nindybun.burnergun.common.items.BurnerGunNBT;
import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2;
import com.nindybun.burnergun.common.items.upgrades.Auto_Smelt.AutoSmelt;
import com.nindybun.burnergun.common.items.upgrades.Trash.Trash;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.common.items.upgrades.UpgradeCard;
import com.nindybun.burnergun.common.network.PacketHandler;
import com.nindybun.burnergun.util.UpgradeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class PacketUpdateGun {
    private static final Logger LOGGER = LogManager.getLogger();
    private boolean open;
    public PacketUpdateGun(boolean open) {
        this.open = open;
    }

    public static void encode(PacketUpdateGun msg, FriendlyByteBuf buffer) {
        buffer.writeBoolean(msg.open);
    }

    public static PacketUpdateGun decode(FriendlyByteBuf buffer) {
        return new PacketUpdateGun(buffer.readBoolean());
    }

    public static class Handler {
        public static void handle(PacketUpdateGun msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                if (player == null)
                    return;

                ItemStack gun = !BurnerGunMK2.getGun(player).isEmpty() ? BurnerGunMK2.getGun(player) : BurnerGunMK1.getGun(player);
                if (gun.isEmpty())
                    return;

                IItemHandler handler = gun.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
                List<Upgrade> currentUpgrades = new ArrayList<>();
                IItemHandler trashHandler = null;
                IItemHandler smeltHandler = null;

                int type = gun.getItem() instanceof BurnerGunMK1 ? 1 : 0;
                for (int i = type; i < handler.getSlots(); i++) {
                    if (!handler.getStackInSlot(i).getItem().equals(Items.AIR)){
                        if (((UpgradeCard)handler.getStackInSlot(i).getItem()).getUpgrade().equals(Upgrade.TRASH))
                            trashHandler = Trash.getHandler(handler.getStackInSlot(i));
                        if (((UpgradeCard)handler.getStackInSlot(i).getItem()).getUpgrade().equals(Upgrade.AUTO_SMELT))
                            smeltHandler = AutoSmelt.getHandler(handler.getStackInSlot(i));
                        currentUpgrades.add(((UpgradeCard)handler.getStackInSlot(i).getItem()).getUpgrade());
                    }
                }

                if (UpgradeUtil.containsUpgradeFromList(currentUpgrades, Upgrade.TRASH)){
                    List<Item> trashFilter = new ArrayList<>();
                    for (int i = 0; i < trashHandler.getSlots(); i++){
                        if (!trashHandler.getStackInSlot(i).getItem().equals(Items.AIR))
                            trashFilter.add(trashHandler.getStackInSlot(i).getItem());
                    }
                    BurnerGunNBT.setTrashFilter(gun, trashFilter);
                }else if (!UpgradeUtil.containsUpgradeFromList(currentUpgrades, Upgrade.TRASH)){
                    BurnerGunNBT.setTrashFilter(gun, new ArrayList<>());
                }

                if (UpgradeUtil.containsUpgradeFromList(currentUpgrades, Upgrade.AUTO_SMELT)){
                    List<Item> smeltFilter = new ArrayList<>();
                    for (int i = 0; i < smeltHandler.getSlots(); i++){
                        if (!smeltHandler.getStackInSlot(i).getItem().equals(Items.AIR))
                            smeltFilter.add(smeltHandler.getStackInSlot(i).getItem());
                    }
                    BurnerGunNBT.setSmeltFilter(gun, smeltFilter);
                }else if (!UpgradeUtil.containsUpgradeFromList(currentUpgrades, Upgrade.AUTO_SMELT)){
                    BurnerGunNBT.setSmeltFilter(gun, new ArrayList<>());
                }

                if (UpgradeUtil.containsUpgradeFromList(currentUpgrades, Upgrade.FOCAL_POINT_1)){
                    Upgrade upgrade = UpgradeUtil.getUpgradeFromListByUpgrade(currentUpgrades, Upgrade.FOCAL_POINT_1);
                        if (BurnerGunNBT.getRaycast(gun) > (int)upgrade.getExtraValue())
                            BurnerGunNBT.setRaycast(gun, (int)upgrade.getExtraValue());
                        BurnerGunNBT.setMaxRaycast(gun, (int)upgrade.getExtraValue());
                }else if (!UpgradeUtil.containsUpgradeFromList(currentUpgrades, Upgrade.FOCAL_POINT_1)){
                        if (BurnerGunNBT.getRaycast(gun) > BurnerGunNBT.MIN_RAYCAST)
                            BurnerGunNBT.setRaycast(gun, BurnerGunNBT.MIN_RAYCAST);
                        if (BurnerGunNBT.getMaxRaycast(gun) > BurnerGunNBT.MIN_RAYCAST)
                            BurnerGunNBT.setMaxRaycast(gun, BurnerGunNBT.MIN_RAYCAST);
                }

                if (UpgradeUtil.containsUpgradeFromList(currentUpgrades, Upgrade.VERTICAL_EXPANSION_1)){
                    Upgrade upgrade = UpgradeUtil.getUpgradeFromListByUpgrade(currentUpgrades, Upgrade.VERTICAL_EXPANSION_1);
                        if (BurnerGunNBT.getVertical(gun) > upgrade.getTier())
                            BurnerGunNBT.setVertical(gun, upgrade.getTier());
                        BurnerGunNBT.setMaxVertical(gun, upgrade.getTier());
                }else if (!UpgradeUtil.containsUpgradeFromList(currentUpgrades, Upgrade.VERTICAL_EXPANSION_1)){
                        BurnerGunNBT.setVertical(gun, 0);
                        BurnerGunNBT.setMaxVertical(gun, 0);
                }

                if (UpgradeUtil.containsUpgradeFromList(currentUpgrades, Upgrade.HORIZONTAL_EXPANSION_1)){
                    Upgrade upgrade = UpgradeUtil.getUpgradeFromListByUpgrade(currentUpgrades, Upgrade.HORIZONTAL_EXPANSION_1);
                        if (BurnerGunNBT.getHorizontal(gun) > upgrade.getTier())
                            BurnerGunNBT.setHorizontal(gun, upgrade.getTier());
                        BurnerGunNBT.setMaxHorizontal(gun, upgrade.getTier());
                }else if (!UpgradeUtil.containsUpgradeFromList(currentUpgrades, Upgrade.HORIZONTAL_EXPANSION_1)){
                        BurnerGunNBT.setHorizontal(gun, 0);
                        BurnerGunNBT.setMaxHorizontal(gun, 0);
                }

                currentUpgrades.forEach(upgrade -> {
                    if ((upgrade.lazyIs(Upgrade.FORTUNE_1) && upgrade.isActive() && currentUpgrades.contains(Upgrade.SILK_TOUCH))
                            || (upgrade.lazyIs(Upgrade.SILK_TOUCH) && upgrade.isActive() && UpgradeUtil.containsUpgradeFromList(currentUpgrades, Upgrade.FORTUNE_1))){
                        upgrade.setActive(!upgrade.isActive());
                    }
                });
                BurnerGunNBT.setUprades(gun, currentUpgrades);
                if (msg.open)
                    PacketHandler.sendTo(new PacketClientUpdateGun(gun), player);
            });
            ctx.get().setPacketHandled(true);
        }
    }
}