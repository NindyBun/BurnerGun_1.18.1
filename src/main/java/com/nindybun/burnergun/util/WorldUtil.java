package com.nindybun.burnergun.util;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public final class WorldUtil {

    private static final Logger LOGGER = LogManager.getLogger();

    public static BlockHitResult getLookingAt(Level world, Player player, ClipContext.Fluid rayTraceFluid, double range) {
        Vec3 look = player.getLookAngle();
        Vec3 start = player.position().add(new Vec3(0, player.getEyeHeight(), 0));
        Vec3 end = new Vec3(player.getX() + look.x * range, player.getY() + player.getEyeHeight() + look.y * range, player.getZ() + look.z * range);
        ClipContext context = new ClipContext(start, end, ClipContext.Block.COLLIDER, rayTraceFluid, player);
        return world.clip(context);
    }

    public static Vec3 getDim(BlockHitResult ray, int xRad, int yRad, Player player){
        //Z Face mining by default
        int xRange = xRad;
        int yRange = yRad;
        int zRange = 0;
        //X Face Mining
        if (Math.abs(ray.getDirection().getNormal().getX()) == 1){
            zRange = xRad;
            xRange = 0;
        }
        //Vertical Mining needs to act like the Horizontal but based on yaw
        if (Math.abs(ray.getDirection().getNormal().getY()) == 1){
            yRange = 0;
            int yaw = (int)player.getYHeadRot()%360;
            int facing = Math.abs(yaw / 45);

            if (facing == 6 || facing == 5 || facing == 2 || facing == 1) { //X axis
                xRange = yRad;
                zRange = xRad;
            }
            if (facing == 7 || facing == 8 || facing == 0 || facing == 4 || facing == 3) { //Z axis
                xRange = xRad;
                zRange = yRad;
            }

        }
        return new Vec3(xRange, yRange, zRange);
    }
}
