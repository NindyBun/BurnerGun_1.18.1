package com.nindybun.burnergun.common.network.packets;

import com.nindybun.burnergun.common.items.BurnerGunNBT;
import com.nindybun.burnergun.common.items.abstractItems.AbstractBurnerGun;
import com.nindybun.burnergun.common.items.abstractItems.AbstractBurnerSword;
import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.burnerswordmk1.BurnerSwordMK1;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.common.network.PacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class PacketRefuel {
    private static final Logger LOGGER = LogManager.getLogger();

    public PacketRefuel(){
    }

    public static void encode(PacketRefuel msg, FriendlyByteBuf buffer){
    }

    public static PacketRefuel decode(FriendlyByteBuf buffer){
        return new PacketRefuel();
    }

    public static class Handler {
        public static void handle(PacketRefuel msg, Supplier<NetworkEvent.Context> ctx){
            ctx.get().enqueueWork( ()-> {
                ServerPlayer player = ctx.get().getSender();
                if (player == null)
                    return;
                ItemStack tool = AbstractBurnerGun.getGun(player);
                if (tool.isEmpty() || !(tool.getItem() instanceof BurnerGunMK1)){
                    tool = AbstractBurnerSword.getSword(player);
                    if (tool.isEmpty() || !(tool.getItem() instanceof BurnerSwordMK1)){
                        return;
                    }
                }
                IItemHandler handler = tool.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
                ItemStack stack = handler.getStackInSlot(0);
                while (handler.getStackInSlot(0).getCount() > 0 && ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0){
                    if (BurnerGunNBT.getFuelValue(tool) + ForgeHooks.getBurnTime(handler.getStackInSlot(0), RecipeType.SMELTING) > AbstractBurnerGun.base_use_buffer)
                        break;
                    BurnerGunNBT.setFuelValue(tool, BurnerGunNBT.getFuelValue(tool) + ForgeHooks.getBurnTime(handler.getStackInSlot(0), RecipeType.SMELTING));
                    ItemStack containerItem = handler.getStackInSlot(0).getContainerItem();
                    handler.getStackInSlot(0).shrink(1);
                    if (!containerItem.isEmpty())
                        handler.insertItem(0, containerItem, false);
                }
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
