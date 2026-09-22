package com.santipdr.winterfall.registry;

import com.santipdr.winterfall.WinterFall;
import com.santipdr.winterfall.salvage.SalvageStationMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, WinterFall.MOD_ID);
    public static final RegistryObject<MenuType<SalvageStationMenu>> SALVAGE_STATION = MENUS.register("salvage_station",
            () -> IForgeMenuType.create((id, inventory, buffer) -> new SalvageStationMenu(id, inventory)));

    private ModMenus() {}
}
