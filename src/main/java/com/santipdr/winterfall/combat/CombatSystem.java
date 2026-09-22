package com.santipdr.winterfall.combat;

import com.santipdr.winterfall.config.WinterFallConfig;
import com.santipdr.winterfall.registry.ModEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public final class CombatSystem {
    private CombatSystem() {}

    public record AimedHit(LivingEntity target, Vec3 location) {}

    public static LivingEntity aimedTarget(Player player, double range, double spreadDegrees) {
        AimedHit hit = aimedHit(player, range, spreadDegrees);
        return hit == null ? null : hit.target();
    }

    public static AimedHit aimedHit(Player player, double range, double spreadDegrees) {
        Vec3 start = player.getEyePosition();
        Vec3 look = player.getLookAngle();
        if (spreadDegrees > 0.0D) {
            double spread = Math.tan(Math.toRadians(spreadDegrees)) * 0.5D;
            look = look.add(player.getRandom().nextGaussian() * spread, player.getRandom().nextGaussian() * spread, player.getRandom().nextGaussian() * spread).normalize();
        }
        Vec3 end = start.add(look.scale(range));
        AABB search = player.getBoundingBox().expandTowards(look.scale(range)).inflate(1.2D);
        EntityHitResult hit = ProjectileUtil.getEntityHitResult(player, start, end, search,
                entity -> entity instanceof LivingEntity living && living.isAlive() && entity != player && entity.isPickable(), 0.0D);
        return hit != null && hit.getEntity() instanceof LivingEntity living ? new AimedHit(living, hit.getLocation()) : null;
    }

    public static void heavyAttack(ServerPlayer player, float reach, float damage, float knockback, float bleedChance) {
        LivingEntity target = aimedTarget(player, reach, 0.0D);
        if (target == null) return;
        float finalDamage = damage * WinterFallConfig.GLOBAL_DAMAGE_MULTIPLIER.get().floatValue();
        if (isBackstab(player, target)) finalDamage *= 1.35F;
        target.hurt(player.damageSources().playerAttack(player), finalDamage);
        target.knockback(knockback, player.getX() - target.getX(), player.getZ() - target.getZ());
        if (player.getRandom().nextFloat() < bleedChance) {
            target.addEffect(new MobEffectInstance(ModEffects.BLEEDING.get(), 140, 0));
        }
    }

    public static boolean isBackstab(Player player, LivingEntity target) {
        Vec3 targetFacing = target.getLookAngle().normalize();
        Vec3 targetToPlayer = player.position().subtract(target.position()).normalize();
        return targetFacing.dot(targetToPlayer) > 0.55D;
    }

    public static boolean isHeadshot(LivingEntity target, Vec3 hitPoint) {
        return hitPoint.y >= target.getY() + target.getBbHeight() * 0.72D;
    }
}
