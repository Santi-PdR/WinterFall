package com.santipdr.winterfall.common;

import com.santipdr.winterfall.WinterFall;
import com.santipdr.winterfall.config.WinterFallConfig;
import com.santipdr.winterfall.item.MeleeWeaponItem;
import com.santipdr.winterfall.perk.Perk;
import com.santipdr.winterfall.registry.ModEffects;
import com.santipdr.winterfall.wave.WaveManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WinterFall.MOD_ID)
public final class CommonGameEvents {
    private CommonGameEvents() {}

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide || event.player.isSpectator()) return;
        Player player = event.player;
        double cost = WinterFallConfig.SPRINT_STAMINA_COST.get();
        if (WinterFallPlayerData.perk(player) == Perk.RUNNER) cost *= .6D;
        if (player.isSprinting()) {
            if (!WinterFallPlayerData.consumeStamina(player, cost)) player.setSprinting(false);
        } else {
            WinterFallPlayerData.setStamina(player, WinterFallPlayerData.stamina(player) + WinterFallConfig.STAMINA_REGEN_PER_TICK.get());
        }
        if (player.tickCount % 100 == 0) WinterFallPlayerData.setThirst(player, WinterFallPlayerData.thirst(player) - 1);
        if (WinterFallPlayerData.thirst(player) <= 0 && player.tickCount % 40 == 0) player.hurt(player.damageSources().starve(), 1.0F);
        if (player.hasEffect(ModEffects.BLEEDING.get())) WinterFallPlayerData.setMorale(player, WinterFallPlayerData.morale(player) - (player.tickCount % 100 == 0 ? 1 : 0));
        if (player.tickCount % 10 == 0) WinterFallPlayerData.sync((ServerPlayer) player);
    }

    @SubscribeEvent
    public static void meleeStamina(AttackEntityEvent event) {
        if (event.getEntity().level().isClientSide || !(event.getEntity().getMainHandItem().getItem() instanceof MeleeWeaponItem weapon)) return;
        if (!WinterFallPlayerData.consumeStamina(event.getEntity(), weapon.profile().staminaCost())) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void modifyMeleeDamage(LivingHurtEvent event) {
        if (!(event.getSource().getEntity() instanceof Player player) || !(player.getMainHandItem().getItem() instanceof MeleeWeaponItem weapon)) return;
        float damage = weapon.profile().lightDamage() * WinterFallConfig.GLOBAL_DAMAGE_MULTIPLIER.get().floatValue();
        if (com.santipdr.winterfall.combat.CombatSystem.isBackstab(player, event.getEntity())) damage *= 1.35F;
        event.setAmount(damage);
        if (!player.level().isClientSide && player.getRandom().nextFloat() < weapon.profile().bleedChance()) {
            event.getEntity().addEffect(new MobEffectInstance(ModEffects.BLEEDING.get(), 120, 0));
            player.level().playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, .65F, .9F);
        }
    }

    @SubscribeEvent
    public static void wounds(LivingHurtEvent event) {
        if (!event.getEntity().level().isClientSide && event.getEntity() instanceof Player player && event.getAmount() >= 7.0F) {
            if (WinterFallPlayerData.perk(player) == Perk.SENTINEL) event.setAmount(event.getAmount() * .85F);
            int duration = WinterFallPlayerData.perk(player) == Perk.FIELD_MEDIC ? 180 : 280;
            player.addEffect(new MobEffectInstance(ModEffects.WOUNDED.get(), duration, 0));
        }
    }

    @SubscribeEvent
    public static void clone(PlayerEvent.Clone event) {
        WinterFallPlayerData.copy(event.getOriginal(), event.getEntity());
    }

    @SubscribeEvent
    public static void login(PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) WinterFallPlayerData.sync(player);
    }

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        ServerLevel overworld = event.getServer().overworld();
        WaveManager.tick(overworld);
    }

    @SubscribeEvent
    public static void commands(RegisterCommandsEvent event) {
        com.santipdr.winterfall.util.WinterFallCommands.register(event.getDispatcher());
    }
}
