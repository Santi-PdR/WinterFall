package com.santipdr.winterfall.item;

import com.santipdr.winterfall.common.WinterFallPlayerData;
import com.santipdr.winterfall.combat.CombatSystem;
import com.santipdr.winterfall.config.WinterFallConfig;
import com.santipdr.winterfall.perk.Perk;
import com.santipdr.winterfall.registry.ModEffects;
import com.santipdr.winterfall.weapon.FirearmProfile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FirearmItem extends Item {
    private static final String ROUNDS = "winterfall_rounds";
    private final FirearmProfile profile;

    public FirearmItem(FirearmProfile profile, Properties properties) {
        super(properties);
        this.profile = profile;
    }

    public FirearmProfile profile() {
        return profile;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.winterfall.firearm_damage", profile.bodyDamage(), profile.headshotDamage()));
        tooltip.add(Component.translatable("tooltip.winterfall.firearm_magazine", profile.magazineSize(), profile.rpm()));
        tooltip.add(Component.translatable("tooltip.winterfall.firearm_ammo", profile.ammoItem().getDescription()));
    }

    public int rounds(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(ROUNDS)) tag.putInt(ROUNDS, profile.magazineSize());
        return tag.getInt(ROUNDS);
    }

    private void setRounds(ItemStack stack, int rounds) {
        stack.getOrCreateTag().putInt(ROUNDS, Math.max(0, Math.min(profile.magazineSize(), rounds)));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide) return InteractionResultHolder.success(stack);
        if (player.isShiftKeyDown()) {
            reload(player, stack);
            return InteractionResultHolder.consume(stack);
        }
        if (rounds(stack) <= 0) {
            player.displayClientMessage(Component.translatable("message.winterfall.empty"), true);
            level.playSound(null, player.blockPosition(), SoundEvents.DISPENSER_FAIL, SoundSource.PLAYERS, .65F, .8F);
            return InteractionResultHolder.fail(stack);
        }
        if (player.getCooldowns().isOnCooldown(this)) return InteractionResultHolder.fail(stack);
        fire((ServerPlayer) player, stack);
        return InteractionResultHolder.consume(stack);
    }

    private void fire(ServerPlayer player, ItemStack stack) {
        setRounds(stack, rounds(stack) - 1);
        float spread = profile.spreadDegrees() * (WinterFallPlayerData.perk(player) == Perk.MARKSMAN ? 0.60F : 1.0F);
        CombatSystem.AimedHit hit = CombatSystem.aimedHit(player, profile.range(), spread);
        if (hit != null) {
            LivingEntity target = hit.target();
            boolean headshot = CombatSystem.isHeadshot(target, hit.location());
            float damage = (headshot ? profile.headshotDamage() : profile.bodyDamage()) * WinterFallConfig.GLOBAL_DAMAGE_MULTIPLIER.get().floatValue();
            target.hurt(player.damageSources().playerAttack(player), damage);
            if (headshot) target.addEffect(new MobEffectInstance(ModEffects.STUNNED.get(), 18, 0));
            player.displayClientMessage(Component.translatable(headshot ? "message.winterfall.headshot" : "message.winterfall.hit"), true);
        }
        player.level().playSound(null, player.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, .38F, 1.5F);
        player.getCooldowns().addCooldown(this, profile.fireCooldownTicks());
    }

    private void reload(Player player, ItemStack stack) {
        int missing = profile.magazineSize() - rounds(stack);
        if (missing <= 0) return;
        int loaded = 0;
        for (ItemStack inventoryStack : player.getInventory().items) {
            while (loaded < missing && inventoryStack.is(profile.ammoItem())) {
                inventoryStack.shrink(1);
                loaded++;
            }
            if (loaded == missing) break;
        }
        if (loaded == 0) {
            player.displayClientMessage(Component.translatable("message.winterfall.no_ammo"), true);
            return;
        }
        setRounds(stack, rounds(stack) + loaded);
        player.getCooldowns().addCooldown(this, profile.reloadTicks());
        player.level().playSound(null, player.blockPosition(), SoundEvents.CROSSBOW_LOADING_START, SoundSource.PLAYERS, .75F, .9F);
    }
}
