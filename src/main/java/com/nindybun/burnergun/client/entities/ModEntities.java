package com.nindybun.burnergun.client.entities;

import com.nindybun.burnergun.BurnerGun;
import com.nindybun.burnergun.client.entities.testArrowEntity.TestArrowEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, BurnerGun.MOD_ID);

    public static final RegistryObject<EntityType<TestArrowEntity>> TEST_ARROW_ENTITY = ENTITIES.register("test_arrow",
            () -> EntityType.Builder.<TestArrowEntity>of(TestArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("test_arrow"));
}
