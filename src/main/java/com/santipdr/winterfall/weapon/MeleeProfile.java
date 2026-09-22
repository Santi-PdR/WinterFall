package com.santipdr.winterfall.weapon;

public record MeleeProfile(float lightDamage, float heavyDamage, float reach, float staminaCost, float heavyStaminaCost,
                           float bleedChance, float knockback, int heavyCooldownTicks) {}
