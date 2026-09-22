package com.santipdr.winterfall.weapon;

import net.minecraft.world.item.Item;
import java.util.function.Supplier;

public record FirearmProfile(Supplier<Item> ammo, int magazineSize, float bodyDamage, float headshotDamage, int rpm,
                             float range, float spreadDegrees, int reloadTicks) {
    public Item ammoItem() {
        return ammo.get();
    }

    public int fireCooldownTicks() {
        return Math.max(1, Math.round(1200.0F / rpm));
    }
}
