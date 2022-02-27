package com.nindybun.burnergun.common.network.packets;

import com.nindybun.burnergun.common.BurnerGun;
import com.nindybun.burnergun.common.blocks.ModBlocks;
import com.nindybun.burnergun.common.items.BurnerGunNBT;
import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.util.UpgradeUtil;
import com.nindybun.burnergun.util.WorldUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.function.Supplier;

public class PacketSpawnLightAtRaycast {
    private static final Logger LOGGER = LogManager.getLogger();

    public PacketSpawnLightAtRaycast() {
    }

    public static void encode(PacketSpawnLightAtRaycast msg, FriendlyByteBuf buffer) {
    }

    public static PacketSpawnLightAtRaycast decode(FriendlyByteBuf buffer) {
        return new PacketSpawnLightAtRaycast();
    }

    public static class Handler {
        public static void handle(PacketSpawnLightAtRaycast msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                if (player == null)
                    return;

                ItemStack gun = !BurnerGunMK2.getGun(player).isEmpty() ? BurnerGunMK2.getGun(player) : BurnerGunMK1.getGun(player);
                if (gun.isEmpty())
                    return;
                List<Upgrade> upgrades = BurnerGunNBT.getUpgrades(gun);
                if (UpgradeUtil.containsUpgradeFromList(upgrades, Upgrade.LIGHT)){
                    if (gun.getItem() instanceof BurnerGunMK1 && BurnerGunNBT.getFuelValue(gun) < Upgrade.LIGHT.getCost())
                        return;
                    BlockHitResult ray = WorldUtil.getLookingAt(player.level, player, ClipContext.Fluid.NONE, BurnerGunNBT.getRaycast(gun));
                    BlockState state = player.level.getBlockState(ray.getBlockPos());
                    if (gun.getItem() instanceof BurnerGunMK1 && (state == Blocks.AIR.defaultBlockState() || player.level.getBlockState(ray.getBlockPos().relative(ray.getDirection())) == Blocks.AIR.defaultBlockState())){
                        if (BurnerGunNBT.getFuelValue(gun) >= Upgrade.LIGHT.getCost())
                            BurnerGunNBT.setFuelValue(gun, BurnerGunNBT.getFuelValue(gun)-Upgrade.LIGHT.getCost());
                        else
                            return;
                    }
                    if (state == Blocks.AIR.defaultBlockState()) {
                        player.level.setBlockAndUpdate(ray.getBlockPos(), ModBlocks.LIGHT.get().defaultBlockState());
                        return;
                    }
                    if (state != Blocks.AIR.defaultBlockState() && player.level.getBlockState(ray.getBlockPos().relative(ray.getDirection())) == Blocks.AIR.defaultBlockState())
                        player.level.setBlockAndUpdate(ray.getBlockPos().relative(ray.getDirection()), ModBlocks.LIGHT.get().defaultBlockState());
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}