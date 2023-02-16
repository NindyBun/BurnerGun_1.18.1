package com.nindybun.burnergun.common.containers;

import com.nindybun.burnergun.BurnerGun;
import com.nindybun.burnergun.common.items.testitems.TestItemBag;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModContainers {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, BurnerGun.MOD_ID);

    public static final RegistryObject<MenuType<BurnerGunMK1Container>> BURNERGUNMK1_CONTAINER = CONTAINERS.register("burnergunmk1_container", () -> IForgeMenuType.create(BurnerGunMK1Container::new));
    public static final RegistryObject<MenuType<BurnerGunMK2Container>> BURNERGUNMK2_CONTAINER = CONTAINERS.register("burnergunmk2_container", () -> IForgeMenuType.create(BurnerGunMK2Container::new));

    public static final RegistryObject<MenuType<BurnerSwordMK1Container>> BURNERSWORDMK1_CONTAINER = CONTAINERS.register("burnerswordmk1_container", () -> IForgeMenuType.create(BurnerSwordMK1Container::new));

    public static final RegistryObject<MenuType<TrashContainer>> TRASH_CONTAINER = CONTAINERS.register("trash_container", () -> IForgeMenuType.create(TrashContainer::new));
    public static final RegistryObject<MenuType<AutoSmeltContainer>> AUTO_SMELT_CONTAINER = CONTAINERS.register("auto_smelt_container", () -> IForgeMenuType.create(AutoSmeltContainer::new));

    public static final RegistryObject<MenuType<TestItemBagContainer>> TESTITEMBAG_CONTAINER = CONTAINERS.register("testitembag_container", () -> IForgeMenuType.create(TestItemBagContainer::new));

    }
