package com.santipdr.winterfall.hud;

import com.santipdr.winterfall.WinterFall;
import com.santipdr.winterfall.client.ClientWinterFallState;
import com.santipdr.winterfall.item.FirearmItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WinterFall.MOD_ID, value = Dist.CLIENT)
public final class WinterFallHud {
    private static final int PANEL = 0xB4141A20;
    private static final int ACCENT = 0xFF91C6D7;
    private static final int WARNING = 0xFFE06C75;

    private WinterFallHud() {}

    @SubscribeEvent
    public static void render(RenderGuiOverlayEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.options.hideGui) return;
        GuiGraphics graphics = event.getGuiGraphics();
        var state = ClientWinterFallState.player();
        var wave = ClientWinterFallState.wave();
        int width = event.getWindow().getGuiScaledWidth();
        int height = event.getWindow().getGuiScaledHeight();
        int x = 10;
        int y = height - 63;
        graphics.fill(x - 3, y - 3, x + 137, y + 40, PANEL);
        int staminaWidth = Math.round(132 * (state.stamina() / Math.max(1.0F, state.maxStamina())));
        graphics.fill(x, y, x + 132, y + 6, 0xFF29343C);
        graphics.fill(x, y, x + staminaWidth, y + 6, ACCENT);
        graphics.drawString(minecraft.font, Component.translatable("hud.winterfall.stamina", Math.round(state.stamina()), Math.round(state.maxStamina())), x, y + 10, 0xFFE7EEF2, false);
        graphics.drawString(minecraft.font, Component.translatable("hud.winterfall.survival", state.thirst(), state.morale()), x, y + 21,
                state.thirst() < 25 || state.morale() < 25 ? WARNING : 0xFFBFCAD0, false);
        graphics.drawString(minecraft.font, Component.translatable("hud.winterfall.salvage", state.salvage()), x, y + 32, 0xFFE4BF69, false);
        if (wave.active() || wave.wave() > 0) {
            String suffix = wave.bossIncoming() ? "  BOSS" : "";
            graphics.drawString(minecraft.font, "WAVE " + wave.wave() + suffix + "  " + (wave.remainingTicks() / 20) + "s", width - 124, 12, WARNING, true);
        }
        ItemStack held = minecraft.player.getMainHandItem();
        if (held.getItem() instanceof FirearmItem firearm) {
            graphics.drawString(minecraft.font, firearm.rounds(held) + " / " + firearm.profile().magazineSize(), width - 74, height - 28, 0xFFF0F5F7, true);
        }
    }
}
