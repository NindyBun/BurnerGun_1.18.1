package com.nindybun.burnergun.common.network.packets;

import com.nindybun.burnergun.common.containers.TrashContainer;
import com.nindybun.burnergun.common.items.BurnerGunNBT;
import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2;
import com.nindybun.burnergun.common.items.upgrades.Trash.Trash;
import com.nindybun.burnergun.common.items.upgrades.Trash.TrashHandler;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.util.UpgradeUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class PacketOpenTrashGui {
    public PacketOpenTrashGui(){

    }

    public static void encode(PacketOpenTrashGui msg, FriendlyByteBuf buffer) {
    }

    public static PacketOpenTrashGui decode(FriendlyByteBuf buffer) {
        return new PacketOpenTrashGui();
    }

    public static class Handler {
        public static void handle(PacketOpenTrashGui msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();

                if (player == null)
                    return;

                ItemStack gun = !BurnerGunMK2.getGun(player).isEmpty() ? BurnerGunMK2.getGun(player) : BurnerGunMK1.getGun(player);
                ItemStack trash = player.getMainHandItem();
                if (!gun.isEmpty()){
                    List<Upgrade> upgradeList = BurnerGunNBT.getUpgrades(gun);
                    if (upgradeList.contains(Upgrade.TRASH))
                        trash = UpgradeUtil.getStackByUpgrade(gun, Upgrade.TRASH);
                }

                if (!(trash.getItem() instanceof Trash))
                    return;

                IItemHandler handler = trash.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
                player.openMenu(new SimpleMenuProvider(
                        (windowId, playerInv, playerEntity) -> new TrashContainer(windowId, playerInv, (TrashHandler) handler),
                        new TextComponent("")
                ));

            });

            ctx.get().setPacketHandled(true);
        }
    }




}
