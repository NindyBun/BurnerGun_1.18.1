package com.nindybun.burnergun.common.network.packets;

import com.nindybun.burnergun.common.items.BurnerGunNBT;
import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class PacketChangeColor {
    private CompoundTag nbt;
    private static final Logger LOGGER = LogManager.getLogger();

    public PacketChangeColor(CompoundTag nbt){
        this.nbt = nbt;
    }

    public static void encode(PacketChangeColor msg, FriendlyByteBuf buffer){
        buffer.writeNbt(msg.nbt);
    }

    public static PacketChangeColor decode(FriendlyByteBuf buffer){
        return new PacketChangeColor(buffer.readNbt());
    }

    public static class Handler {
        public static void handle(PacketChangeColor msg, Supplier<NetworkEvent.Context> ctx){
            ctx.get().enqueueWork( ()-> {
                ServerPlayer player = ctx.get().getSender();
                if (player == null)
                    return;
                ItemStack gun = !BurnerGunMK2.getGun(player).isEmpty() ? BurnerGunMK2.getGun(player) : BurnerGunMK1.getGun(player);
                if (gun.isEmpty())
                    return;

                BurnerGunNBT.setColor(gun, new float[]{
                        msg.nbt.getFloat("Red"),
                        msg.nbt.getFloat("Green"),
                        msg.nbt.getFloat("Blue")
                });
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
