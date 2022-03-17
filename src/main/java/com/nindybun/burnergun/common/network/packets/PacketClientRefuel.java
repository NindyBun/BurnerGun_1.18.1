package com.nindybun.burnergun.common.network.packets;

import com.nindybun.burnergun.common.items.BurnerGunNBT;
import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.providers.score.ScoreboardNameProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class PacketClientRefuel {
    private static final Logger LOGGER = LogManager.getLogger();
    private ItemStack container;

    public PacketClientRefuel(ItemStack container){
        this.container = container;
    }

    public static void encode(PacketClientRefuel msg, FriendlyByteBuf buffer){
        buffer.writeItemStack(msg.container, true);
    }

    public static PacketClientRefuel decode(FriendlyByteBuf buffer){
        return new PacketClientRefuel(buffer.readItem());
    }

    public static class Handler {
        public static void handle(PacketClientRefuel msg, Supplier<NetworkEvent.Context> ctx){
            ctx.get().enqueueWork( ()-> {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    ItemStack gun = Minecraft.getInstance().player.getMainHandItem().getItem() instanceof BurnerGunMK1 ? Minecraft.getInstance().player.getMainHandItem() : Minecraft.getInstance().player.getOffhandItem();
                    if (gun == ItemStack.EMPTY || !(gun.getItem() instanceof BurnerGunMK1))
                        return;
                    IItemHandler handler = BurnerGunMK1.getHandler(gun);
                    handler.insertItem(0, msg.container, false);
                });
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
