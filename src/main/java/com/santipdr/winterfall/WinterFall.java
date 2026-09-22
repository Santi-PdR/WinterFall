package com.santipdr.winterfall;

import com.mojang.logging.LogUtils;
import com.santipdr.winterfall.config.WinterFallConfig;
import com.santipdr.winterfall.network.ModNetworking;
import com.santipdr.winterfall.registry.ModBlocks;
import com.santipdr.winterfall.registry.ModCreativeTabs;
import com.santipdr.winterfall.registry.ModEffects;
import com.santipdr.winterfall.registry.ModEntityTypes;
import com.santipdr.winterfall.registry.ModItems;
import com.santipdr.winterfall.registry.ModMenus;
import com.santipdr.winterfall.registry.ModSounds;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(WinterFall.MOD_ID)
public final class WinterFall {
    public static final String MOD_ID = "winterfall";
    public static final Logger LOGGER = LogUtils.getLogger();

    public WinterFall() {
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(modBus);
        ModCreativeTabs.TABS.register(modBus);
        ModBlocks.BLOCKS.register(modBus);
        ModEffects.EFFECTS.register(modBus);
        ModEntityTypes.ENTITY_TYPES.register(modBus);
        ModSounds.SOUNDS.register(modBus);
        ModMenus.MENUS.register(modBus);
        WinterFallConfig.register(ModLoadingContext.get());
        ModNetworking.register();
    }
}
