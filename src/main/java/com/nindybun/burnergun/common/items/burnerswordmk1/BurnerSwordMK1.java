package com.nindybun.burnergun.common.items.burnerswordmk1;

import com.nindybun.burnergun.BurnerGun;
import com.nindybun.burnergun.common.items.abstractItems.AbstractBurnerSword;

public class BurnerSwordMK1 extends AbstractBurnerSword {
    public BurnerSwordMK1() {
        super(5, -2.4f, new Properties().stacksTo(1).setNoRepair().tab(BurnerGun.itemGroup));
    }
}
