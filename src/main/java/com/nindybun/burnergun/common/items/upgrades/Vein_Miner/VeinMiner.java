package com.nindybun.burnergun.common.items.upgrades.Vein_Miner;

import com.nindybun.burnergun.client.screens.ModScreens;
import com.nindybun.burnergun.common.containers.ModContainers;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.common.items.upgrades.UpgradeCard;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VeinMiner extends UpgradeCard {
    Upgrade upgrade;
    public static final Logger LOGGER = LogManager.getLogger();

    public VeinMiner(Upgrade upgrade) {
        super(upgrade);
        this.upgrade = upgrade;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide){
            List<Blocks> blockList = ForgeRegistries.BLOCKS.getValues().stream().flatMap(block -> {

            });
            ModScreens.openFilterListScreen(stack);
        }
        return InteractionResultHolder.success(stack);
    }
}
