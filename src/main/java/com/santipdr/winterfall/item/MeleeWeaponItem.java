package com.santipdr.winterfall.item;

import com.santipdr.winterfall.common.WinterFallPlayerData;
import com.santipdr.winterfall.combat.CombatSystem;
import com.santipdr.winterfall.weapon.MeleeProfile;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MeleeWeaponItem extends Item {
    private final MeleeProfile profile;

    public MeleeWeaponItem(MeleeProfile profile, Properties properties) {
        super(properties);
        this.profile = profile;
    }

    public MeleeProfile profile() {
        return profile;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.winterfall.melee_damage", profile.lightDamage(), profile.heavyDamage()));
        tooltip.add(Component.translatable("tooltip.winterfall.melee_stamina", profile.staminaCost(), profile.heavyStaminaCost()));
        tooltip.add(Component.translatable("tooltip.winterfall.melee_reach", profile.reach()));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide) return InteractionResultHolder.success(stack);
        if (!WinterFallPlayerData.consumeStamina(player, profile.heavyStaminaCost())) {
            player.displayClientMessage(Component.translatable("message.winterfall.exhausted"), true);
            return InteractionResultHolder.fail(stack);
        }
        ServerPlayer serverPlayer = (ServerPlayer) player;
        CombatSystem.heavyAttack(serverPlayer, profile.reach(), profile.heavyDamage(), profile.knockback(), profile.bleedChance());
        player.getCooldowns().addCooldown(this, profile.heavyCooldownTicks());
        level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.PLAYERS, 0.9F, 0.82F);
        return InteractionResultHolder.consume(stack);
    }
}
