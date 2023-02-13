package com.nindybun.burnergun.client.entities.testArrowEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.nindybun.burnergun.BurnerGun;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class TestArrowRenderer extends EntityRenderer<TestArrowEntity> {
    private static final ResourceLocation TEST_ARROW_TEXTURE = new ResourceLocation(BurnerGun.MOD_ID, "textures/entity/test_arrow.pn");

    public TestArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(TestArrowEntity p_113839_, float p_113840_, float p_113841_, PoseStack p_113842_, MultiBufferSource p_113843_, int p_113844_) {
        super.render(p_113839_, p_113840_, p_113841_, p_113842_, p_113843_, p_113844_);
    }

    @Override
    public ResourceLocation getTextureLocation(TestArrowEntity arrow) {
        return TEST_ARROW_TEXTURE;
    }
}
