package com.santipdr.winterfall.network;

import com.santipdr.winterfall.client.ClientWinterFallState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record WaveStatePacket(int wave, boolean active, int remainingTicks, boolean bossIncoming) {
    public static void encode(WaveStatePacket packet, FriendlyByteBuf buffer) {
        buffer.writeVarInt(packet.wave);
        buffer.writeBoolean(packet.active);
        buffer.writeVarInt(packet.remainingTicks);
        buffer.writeBoolean(packet.bossIncoming);
    }

    public static WaveStatePacket decode(FriendlyByteBuf buffer) {
        return new WaveStatePacket(buffer.readVarInt(), buffer.readBoolean(), buffer.readVarInt(), buffer.readBoolean());
    }

    public static void handle(WaveStatePacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientWinterFallState.setWave(packet)));
        context.setPacketHandled(true);
    }
}
