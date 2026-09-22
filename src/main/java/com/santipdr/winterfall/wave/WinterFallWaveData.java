package com.santipdr.winterfall.wave;

import com.santipdr.winterfall.WinterFall;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public final class WinterFallWaveData extends SavedData {
    private static final String NAME = WinterFall.MOD_ID + "_waves";
    private int wave;
    private int remainingTicks;
    private int spawnTimer;
    private boolean active;
    private boolean bossIncoming;

    public static WinterFallWaveData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(WinterFallWaveData::load, WinterFallWaveData::new, NAME);
    }

    public static WinterFallWaveData load(CompoundTag tag) {
        WinterFallWaveData data = new WinterFallWaveData();
        data.wave = tag.getInt("wave");
        data.remainingTicks = tag.getInt("remainingTicks");
        data.spawnTimer = tag.getInt("spawnTimer");
        data.active = tag.getBoolean("active");
        data.bossIncoming = tag.getBoolean("bossIncoming");
        return data;
    }

    public void start(int desiredWave, int durationTicks) {
        wave = Math.max(1, Math.min(10, desiredWave));
        remainingTicks = durationTicks;
        spawnTimer = 1;
        active = true;
        bossIncoming = wave == 10;
        setDirty();
    }

    public void stop() {
        active = false;
        remainingTicks = 0;
        setDirty();
    }

    public void tick() {
        if (!active) return;
        remainingTicks--;
        spawnTimer--;
        if (remainingTicks <= 0) {
            active = false;
            remainingTicks = 0;
        }
        setDirty();
    }

    public int wave() { return wave; }
    public int remainingTicks() { return remainingTicks; }
    public int spawnTimer() { return spawnTimer; }
    public boolean active() { return active; }
    public boolean bossIncoming() { return bossIncoming; }
    public void resetSpawnTimer(int ticks) { spawnTimer = ticks; setDirty(); }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putInt("wave", wave);
        tag.putInt("remainingTicks", remainingTicks);
        tag.putInt("spawnTimer", spawnTimer);
        tag.putBoolean("active", active);
        tag.putBoolean("bossIncoming", bossIncoming);
        return tag;
    }
}
