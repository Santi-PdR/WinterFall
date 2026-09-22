package com.santipdr.winterfall.client.render;

import com.santipdr.winterfall.WinterFall;
import com.santipdr.winterfall.entity.CombatKnifeScavenger;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Client-only renderer. Its registration prevents EntityRenderDispatcher from receiving a null renderer
 * when a Combat Knife Scavenger is tracked by a client.
 */
public final class CombatKnifeScavengerRenderer extends HumanoidMobRenderer<CombatKnifeScavenger, HumanoidModel<CombatKnifeScavenger>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(WinterFall.MOD_ID, "textures/entity/combat_knife_scavenger.png");

    public CombatKnifeScavengerRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.ZOMBIE)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(CombatKnifeScavenger scavenger) {
        return TEXTURE;
    }
}
