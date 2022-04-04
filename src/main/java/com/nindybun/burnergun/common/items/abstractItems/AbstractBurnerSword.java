package com.nindybun.burnergun.common.items.abstractItems;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.nindybun.burnergun.common.items.BurnerGunNBT;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AbstractBurnerSword extends Item {
    private int atkDmg;
    private float atkSpeed;
    private Multimap<Attribute, AttributeModifier> modifiers;
    private static final Logger LOGGER = LogManager.getLogger();

    public AbstractBurnerSword(int baseDmg, float baseAtkSpeed, Properties properties) {
        super(properties);/*
        this.atkDmg = baseDmg;
        this.atkSpeed = baseAtkSpeed;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", baseDmg, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", baseAtkSpeed, AttributeModifier.Operation.ADDITION));
        this.modifiers = builder.build();*/
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        LOGGER.info(BurnerGunNBT.getAtkDmg(stack));
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack sword = player.getItemInHand(hand);
        BurnerGunNBT.setAtkDmg(sword, BurnerGunNBT.getAtkDmg(sword)+1);
        return InteractionResultHolder.success(sword);
    }

    @Override
    public int getDamage(ItemStack stack) {
        return BurnerGunNBT.getAtkDmg(stack);
    }

    @Override
    public void inventoryTick(ItemStack tool, Level world, Entity entity, int slot, boolean held) {
        super.inventoryTick(tool, world, entity, slot, held);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack tool) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", BurnerGunNBT.getAtkDmg(tool), AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", BurnerGunNBT.getAtkSpeed(tool), AttributeModifier.Operation.ADDITION));
        return slot == EquipmentSlot.MAINHAND ? builder.build() : super.getAttributeModifiers(slot, tool);
    }

}
