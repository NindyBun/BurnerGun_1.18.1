package com.nindybun.burnergun.common.network.packets;

import com.nindybun.burnergun.common.items.BurnerGunNBT;
import com.nindybun.burnergun.common.items.abstractItems.AbstractBurnerGun;
import com.nindybun.burnergun.common.items.testitems.TestItemBag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.function.Supplier;

public class PacketSaveSelection {
    private ItemStack itemStack;
    private static final Logger LOGGER = LogManager.getLogger();

    public PacketSaveSelection(ItemStack itemStack){
        this.itemStack = itemStack;
    }

    public static void encode(PacketSaveSelection msg, FriendlyByteBuf buffer){
        buffer.writeItemStack(msg.itemStack, false);
    }

    public static PacketSaveSelection decode(FriendlyByteBuf buffer){
        return new PacketSaveSelection(buffer.readItem());
    }

    public static class Handler {
        public static void handle(PacketSaveSelection msg, Supplier<NetworkEvent.Context> ctx){
            ctx.get().enqueueWork( ()-> {
                ServerPlayer player = ctx.get().getSender();
                if (player == null)
                    return;
                ItemStack tool = player.getMainHandItem();
                if (!(tool.getItem() instanceof TestItemBag))
                    return;
                tool.getOrCreateTag().put("Bullet_Info", msg.itemStack.getOrCreateTag().copy());
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
