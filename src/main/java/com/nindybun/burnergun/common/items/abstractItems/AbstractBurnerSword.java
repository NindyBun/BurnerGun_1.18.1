package com.nindybun.burnergun.common.items.abstractItems;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.nindybun.burnergun.client.entities.ModEntities;
import com.nindybun.burnergun.client.entities.testArrowEntity.TestArrowEntity;
import com.nindybun.burnergun.common.items.BurnerGunNBT;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.*;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class AbstractBurnerSword extends Item {
    private static final double base_use = 100;
    public static final double base_use_buffer = 20_000;
    private static final Logger LOGGER = LogManager.getLogger();

    public AbstractBurnerSword(Properties properties) {
        super(properties);
    }

    public static IItemHandler getHandler(ItemStack itemStack) {
        return itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
    }

    public static ItemStack getSword(Player player) {
        ItemStack heldItem = player.getMainHandItem();
        if (!(heldItem.getItem() instanceof AbstractBurnerSword)) {
            heldItem = player.getOffhandItem();
            if (!(heldItem.getItem() instanceof AbstractBurnerSword)) {
                return ItemStack.EMPTY;
            }
        }
        return heldItem;
    }

    @Override
    public boolean canAttackBlock(BlockState p_41441_, Level p_41442_, BlockPos p_41443_, Player player) {
        return !player.isCreative();
    }

    @Override
    public float getDestroySpeed(ItemStack p_41425_, BlockState state) {
        if (state.is(Blocks.COBWEB)) {
            return 15.0F;
        } else {
            Material material = state.getMaterial();
            return material != Material.PLANT && material != Material.REPLACEABLE_PLANT && !state.is(BlockTags.LEAVES) && material != Material.VEGETABLE ? 1.0F : 1.5F;
        }
    }

    @Override
    public boolean mineBlock(ItemStack tool, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        if (state.getDestroySpeed(level, pos) != 0.0F) {
            tool.hurtAndBreak(0, entity, (entity1) -> {
                entity1.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }

        return true;
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return state.is(Blocks.COBWEB);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_SWORD_ACTIONS.contains(toolAction);
    }

    @Override
    public int getDamage(ItemStack stack) {
        return 1+BurnerGunNBT.getAtkDmg(stack);
    }

    @Override
    public boolean hurtEnemy(ItemStack tool, LivingEntity entity, LivingEntity player) {
        LOGGER.info("HURTING");
        return super.hurtEnemy(tool, entity, player);
    }

    public Entity getEntityPlayerLookingAt(Level level, Player player, double range){
        Vec3 look = player.getLookAngle();
        Vec3 start = player.position().add(new Vec3(0, player.getEyeHeight(), 0));
        for (double i = 0.5; i <= range; i+=0.5){
            Vec3 point = new Vec3(  player.getX() + look.x * i,
                                    player.getY() + player.getEyeHeight() + look.y * i,
                                    player.getZ() + look.z * i);
            List<Entity> entities = level.getEntities(player, new AABB(point, point));
            if (!entities.isEmpty()){
                for (Entity e : entities) {
                    BlockHitResult result = level.clip(new ClipContext(start, point, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
                    if (e.isAlive() && result.getType() != HitResult.Type.BLOCK)
                        return e;
                }
            }
        }
        return null;
    }

    @Override
    public boolean onEntitySwing(ItemStack tool, LivingEntity livingEntity) {
        if (livingEntity instanceof Player){
            Player player = (Player)livingEntity;
            Level level = player.level;
            if (!level.isClientSide) {
                if (player.isCrouching()){
                    /*ArrowItem arrowitem = (ArrowItem)(Items.ARROW);
                    AbstractArrow abstractarrow = arrowitem.createArrow(level, Items.ARROW.getDefaultInstance(), player);
                    abstractarrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3F, 0.0F);
                    abstractarrow.pickup = AbstractArrow.Pickup.DISALLOWED;
                    abstractarrow.setNoGravity(false);
                    level.addFreshEntity(abstractarrow);*/
                    TestArrowEntity test = new TestArrowEntity(ModEntities.TEST_ARROW_ENTITY.get(), player, level);
                    test.shootFromRotation(player, player.getXRot(), player.getYRot(), 0F, 3F, 0F);
                    level.addFreshEntity(test);
                    /*if (BurnerGunNBT.getAtkCoolDown(tool) <= 0){
                        BurnerGunNBT.setAtkCoolDown(tool, 1);
                        level.addFreshEntity(abstractarrow);
                    }
                    SmallFireball smallFireball = new SmallFireball(EntityType.SMALL_FIREBALL, level);
                    if (!player.getCooldowns().isOnCooldown(tool.getItem())){
                        level.addFreshEntity(abstractarrow);
                        player.getCooldowns().addCooldown(tool.getItem(), 10);
                    }*/
                }
            }
        }
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack tool = player.getItemInHand(hand);
        if (!level.isClientSide){
            Entity entity = getEntityPlayerLookingAt(level, player, BurnerGunNBT.getRaycast(tool));
            if (entity != null){
            }
        }
        return InteractionResultHolder.pass(tool);
    }

    @Override
    public void inventoryTick(ItemStack tool, Level level, Entity player, int slot, boolean held) {
        super.inventoryTick(tool, level, player, slot, held);
        /*if (BurnerGunNBT.getAtkCoolDown(tool) > 0)
            BurnerGunNBT.setAtkCoolDown(tool, BurnerGunNBT.getAtkCoolDown(tool)-0.1f);
        else if (BurnerGunNBT.getAtkCoolDown(tool) <= 0)
            BurnerGunNBT.setAtkCoolDown(tool, 0);*/
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack tool) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(tool);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", -1+BurnerGunNBT.getAtkDmg(tool), AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier( BASE_ATTACK_SPEED_UUID, "Weapon modifier", -4+BurnerGunNBT.getAtkSpeed(tool), AttributeModifier.Operation.ADDITION));
        return slot == EquipmentSlot.MAINHAND ? builder.build() : super.getAttributeModifiers(slot, tool);
    }

}
