package com.santipdr.winterfall.client;

import com.santipdr.winterfall.WinterFall;
import com.santipdr.winterfall.client.render.CombatKnifeScavengerRenderer;
import com.santipdr.winterfall.registry.ModEntityTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WinterFall.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {
    private ClientModEvents() {}

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.COMBAT_KNIFE_SCAVENGER.get(), CombatKnifeScavengerRenderer::new);
    }
}
