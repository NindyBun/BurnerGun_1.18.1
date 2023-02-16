package com.nindybun.burnergun.common.items.testitems;

import com.nindybun.burnergun.BurnerGun;
import net.minecraft.world.item.Item;

public class TestItemTool extends Item {
    public TestItemTool() {
        super(new Properties().stacksTo(1).setNoRepair().tab(BurnerGun.itemGroup));
    }
}
