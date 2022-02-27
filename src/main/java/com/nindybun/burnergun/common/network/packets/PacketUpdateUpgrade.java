package com.nindybun.burnergun.common.network.packets;

import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.util.UpgradeUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class PacketUpdateUpgrade {
    private final String upgrade;

    public PacketUpdateUpgrade(String upgrade) {
        this.upgrade = upgrade;
    }

    public static void encode(PacketUpdateUpgrade msg, FriendlyByteBuf buffer) {
        buffer.writeUtf(msg.upgrade);
    }

    public static PacketUpdateUpgrade decode(FriendlyByteBuf buffer) {
        return new PacketUpdateUpgrade(buffer.readUtf(100));
    }

    public static class Handler {
        public static void handle(PacketUpdateUpgrade msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                if (player == null)
                    return;

                Upgrade upgrade = UpgradeUtil.getUpgradeByName(msg.upgrade);
                if( upgrade == null )
                    return;

                ItemStack stack = !BurnerGunMK2.getGun(player).isEmpty() ? BurnerGunMK2.getGun(player) : BurnerGunMK1.getGun(player);
                if (stack.isEmpty())
                    return;
                UpgradeUtil.updateUpgrade(stack, upgrade);
            });

            ctx.get().setPacketHandled(true);
        }
    }
}