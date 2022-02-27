package com.nindybun.burnergun.client.particles.lightParticle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.awt.*;


public class LightParticleType extends SimpleParticleType {
    public LightParticleType() {
        super(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static class LightParticleFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet SPRITE;
        public LightParticleFactory(SpriteSet sprite){
            this.SPRITE = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType basicParticleType, ClientLevel world, double x, double y, double z, double xS, double yS, double zS) {
            LightParticle particle = new LightParticle(world, x, y, z, xS, yS, zS);
            particle.setSpriteFromAge(this.SPRITE);
            return particle;
        }
    }
}
