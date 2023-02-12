package com.nindybun.burnergun.common.network.packets;

import com.nindybun.burnergun.common.blocks.ModBlocks;
import com.nindybun.burnergun.common.items.BurnerGunNBT;
import com.nindybun.burnergun.common.items.abstractItems.AbstractBurnerGun;
import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.common.network.PacketHandler;
import com.nindybun.burnergun.util.UpgradeUtil;
import com.nindybun.burnergun.util.WorldUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
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

                ItemStack gun = AbstractBurnerGun.getGun(player);
                if (gun.isEmpty())
                    return;

                List<Upgrade> upgrades = BurnerGunNBT.getUpgrades(gun);
                if (UpgradeUtil.containsUpgradeFromList(upgrades, Upgrade.LIGHT)){
                    if (gun.getItem() instanceof BurnerGunMK1 && BurnerGunNBT.getFuelValue(gun) < Upgrade.LIGHT.getCost())
                        return;
                    BlockHitResult ray = WorldUtil.getLookingAt(player.level, player, ClipContext.Fluid.NONE, BurnerGunNBT.getRaycast(gun));
                    BlockState state = player.level.getBlockState(ray.getBlockPos().relative(ray.getDirection()));
                    if (!player.level.mayInteract(player, ray.getBlockPos())
                            || !player.getAbilities().mayBuild)
                        return;
                    if (ray.getType() == HitResult.Type.MISS) {
                        if (gun.getItem() instanceof BurnerGunMK1){
                            if (BurnerGunNBT.getFuelValue(gun) >= Upgrade.LIGHT.getCost())
                                BurnerGunNBT.setFuelValue(gun, BurnerGunNBT.getFuelValue(gun)-Upgrade.LIGHT.getCost());
                            else
                                return;
                        }
                        PacketHandler.sendTo(new PacketClientPlayLightSound(BurnerGunNBT.getVolume(gun)), player);
                        player.level.setBlockAndUpdate(ray.getBlockPos(), ModBlocks.LIGHT.get().defaultBlockState());
                        return;
                    }
                    if ((ray.getType() == HitResult.Type.BLOCK)
                            && (state == Blocks.AIR.defaultBlockState()
                            || state == Blocks.CAVE_AIR.defaultBlockState()
                            || (state.getFluidState().isSource() && !state.hasProperty(BlockStateProperties.WATERLOGGED))
                            || (state.getFluidState().getAmount() > 0 && !state.hasProperty(BlockStateProperties.WATERLOGGED)))
                            ){
                        if (gun.getItem() instanceof BurnerGunMK1){
                            if (BurnerGunNBT.getFuelValue(gun) >= Upgrade.LIGHT.getCost())
                                BurnerGunNBT.setFuelValue(gun, BurnerGunNBT.getFuelValue(gun)-Upgrade.LIGHT.getCost());
                            else
                                return;
                        }
                        PacketHandler.sendTo(new PacketClientPlayLightSound(BurnerGunNBT.getVolume(gun)), player);
                        player.level.setBlockAndUpdate(ray.getBlockPos().relative(ray.getDirection()), ModBlocks.LIGHT.get().defaultBlockState());
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}