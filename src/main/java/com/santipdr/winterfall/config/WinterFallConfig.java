package com.santipdr.winterfall.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public final class WinterFallConfig {
    public static final ForgeConfigSpec SERVER;
    public static final ForgeConfigSpec.DoubleValue STAMINA_MAX;
    public static final ForgeConfigSpec.DoubleValue STAMINA_REGEN_PER_TICK;
    public static final ForgeConfigSpec.DoubleValue SPRINT_STAMINA_COST;
    public static final ForgeConfigSpec.IntValue BLEED_DAMAGE_INTERVAL;
    public static final ForgeConfigSpec.IntValue WAVE_DURATION_SECONDS;
    public static final ForgeConfigSpec.IntValue SAFE_ZONE_RADIUS;
    public static final ForgeConfigSpec.IntValue WAVE_BASE_SCAVENGERS;
    public static final ForgeConfigSpec.DoubleValue GLOBAL_DAMAGE_MULTIPLIER;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("combat");
        STAMINA_MAX = builder.comment("Base maximum stamina. Perks can adjust this value.")
                .defineInRange("staminaMax", 100.0D, 20.0D, 500.0D);
        STAMINA_REGEN_PER_TICK = builder.comment("Stamina restored each tick outside strenuous actions.")
                .defineInRange("staminaRegenPerTick", 0.32D, 0.01D, 5.0D);
        SPRINT_STAMINA_COST = builder.comment("Stamina consumed per tick while sprinting.")
                .defineInRange("sprintStaminaCost", 0.18D, 0.0D, 5.0D);
        BLEED_DAMAGE_INTERVAL = builder.comment("Ticks between bleeding damage pulses.")
                .defineInRange("bleedDamageInterval", 40, 10, 1200);
        GLOBAL_DAMAGE_MULTIPLIER = builder.comment("Multiplies WinterFall weapon damage.")
                .defineInRange("globalDamageMultiplier", 1.0D, 0.1D, 10.0D);
        builder.pop();
        builder.push("waves");
        WAVE_DURATION_SECONDS = builder.comment("Time limit of an active storm wave.")
                .defineInRange("waveDurationSeconds", 180, 30, 3600);
        SAFE_ZONE_RADIUS = builder.comment("Radius around world spawn protected from storm exposure.")
                .defineInRange("safeZoneRadius", 24, 4, 256);
        WAVE_BASE_SCAVENGERS = builder.comment("Scavengers spawned before wave scaling.")
                .defineInRange("waveBaseScavengers", 2, 1, 32);
        builder.pop();
        SERVER = builder.build();
    }

    private WinterFallConfig() {}

    public static void register(ModLoadingContext context) {
        context.registerConfig(ModConfig.Type.SERVER, SERVER);
    }
}
