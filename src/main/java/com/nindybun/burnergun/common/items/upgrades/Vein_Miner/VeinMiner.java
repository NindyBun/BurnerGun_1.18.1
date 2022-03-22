package com.nindybun.burnergun.common.items.upgrades.Vein_Miner;

import com.nindybun.burnergun.client.screens.ModScreens;
import com.nindybun.burnergun.common.containers.ModContainers;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.common.items.upgrades.UpgradeCard;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.units.qual.C;

import java.util.List;
import java.util.stream.Collectors;

public class VeinMiner extends UpgradeCard {
    Upgrade upgrade;
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String BLOCK = "Block";
    public static final String WHITELIST = "Whitelist";
    public static final String VEIN_MINER_FILTER = "VeinMinerFilter";

    public VeinMiner(Upgrade upgrade) {
        super(upgrade);
        this.upgrade = upgrade;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide){
            ListTag filterTag = stack.getOrCreateTag().getList(VEIN_MINER_FILTER, Tag.TAG_COMPOUND);
            ListTag filterTagString = new ListTag();
            filterTag.forEach(tag -> {
                filterTagString.add(((CompoundTag)tag).getString(BLOCK))
            });
            List<Block> blockList = ForgeRegistries.BLOCKS.getValues().stream().collect(Collectors.toList());
            blockList.forEach(block -> {
                CompoundTag tag = new CompoundTag();
                tag.putString(BLOCK, block.toString());
                if (!filterTag.contains(tag)){
                    tag.putBoolean(WHITELIST, false);
                    filterTag.add(tag);
                }
            });
            stack.getOrCreateTag().put(VEIN_MINER_FILTER, filterTag);
            //ModScreens.openFilterListScreen(stack);
        }
        return InteractionResultHolder.success(stack);
    }
}
