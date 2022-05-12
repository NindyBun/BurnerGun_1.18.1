package com.nindybun.burnergun.common.network.packets;

import com.nindybun.burnergun.common.items.BurnerGunNBT;
import com.nindybun.burnergun.common.items.abstractItems.AbstractBurnerGun;
import com.nindybun.burnergun.common.items.abstractItems.AbstractBurnerSword;
import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.burnerswordmk1.BurnerSwordMK1;
import com.nindybun.burnergun.common.items.upgrades.Auto_Smelt.AutoSmelt;
import com.nindybun.burnergun.common.items.upgrades.Trash.Trash;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.common.items.upgrades.UpgradeCard;
import com.nindybun.burnergun.common.network.PacketHandler;
import com.nindybun.burnergun.util.UpgradeUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PacketUpdateSword {
    private static final Logger LOGGER = LogManager.getLogger();
    private boolean open;
    public PacketUpdateSword(boolean open) {
        this.open = open;
    }

    public static void encode(PacketUpdateSword msg, FriendlyByteBuf buffer) {
        buffer.writeBoolean(msg.open);
    }

    public static PacketUpdateSword decode(FriendlyByteBuf buffer) {
        return new PacketUpdateSword(buffer.readBoolean());
    }

    public static class Handler {
        public static void handle(PacketUpdateSword msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                if (player == null)
                    return;

                ItemStack sword = AbstractBurnerSword.getSword(player);
                if (sword.isEmpty())
                    return;

                IItemHandler handler = sword.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
                List<Upgrade> currentUpgrades = new ArrayList<>();

                int type = sword.getItem() instanceof BurnerSwordMK1 ? 1 : 0;
                for (int i = type; i < handler.getSlots(); i++) {
                    if (!handler.getStackInSlot(i).getItem().equals(Items.AIR))
                        currentUpgrades.add(((UpgradeCard)handler.getStackInSlot(i).getItem()).getUpgrade());
                }

                /*if (UpgradeUtil.containsUpgradeFromList(currentUpgrades, Upgrade.REACH_1)){
                    Upgrade upgrade = UpgradeUtil.getUpgradeFromListByUpgrade(currentUpgrades, Upgrade.FOCAL_POINT_1);
                        if (BurnerGunNBT.getRaycast(gun) > (int)upgrade.getExtraValue())
                            BurnerGunNBT.setRaycast(gun, (int)upgrade.getExtraValue());
                        BurnerGunNBT.setMaxRaycast(gun, (int)upgrade.getExtraValue());
                }else if (!UpgradeUtil.containsUpgradeFromList(currentUpgrades, Upgrade.FOCAL_POINT_1)){
                        if (BurnerGunNBT.getRaycast(gun) > BurnerGunNBT.MIN_RAYCAST)
                            BurnerGunNBT.setRaycast(gun, BurnerGunNBT.MIN_RAYCAST);
                        if (BurnerGunNBT.getMaxRaycast(gun) > BurnerGunNBT.MIN_RAYCAST)
                            BurnerGunNBT.setMaxRaycast(gun, BurnerGunNBT.MIN_RAYCAST);
                }*/

                /*currentUpgrades.forEach(upgrade -> {
                    if ((upgrade.lazyIs(Upgrade.FORTUNE_1) && upgrade.isActive() && currentUpgrades.contains(Upgrade.SILK_TOUCH) && UpgradeUtil.getUpgradeFromListByUpgrade(currentUpgrades, Upgrade.SILK_TOUCH).isActive())
                            || (upgrade.lazyIs(Upgrade.SILK_TOUCH) && upgrade.isActive() && UpgradeUtil.containsUpgradeFromList(currentUpgrades, Upgrade.FORTUNE_1) && UpgradeUtil.getUpgradeFromListByUpgrade(currentUpgrades, Upgrade.FORTUNE_1).isActive())){
                        upgrade.setActive(false);
                    }
                });*/
                BurnerGunNBT.setUprades(sword, currentUpgrades);
                /*if (msg.open)
                    PacketHandler.sendTo(new PacketClientOpenGunSettings(sword), player);*/
            });
            ctx.get().setPacketHandled(true);
        }
    }
}