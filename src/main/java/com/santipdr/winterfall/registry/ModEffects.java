package com.santipdr.winterfall.registry;

import com.santipdr.winterfall.WinterFall;
import com.santipdr.winterfall.survival.BleedingEffect;
import com.santipdr.winterfall.survival.StunEffect;
import com.santipdr.winterfall.survival.WoundedEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, WinterFall.MOD_ID);
    public static final RegistryObject<MobEffect> BLEEDING = EFFECTS.register("bleeding", BleedingEffect::new);
    public static final RegistryObject<MobEffect> WOUNDED = EFFECTS.register("wounded", WoundedEffect::new);
    public static final RegistryObject<MobEffect> STUNNED = EFFECTS.register("stunned", StunEffect::new);

    private ModEffects() {}
}
