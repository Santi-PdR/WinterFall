package com.santipdr.winterfall.common;

import com.santipdr.winterfall.WinterFall;
import com.santipdr.winterfall.entity.CombatKnifeScavenger;
import com.santipdr.winterfall.registry.ModEntityTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WinterFall.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModLifecycleEvents {
    private ModLifecycleEvents() {}

    @SubscribeEvent
    public static void attributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.COMBAT_KNIFE_SCAVENGER.get(), CombatKnifeScavenger.createAttributes().build());
    }
}
