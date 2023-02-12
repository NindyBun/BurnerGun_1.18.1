package com.nindybun.burnergun.client.entities.testArrowEntity;

import com.nindybun.burnergun.client.entities.ModEntities;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestArrowEntity extends AbstractArrow {
    public final Logger LOGGER = LogManager.getLogger();

    public TestArrowEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public TestArrowEntity(EntityType<? extends AbstractArrow> entityType, double x, double y, double z, Level level) {
        super(entityType, x, y, z, level);
    }

    public TestArrowEntity(EntityType<? extends AbstractArrow> entityType, LivingEntity shooter, Level level) {
        super(entityType, shooter, level);
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        LOGGER.info(this.level.getBlockState(hitResult.getBlockPos()));
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
