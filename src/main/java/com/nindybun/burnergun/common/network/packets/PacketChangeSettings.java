package com.nindybun.burnergun.common.network.packets;

import com.nindybun.burnergun.common.items.BurnerGunNBT;
import com.nindybun.burnergun.common.items.abstractItems.AbstractBurnerGun;
import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class PacketChangeSettings {
    private CompoundTag nbt;
    private static final Logger LOGGER = LogManager.getLogger();

    public PacketChangeSettings(CompoundTag nbt){
        this.nbt = nbt;
    }

    public static void encode(PacketChangeSettings msg, FriendlyByteBuf buffer){
        buffer.writeNbt(msg.nbt);
    }

    public static PacketChangeSettings decode(FriendlyByteBuf buffer){
        return new PacketChangeSettings(buffer.readNbt());
    }

    public static class Handler {
        public static void handle(PacketChangeSettings msg, Supplier<NetworkEvent.Context> ctx){
            ctx.get().enqueueWork( ()-> {
                ServerPlayer player = ctx.get().getSender();
                if (player == null)
                    return;
                ItemStack gun = AbstractBurnerGun.getGun(player);;
                if (gun.isEmpty())
                    return;
                BurnerGunNBT.setVolume(gun, msg.nbt.getFloat("Volume"));
                BurnerGunNBT.setRaycast(gun, msg.nbt.getInt("Raycast"));
                BurnerGunNBT.setVertical(gun, msg.nbt.getInt("Vertical"));
                BurnerGunNBT.setHorizontal(gun, msg.nbt.getInt("Horizontal"));
                BurnerGunNBT.setCollectedBlocks(gun, msg.nbt.getInt("Collected_Blocks"));
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
