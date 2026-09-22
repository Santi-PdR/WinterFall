package com.santipdr.winterfall.registry;

import com.santipdr.winterfall.WinterFall;
import com.santipdr.winterfall.entity.CombatKnifeScavenger;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, WinterFall.MOD_ID);
    public static final RegistryObject<EntityType<CombatKnifeScavenger>> COMBAT_KNIFE_SCAVENGER = ENTITY_TYPES.register("combat_knife_scavenger",
            () -> EntityType.Builder.of(CombatKnifeScavenger::new, MobCategory.MONSTER).sized(.65F, 1.95F).clientTrackingRange(10).build("combat_knife_scavenger"));

    private ModEntityTypes() {}
}
