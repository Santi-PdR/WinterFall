package com.santipdr.winterfall.wave;

import com.santipdr.winterfall.config.WinterFallConfig;
import com.santipdr.winterfall.entity.CombatKnifeScavenger;
import com.santipdr.winterfall.network.ModNetworking;
import com.santipdr.winterfall.network.WaveStatePacket;
import com.santipdr.winterfall.registry.ModEntityTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public final class WaveManager {
    private WaveManager() {}

    public static WinterFallWaveData data(ServerLevel level) {
        return WinterFallWaveData.get(level);
    }

    public static void start(ServerLevel level, int wave) {
        WinterFallWaveData data = data(level);
        data.start(wave, WinterFallConfig.WAVE_DURATION_SECONDS.get() * 20);
        level.players().forEach(player -> {
            player.displayClientMessage(Component.translatable("message.winterfall.wave_started", data.wave()).withStyle(ChatFormatting.RED), false);
            ModNetworking.sendToPlayer(player, packet(data));
        });
    }

    public static void tick(ServerLevel level) {
        WinterFallWaveData data = data(level);
        if (!data.active()) return;
        data.tick();
        if (data.spawnTimer() <= 0) {
            int amount = WinterFallConfig.WAVE_BASE_SCAVENGERS.get() + data.wave() / 2;
            if (data.bossIncoming()) amount += 3;
            for (int i = 0; i < amount; i++) spawnScavenger(level, data.wave(), data.bossIncoming() && i == 0);
            data.resetSpawnTimer(Math.max(90, 260 - data.wave() * 14));
        }
        int safe = WinterFallConfig.SAFE_ZONE_RADIUS.get();
        BlockPos spawn = level.getSharedSpawnPos();
        for (ServerPlayer player : level.players()) {
            if (player.position().distanceToSqr(spawn.getX() + .5D, spawn.getY(), spawn.getZ() + .5D) > safe * safe) {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0, true, false, true));
                if (player.tickCount % 80 == 0) player.hurt(level.damageSources().freeze(), 1.0F);
            }
            if (player.tickCount % 20 == 0) ModNetworking.sendToPlayer(player, packet(data));
        }
        if (!data.active()) {
            level.players().forEach(player -> player.displayClientMessage(Component.translatable("message.winterfall.wave_cleared", data.wave()), false));
        }
    }

    public static void spawnScavenger(ServerLevel level, int wave, boolean elite) {
        ServerPlayer target = level.players().isEmpty() ? null : level.players().get(level.random.nextInt(level.players().size()));
        if (target == null) return;
        CombatKnifeScavenger scavenger = ModEntityTypes.COMBAT_KNIFE_SCAVENGER.get().create(level);
        if (scavenger == null) return;
        double angle = level.random.nextDouble() * Math.PI * 2D;
        double distance = 14D + level.random.nextInt(10);
        scavenger.moveTo(target.getX() + Math.cos(angle) * distance, target.getY(), target.getZ() + Math.sin(angle) * distance, level.random.nextFloat() * 360F, 0);
        if (elite) {
            scavenger.setCustomName(Component.literal("Whiteout Executioner").withStyle(ChatFormatting.DARK_RED));
            scavenger.setCustomNameVisible(true);
            scavenger.getAttribute(Attributes.MAX_HEALTH).setBaseValue(240D);
            scavenger.setHealth(240F);
            scavenger.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(15D);
        }
        level.addFreshEntity(scavenger);
    }

    public static WaveStatePacket packet(WinterFallWaveData data) {
        return new WaveStatePacket(data.wave(), data.active(), data.remainingTicks(), data.bossIncoming());
    }
}
