package com.santipdr.winterfall.salvage;

import com.santipdr.winterfall.common.WinterFallPlayerData;
import com.santipdr.winterfall.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public final class WorkbenchBlock extends Block {
    public static final int BILLHOOK_COST = 100;

    public WorkbenchBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        var held = player.getItemInHand(hand);
        if (held.is(ModItems.BILHOOK_BLUEPRINT.get())) {
            if (!WinterFallPlayerData.spendSalvage(player, BILLHOOK_COST)) {
                player.displayClientMessage(Component.translatable("message.winterfall.not_enough_salvage", BILLHOOK_COST), true);
                return InteractionResult.FAIL;
            }
            held.shrink(1);
            player.addItem(ModItems.BILLHOOK.get().getDefaultInstance());
            level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, .85F, .9F);
            player.displayClientMessage(Component.translatable("message.winterfall.billhook_crafted"), true);
            return InteractionResult.CONSUME;
        }
        NetworkHooks.openScreen((ServerPlayer) player,
                new SimpleMenuProvider((id, inventory, ignored) -> new SalvageStationMenu(id, inventory),
                        Component.translatable("container.winterfall.workbench")), pos);
        return InteractionResult.CONSUME;
    }
}
