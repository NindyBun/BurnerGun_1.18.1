package com.nindybun.burnergun.common.blocks;

import com.nindybun.burnergun.BurnerGun;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BurnerGun.MOD_ID);

    public static final RegistryObject<Block> LIGHT = BLOCKS.register("light", Light::new);
}
