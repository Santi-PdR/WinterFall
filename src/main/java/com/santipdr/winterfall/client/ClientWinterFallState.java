package com.santipdr.winterfall.client;

import com.santipdr.winterfall.network.PlayerStatePacket;
import com.santipdr.winterfall.network.WaveStatePacket;

public final class ClientWinterFallState {
    private static PlayerStatePacket player = new PlayerStatePacket(100, 100, 100, 100, 0, "VANGUARD");
    private static WaveStatePacket wave = new WaveStatePacket(0, false, 0, false);

    private ClientWinterFallState() {}

    public static void setPlayer(PlayerStatePacket state) { player = state; }
    public static void setWave(WaveStatePacket state) { wave = state; }
    public static PlayerStatePacket player() { return player; }
    public static WaveStatePacket wave() { return wave; }
}
