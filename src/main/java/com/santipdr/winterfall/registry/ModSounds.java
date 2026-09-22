package com.santipdr.winterfall.registry;

import com.santipdr.winterfall.WinterFall;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, WinterFall.MOD_ID);
    public static final RegistryObject<SoundEvent> MELEE_LIGHT = sound("weapon.melee_light");
    public static final RegistryObject<SoundEvent> MELEE_HEAVY = sound("weapon.melee_heavy");
    public static final RegistryObject<SoundEvent> FIRE = sound("weapon.fire");
    public static final RegistryObject<SoundEvent> RELOAD = sound("weapon.reload");
    public static final RegistryObject<SoundEvent> IMPACT_FLESH = sound("impact.flesh");
    public static final RegistryObject<SoundEvent> HEADSHOT = sound("impact.headshot");
    public static final RegistryObject<SoundEvent> SCAVENGER_ALERT = sound("scavenger.alert");
    public static final RegistryObject<SoundEvent> STORM = sound("ambient.storm");

    private ModSounds() {}

    private static RegistryObject<SoundEvent> sound(String id) {
        return SOUNDS.register(id, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WinterFall.MOD_ID, id)));
    }
}
