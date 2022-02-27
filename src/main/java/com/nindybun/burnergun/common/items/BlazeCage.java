package com.nindybun.burnergun.common.items;

import com.nindybun.burnergun.common.BurnerGun;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BlazeCage extends Item {
    private static final Logger LOGGER = LogManager.getLogger();
    public BlazeCage() {
        super(new Properties().stacksTo(16).tab(BurnerGun.itemGroup));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        if (!player.level.isClientSide){
            if(entity.isAlive() && entity.getType().equals(EntityType.BLAZE)){
                player.getItemInHand(hand).shrink(1);
                if (player.getItemInHand(hand).isEmpty()){
                    IItemHandler playerHandler = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
                    playerHandler.insertItem(hand.equals(InteractionHand.MAIN_HAND) ? EquipmentSlot.MAINHAND.getIndex() : EquipmentSlot.OFFHAND.getIndex(), new ItemStack(ModItems.CAGED_BLAZE.get()), false);
                }
                else if(!player.getInventory().add(new ItemStack(ModItems.CAGED_BLAZE.get())))
                    player.drop(new ItemStack(ModItems.CAGED_BLAZE.get()), false);
            }
            entity.remove(Entity.RemovalReason.DISCARDED);
        }
        return InteractionResult.PASS;
    }
}
