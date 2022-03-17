package com.nindybun.burnergun.common.network.packets;

import com.nindybun.burnergun.client.screens.ModScreens;
import com.nindybun.burnergun.common.items.BurnerGunNBT;
import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class PacketClientPlayLightSound {
    private static final Logger LOGGER = LogManager.getLogger();
    private float volume;
    public PacketClientPlayLightSound(float volume) {
        this.volume = volume;
    }

    public static void encode(PacketClientPlayLightSound msg, FriendlyByteBuf buffer) {
        buffer.writeFloat(msg.volume);
    }

    public static PacketClientPlayLightSound decode(FriendlyByteBuf buffer) {
        return new PacketClientPlayLightSound(buffer.readFloat());
    }

    public static class Handler {
        public static void handle(PacketClientPlayLightSound msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    Minecraft.getInstance().player.playSound(SoundEvents.WOOL_PLACE, msg.volume*0.5f, 1.0f);
                });
            });
            ctx.get().setPacketHandled(true);
        }
    }
}