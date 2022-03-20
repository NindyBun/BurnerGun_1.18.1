package com.nindybun.burnergun.common.network.packets;

import com.nindybun.burnergun.client.screens.ModScreens;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class PacketClientOpenGunSettings {
    private static final Logger LOGGER = LogManager.getLogger();
    private ItemStack gun;
    public PacketClientOpenGunSettings(ItemStack stack) {
        this.gun = stack;
    }

    public static void encode(PacketClientOpenGunSettings msg, FriendlyByteBuf buffer) {
    buffer.writeItemStack(msg.gun, false);
    }

    public static PacketClientOpenGunSettings decode(FriendlyByteBuf buffer) {
        return new PacketClientOpenGunSettings(buffer.readItem());
    }

    public static class Handler {
        public static void handle(PacketClientOpenGunSettings msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    ModScreens.openGunSettingsScreen(msg.gun);
                });
            });
            ctx.get().setPacketHandled(true);
        }
    }
}