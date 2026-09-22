package com.santipdr.winterfall.common;

import com.santipdr.winterfall.config.WinterFallConfig;
import com.santipdr.winterfall.network.ModNetworking;
import com.santipdr.winterfall.network.PlayerStatePacket;
import com.santipdr.winterfall.perk.Perk;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public final class WinterFallPlayerData {
    private static final String ROOT = "winterfall_state";
    private static final String STAMINA = "stamina";
    private static final String THIRST = "thirst";
    private static final String MORALE = "morale";
    private static final String SALVAGE = "salvage";
    private static final String PERK = "perk";

    private WinterFallPlayerData() {}

    private static CompoundTag tag(Player player) {
        CompoundTag persistent = player.getPersistentData();
        if (!persistent.contains(ROOT, Tag.TAG_COMPOUND)) {
            CompoundTag state = new CompoundTag();
            state.putDouble(STAMINA, WinterFallConfig.STAMINA_MAX.get());
            state.putInt(THIRST, 100);
            state.putInt(MORALE, 100);
            state.putInt(SALVAGE, 0);
            state.putString(PERK, Perk.VANGUARD.name());
            persistent.put(ROOT, state);
        }
        return persistent.getCompound(ROOT);
    }

    public static double maxStamina(Player player) {
        return WinterFallConfig.STAMINA_MAX.get() * (perk(player) == Perk.VANGUARD ? 1.20D : 1.0D);
    }

    public static double stamina(Player player) { return tag(player).getDouble(STAMINA); }

    public static void setStamina(Player player, double value) {
        tag(player).putDouble(STAMINA, Math.max(0.0D, Math.min(maxStamina(player), value)));
    }

    public static boolean consumeStamina(Player player, double amount) {
        if (stamina(player) < amount) return false;
        setStamina(player, stamina(player) - amount);
        return true;
    }

    public static int thirst(Player player) { return tag(player).getInt(THIRST); }
    public static void setThirst(Player player, int value) { tag(player).putInt(THIRST, Math.max(0, Math.min(100, value))); }
    public static int morale(Player player) { return tag(player).getInt(MORALE); }
    public static void setMorale(Player player, int value) { tag(player).putInt(MORALE, Math.max(0, Math.min(100, value))); }
    public static int salvage(Player player) { return tag(player).getInt(SALVAGE); }

    public static void addSalvage(Player player, int value) {
        int bonus = perk(player) == Perk.SCAVENGER && value > 0 ? Math.round(value * 1.25F) : value;
        tag(player).putInt(SALVAGE, Math.max(0, salvage(player) + bonus));
    }

    public static boolean spendSalvage(Player player, int value) {
        if (salvage(player) < value) return false;
        tag(player).putInt(SALVAGE, salvage(player) - value);
        return true;
    }

    public static Perk perk(Player player) { return Perk.byId(tag(player).getString(PERK)); }

    public static void setPerk(Player player, Perk perk) {
        tag(player).putString(PERK, perk.name());
        setStamina(player, stamina(player));
    }

    public static void copy(Player from, Player to) { to.getPersistentData().put(ROOT, tag(from).copy()); }

    public static void sync(ServerPlayer player) {
        ModNetworking.sendToPlayer(player, new PlayerStatePacket((float) stamina(player), (float) maxStamina(player),
                thirst(player), morale(player), salvage(player), perk(player).name()));
    }
}
