package com.santipdr.winterfall.util;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.santipdr.winterfall.common.WinterFallPlayerData;
import com.santipdr.winterfall.perk.Perk;
import com.santipdr.winterfall.registry.ModEffects;
import com.santipdr.winterfall.wave.WaveManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;

public final class WinterFallCommands {
    private WinterFallCommands() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("winterfall").requires(source -> source.hasPermission(2));
        root.then(Commands.literal("salvage")
                .then(Commands.literal("add").then(Commands.argument("target", EntityArgument.player()).then(Commands.argument("amount", IntegerArgumentType.integer())
                        .executes(context -> salvage(context.getSource(), EntityArgument.getPlayer(context, "target"), IntegerArgumentType.getInteger(context, "amount"), false)))))
                .then(Commands.literal("set").then(Commands.argument("target", EntityArgument.player()).then(Commands.argument("amount", IntegerArgumentType.integer(0))
                        .executes(context -> salvage(context.getSource(), EntityArgument.getPlayer(context, "target"), IntegerArgumentType.getInteger(context, "amount"), true))))));
        root.then(Commands.literal("wave")
                .then(Commands.literal("start").executes(context -> startWave(context.getSource(), 1))
                        .then(Commands.argument("number", IntegerArgumentType.integer(1, 10)).executes(context -> startWave(context.getSource(), IntegerArgumentType.getInteger(context, "number")))))
                .then(Commands.literal("set").then(Commands.argument("number", IntegerArgumentType.integer(1, 10))
                        .executes(context -> startWave(context.getSource(), IntegerArgumentType.getInteger(context, "number")))))
                .then(Commands.literal("stop").executes(context -> {
                    WaveManager.stop(context.getSource().getLevel());
                    context.getSource().sendSuccess(() -> Component.literal("WinterFall wave stopped."), true);
                    return 1;
                })));
        root.then(Commands.literal("spawn").executes(context -> {
            WaveManager.spawnScavenger(context.getSource().getLevel(), 1, false);
            context.getSource().sendSuccess(() -> Component.literal("WinterFall scavenger spawned."), true);
            return 1;
        }));
        root.then(Commands.literal("wound")
                .then(Commands.literal("apply").then(Commands.argument("target", EntityArgument.player()).executes(context -> wound(EntityArgument.getPlayer(context, "target"), true))))
                .then(Commands.literal("clear").then(Commands.argument("target", EntityArgument.player()).executes(context -> wound(EntityArgument.getPlayer(context, "target"), false)))));
        root.then(Commands.literal("injury")
                .then(Commands.literal("bleeding").then(Commands.argument("target", EntityArgument.player()).executes(context -> effect(EntityArgument.getPlayer(context, "target"), ModEffects.BLEEDING.get()))))
                .then(Commands.literal("wounded").then(Commands.argument("target", EntityArgument.player()).executes(context -> effect(EntityArgument.getPlayer(context, "target"), ModEffects.WOUNDED.get()))))
                .then(Commands.literal("clear").then(Commands.argument("target", EntityArgument.player()).executes(context -> wound(EntityArgument.getPlayer(context, "target"), false)))));
        root.then(Commands.literal("morale").then(Commands.literal("set").then(Commands.argument("target", EntityArgument.player()).then(Commands.argument("amount", IntegerArgumentType.integer(0, 100))
                .executes(context -> {
                    ServerPlayer player = EntityArgument.getPlayer(context, "target");
                    WinterFallPlayerData.setMorale(player, IntegerArgumentType.getInteger(context, "amount"));
                    WinterFallPlayerData.sync(player);
                    return 1;
                })))));
        root.then(Commands.literal("perk").then(Commands.argument("target", EntityArgument.player()).then(Commands.argument("id", StringArgumentType.word())
                .suggests((context, builder) -> net.minecraft.commands.SharedSuggestionProvider.suggest(java.util.Arrays.stream(Perk.values()).map(perk -> perk.name().toLowerCase()), builder))
                .executes(context -> setPerk(EntityArgument.getPlayer(context, "target"), StringArgumentType.getString(context, "id"))))));
        root.then(Commands.literal("debug").executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    context.getSource().sendSuccess(() -> Component.literal("stamina=%.1f thirst=%d morale=%d salvage=%d perk=%s".formatted(
                            WinterFallPlayerData.stamina(player), WinterFallPlayerData.thirst(player), WinterFallPlayerData.morale(player),
                            WinterFallPlayerData.salvage(player), WinterFallPlayerData.perk(player))), false);
                    return 1;
                }));
        dispatcher.register(root);
    }

    private static int salvage(CommandSourceStack source, ServerPlayer player, int amount, boolean set) {
        if (set) WinterFallPlayerData.addSalvage(player, -WinterFallPlayerData.salvage(player));
        WinterFallPlayerData.addSalvage(player, amount);
        WinterFallPlayerData.sync(player);
        source.sendSuccess(() -> Component.literal("Salvage: " + WinterFallPlayerData.salvage(player)), true);
        return 1;
    }

    private static int startWave(CommandSourceStack source, int wave) {
        WaveManager.start(source.getLevel(), wave);
        source.sendSuccess(() -> Component.literal("WinterFall wave " + wave + " started."), true);
        return 1;
    }

    private static int wound(ServerPlayer player, boolean apply) {
        if (apply) {
            player.addEffect(new MobEffectInstance(ModEffects.BLEEDING.get(), 240, 0));
            player.addEffect(new MobEffectInstance(ModEffects.WOUNDED.get(), 300, 0));
        } else {
            player.removeEffect(ModEffects.BLEEDING.get());
            player.removeEffect(ModEffects.WOUNDED.get());
            player.removeEffect(ModEffects.STUNNED.get());
        }
        return 1;
    }

    private static int effect(ServerPlayer player, net.minecraft.world.effect.MobEffect effect) {
        player.addEffect(new MobEffectInstance(effect, 300, 0));
        return 1;
    }

    private static int setPerk(ServerPlayer player, String id) {
        WinterFallPlayerData.setPerk(player, Perk.byId(id));
        WinterFallPlayerData.sync(player);
        player.sendSystemMessage(Component.literal("WinterFall perk set to " + WinterFallPlayerData.perk(player).displayName()));
        return 1;
    }
}
