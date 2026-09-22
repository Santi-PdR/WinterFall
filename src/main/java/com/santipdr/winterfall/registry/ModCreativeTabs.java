package com.santipdr.winterfall.registry;

import com.santipdr.winterfall.WinterFall;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/** One discoverable, ordered entry point for every player-facing WinterFall item. */
public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WinterFall.MOD_ID);

    public static final RegistryObject<CreativeModeTab> WINTERFALL = TABS.register("winterfall", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.winterfall"))
            .icon(() -> new ItemStack(ModItems.BILLHOOK.get()))
            .displayItems((parameters, output) -> {
                // Survival / resources
                output.accept(ModItems.FIELD_RATION.get());
                output.accept(ModItems.CANTEEN.get());
                output.accept(ModItems.SHORT_AMMO.get());
                output.accept(ModItems.LONG_AMMO.get());
                output.accept(ModItems.SHOTGUN_SHELL.get());
                // Melee
                output.accept(ModItems.COMBAT_KNIFE.get());
                output.accept(ModItems.BILLHOOK.get());
                output.accept(ModItems.PIPE_WRENCH.get());
                output.accept(ModItems.CLEAVER.get());
                output.accept(ModItems.MACHETE.get());
                output.accept(ModItems.HATCHET.get());
                output.accept(ModItems.SLEDGEHAMMER.get());
                output.accept(ModItems.CROWBAR.get());
                output.accept(ModItems.SPEAR.get());
                output.accept(ModItems.RIOT_BATON.get());
                output.accept(ModItems.TRENCH_SHOVEL.get());
                // Firearms
                output.accept(ModItems.BROOMHANDLE_22.get());
                output.accept(ModItems.MARK_VII.get());
                output.accept(ModItems.SERVICE_RIFLE.get());
                output.accept(ModItems.PUMP_SHOTGUN.get());
                output.accept(ModItems.IMPROVISED_SMG.get());
                output.accept(ModItems.HUNTING_RIFLE.get());
                output.accept(ModItems.FLARE_GUN.get());
                // Blueprints / stations / development access
                output.accept(ModItems.BILHOOK_BLUEPRINT.get());
                output.accept(ModItems.SCRAPPER.get());
                output.accept(ModItems.WORKBENCH.get());
                output.accept(ModItems.COMBAT_KNIFE_SCAVENGER_SPAWN_EGG.get());
            })
            .build());

    private ModCreativeTabs() {}
}
