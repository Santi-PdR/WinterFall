package com.santipdr.winterfall.network;

import com.santipdr.winterfall.client.ClientWinterFallState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record PlayerStatePacket(float stamina, float maxStamina, int thirst, int morale, int salvage, String perk) {
    public static void encode(PlayerStatePacket packet, FriendlyByteBuf buffer) {
        buffer.writeFloat(packet.stamina);
        buffer.writeFloat(packet.maxStamina);
        buffer.writeVarInt(packet.thirst);
        buffer.writeVarInt(packet.morale);
        buffer.writeVarInt(packet.salvage);
        buffer.writeUtf(packet.perk);
    }

    public static PlayerStatePacket decode(FriendlyByteBuf buffer) {
        return new PlayerStatePacket(buffer.readFloat(), buffer.readFloat(), buffer.readVarInt(), buffer.readVarInt(), buffer.readVarInt(), buffer.readUtf(32));
    }

    public static void handle(PlayerStatePacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientWinterFallState.setPlayer(packet)));
        context.setPacketHandled(true);
    }
}
