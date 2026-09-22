package com.santipdr.winterfall.salvage;

import com.santipdr.winterfall.registry.ModMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public final class SalvageStationMenu extends AbstractContainerMenu {
    public SalvageStationMenu(int containerId, Inventory inventory) {
        super(ModMenus.SALVAGE_STATION.get(), containerId);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
