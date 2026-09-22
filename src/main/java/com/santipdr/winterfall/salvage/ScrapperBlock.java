package com.santipdr.winterfall.salvage;

import com.santipdr.winterfall.common.WinterFallPlayerData;
import com.santipdr.winterfall.item.FirearmItem;
import com.santipdr.winterfall.item.MeleeWeaponItem;
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
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public final class ScrapperBlock extends Block {
    public ScrapperBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        var held = player.getItemInHand(hand);
        if (held.isEmpty()) {
            NetworkHooks.openScreen((ServerPlayer) player, menu(), pos);
            return InteractionResult.CONSUME;
        }
        if (held.is(ModItems.BILHOOK_BLUEPRINT.get())) {
            player.displayClientMessage(Component.translatable("message.winterfall.scrapper_blueprint"), true);
            return InteractionResult.FAIL;
        }
        int gain = held.getItem() instanceof FirearmItem ? 18 : held.getItem() instanceof MeleeWeaponItem ? 12 : Math.max(1, held.getRarity().ordinal() + 1);
        held.shrink(1);
        WinterFallPlayerData.addSalvage(player, gain);
        player.displayClientMessage(Component.translatable("message.winterfall.salvage_gained", gain), true);
        level.playSound(null, pos, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, .55F, 1.45F);
        return InteractionResult.CONSUME;
    }

    private MenuProvider menu() {
        return new SimpleMenuProvider((id, inventory, ignored) -> new SalvageStationMenu(id, inventory),
                Component.translatable("container.winterfall.scrapper"));
    }
}
