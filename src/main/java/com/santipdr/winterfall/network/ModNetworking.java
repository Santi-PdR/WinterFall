package com.santipdr.winterfall.network;

import com.santipdr.winterfall.WinterFall;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public final class ModNetworking {
    private static final String PROTOCOL = "1";
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(WinterFall.MOD_ID, "main"), () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);
    private static int id;

    private ModNetworking() {}

    public static void register() {
        CHANNEL.registerMessage(id++, PlayerStatePacket.class, PlayerStatePacket::encode, PlayerStatePacket::decode, PlayerStatePacket::handle);
        CHANNEL.registerMessage(id++, WaveStatePacket.class, WaveStatePacket::encode, WaveStatePacket::decode, WaveStatePacket::handle);
    }

    public static void sendToPlayer(ServerPlayer player, Object message) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
