package com.nindybun.burnergun.common.items.abstractItems;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.nindybun.burnergun.common.BurnerGun;
import com.nindybun.burnergun.common.items.BurnerGunNBT;
import com.nindybun.burnergun.util.WorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.*;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class AbstractBurnerSword extends Item {
    private static final Logger LOGGER = LogManager.getLogger();

    public AbstractBurnerSword(Properties properties) {
        super(properties);
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
        return BurnerGunNBT.getAtkDmg(stack);
    }

    @Override
    public boolean hurtEnemy(ItemStack tool, LivingEntity entity, LivingEntity player) {
        return super.hurtEnemy(tool, entity, player);
    }

    public static EntityHitResult getPlayerPOVHitResult(Player player, double range) {
        /*float playerRotX = player.getXRot();
        float playerRotY = player.getYRot();
        Vec3 startPos = player.getEyePosition();
        float f2 = Mth.cos(-playerRotY * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = Mth.sin(-playerRotY * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -Mth.cos(-playerRotX * ((float)Math.PI / 180F));
        float additionY = Mth.sin(-playerRotX * ((float)Math.PI / 180F));
        float additionX = f3 * f4;
        float additionZ = f2 * f4;
        double range = range;
        Vec3 endVec = startPos.add((double)additionX * range, (double)additionY * range, (double)additionZ * range);
*/
        Vec3 look = player.getLookAngle();
        Vec3 startPos = player.position().add(new Vec3(0, player.getEyeHeight(), 0));
        Vec3 endVec = new Vec3(player.getX() + look.x * range, player.getY() + player.getEyeHeight() + look.y * range, player.getZ() + look.z * range);
        AABB startEndBox = new AABB(startPos, endVec);

        Entity entity = null;
        for(Entity entity1 : player.level.getEntities(player, startEndBox, (val) -> true)) {
            AABB aabb = entity1.getBoundingBox().inflate(entity1.getPickRadius());
            Optional<Vec3> optional = aabb.clip(startPos, endVec);
            if (aabb.contains(startPos)) {
                if (range >= 0.0D) {
                    entity = entity1;
                    startPos = optional.orElse(startPos);
                    range = 0.0D;
                }
            } else if (optional.isPresent()) {
                Vec3 vec31 = optional.get();
                double d1 = startPos.distanceToSqr(vec31);
                if (d1 < range || range == 0.0D) {
                    if (entity1.getRootVehicle() == player.getRootVehicle() && !entity1.canRiderInteract()) {
                        if (range == 0.0D) {
                            entity = entity1;
                            startPos = vec31;
                        }
                    } else {
                        entity = entity1;
                        startPos = vec31;
                        range = d1;
                    }
                }
            }
        }

        return (entity == null) ? null:new EntityHitResult(entity);
    }

    public Entity getEntityPlayerLookingAt(Level level, Player player, double range){
        Vec3 look = player.getLookAngle();
        for (int i = 1; i < range; i++){
            Vec3 point = new Vec3(  player.getX() + look.x * i,
                                    player.getY() + player.getEyeHeight() + look.y * i,
                                    player.getZ() + look.z * i);
            List<Entity> entities = level.getEntities(player, new AABB(point, point));
            if (!entities.isEmpty()){
                for (Entity e : entities) {
                    BlockHitResult result = WorldUtil.getLookingAt(level, player, ClipContext.Fluid.NONE, e.distanceTo(player));
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
            /*int range = 10;
            Vec3 look = player.getLookAngle();
            Vec3 start = player.position().add(new Vec3(0, player.getEyeHeight(), 0));
            Vec3 end = new Vec3(player.getX() + look.x * range, player.getY() + player.getEyeHeight() + look.y * range, player.getZ() + look.z * range);
            List<Entity> entity = level.getEntities(player, new AABB(start, end));
            List<Entity> ent = new ArrayList<>();
            for (int i = 0; i < entity.size(); i++) {
                Entity e = entity.get(i);
                if (e instanceof Mob)
                        ent.add(e);
            }
            LOGGER.info(getPlayerPOVHitResult(player, range).getEntity());*/
            if (!level.isClientSide){
                Entity entity = getEntityPlayerLookingAt(level, player, 10);
                if (entity != null)
                    player.attack(entity);
                /*if (entity != null){
                    Entity closest = ent.get(0);
                    double range2 = 0;
                    for (Entity e : ent) {
                        double curr = e.position().distanceTo(player.position());
                        double old = closest.position().distanceTo(player.position());
                        if (old > curr){
                            closest = e;
                            range2 = curr;
                        }
                    }
                    if (closest.isAlive() && BurnerGunNBT.getAtkCoolDown(tool) <= 0){
                        player.attack(closest);
                        BurnerGunNBT.setAtkCoolDown(tool, 2f/(4+BurnerGunNBT.getAtkSpeed(tool)));
                        return true;
                    }
                    Vec3 end2 = new Vec3(player.getX() + look.x * range2, player.getY() + player.getEyeHeight() + look.y * range2, player.getZ() + look.z * range2);
                    ClipContext context = new ClipContext(start, end2, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player);
                    //LOGGER.info(level.clip(context).getType());
                    if (entity.getType() == HitResult.Type.ENTITY)
                        player.attack(entity.getEntity());
                }*/
            }
        }
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack tool = player.getItemInHand(hand);
        if (!level.isClientSide){
            /*ArrowItem arrowItem = (ArrowItem)Items.ARROW;
            AbstractArrow arrow = arrowItem.createArrow(level, Items.ARROW.getDefaultInstance(), player);
            arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 1.0F);
            arrow.setBaseDamage(arrow.getBaseDamage()+8d);

            level.addFreshEntity(arrow);
            level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
        */
        }
        return super.use(level, player, hand);
    }

    @Override
    public void inventoryTick(ItemStack tool, Level world, Entity player, int slot, boolean held) {
        super.inventoryTick(tool, world, player, slot, held);
        if (BurnerGunNBT.getAtkCoolDown(tool) > 0)
            BurnerGunNBT.setAtkCoolDown(tool, BurnerGunNBT.getAtkCoolDown(tool)-0.1f);
        else if (BurnerGunNBT.getAtkCoolDown(tool) <= 0)
            BurnerGunNBT.setAtkCoolDown(tool, 0);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack tool) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(tool);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", BurnerGunNBT.getAtkDmg(tool), AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier( BASE_ATTACK_SPEED_UUID, "Weapon modifier", BurnerGunNBT.getAtkSpeed(tool), AttributeModifier.Operation.ADDITION));
        return slot == EquipmentSlot.MAINHAND ? builder.build() : super.getAttributeModifiers(slot, tool);
    }

}
