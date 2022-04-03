package com.nindybun.burnergun.common.network.packets;

import com.nindybun.burnergun.common.items.BurnerGunNBT;
import com.nindybun.burnergun.common.items.abstractItems.AbstractBurnerGun;
import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketToggleTrashFilter {
    public PacketToggleTrashFilter() {
    }

    public static void encode(PacketToggleTrashFilter msg, FriendlyByteBuf buffer) {

    }

    public static PacketToggleTrashFilter decode(FriendlyByteBuf buffer) {
        return new PacketToggleTrashFilter();
    }

    public static class Handler {
        public static void handle(PacketToggleTrashFilter msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                if (player == null)
                    return;

                ItemStack gun = AbstractBurnerGun.getGun(player);
                if (gun.isEmpty())
                    return;
                BurnerGunNBT.setTrashWhitelist(gun, !BurnerGunNBT.getTrashWhitelist(gun));
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
