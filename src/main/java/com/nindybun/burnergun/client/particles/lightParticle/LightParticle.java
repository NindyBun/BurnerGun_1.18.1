package com.nindybun.burnergun.client.particles.lightParticle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;

public class LightParticle extends TextureSheetParticle {
    public LightParticle(ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double speedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, speedIn);
        this.xd *= (double) 0.03F;
        this.yd *= (double) 0.03F;
        this.zd *= (double) 0.03F;
        this.quadSize = this.random.nextFloat() * 0.1F + 0.1f;
        this.setSize(0.02f, 0.02f);
        this.age = ((int)(20.0D / (Math.random() * 0.8D + 0.2D)));
        this.alpha = 1.0f;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

   /* public int getLightColor(float p_189214_1_) {
        float f = ((float)this.age + p_189214_1_) / (float)this.lifetime;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        int i = super.getLightColor(p_189214_1_);
        int j = i & 255;
        int k = i >> 16 & 255;
        j = j + (int)(f * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }

        return j | k << 16;
    }*/

    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().move(x, y, z));
        this.setLocationFromBoundingbox();
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age-- <= 0) {
            this.remove();
        } else {
            this.move(this.xd, this.yd, this.zd);
            this.xd *= 0.99D;
            this.yd *= 0.99D;
            this.zd *= 0.99D;
        }
    }
}
