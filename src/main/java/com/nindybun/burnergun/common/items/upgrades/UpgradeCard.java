package com.nindybun.burnergun.common.items.upgrades;

import com.nindybun.burnergun.common.BurnerGun;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public class UpgradeCard extends Item {
    Upgrade upgrade;

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<net.minecraft.network.chat.Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (stack.getItem() instanceof UpgradeCard){
            Upgrade upgrade = ((UpgradeCard)stack.getItem()).upgrade;
            tooltip.add(new TextComponent("Cost: " +  (!(this.getUpgrade().equals(Upgrade.AUTO_SMELT)) ? upgrade.getCost()+"" : "[50, 175]")).withStyle(ChatFormatting.AQUA));
            tooltip.add(new TranslatableComponent(this.upgrade.getToolTip())
                    .append(this.getUpgrade().getBaseName().equals(Upgrade.FOCAL_POINT_1.getBaseName()) ? this.upgrade.getExtraValue() + " blocks." : "")
                    .append(this.getUpgrade().getBaseName().equals(Upgrade.FUEL_EFFICIENCY_1.getBaseName()) ? this.upgrade.getExtraValue()*100 + "%" : "")
                    .append(this.getUpgrade().getBaseName().equals(Upgrade.HORIZONTAL_EXPANSION_1.getBaseName()) ||
                            this.getUpgrade().getBaseName().equals(Upgrade.VERTICAL_EXPANSION_1.getBaseName()) ? this.upgrade.getTier()*2+1 + "" : "")
                    .withStyle(ChatFormatting.GRAY));
        }
    }

    public UpgradeCard(Upgrade upgrade) {
        super(new Properties().tab(BurnerGun.itemGroup).stacksTo(upgrade.getStackSize()));
        this.upgrade = upgrade;
    }

    public Upgrade getUpgrade(){
        return upgrade;
    }
}
