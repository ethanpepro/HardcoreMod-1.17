package com.ethanpepro.hardcoremod.mixin;

import com.ethanpepro.hardcoremod.HardcoreMod;
import com.ethanpepro.hardcoremod.api.temperature.TemperatureHelper;
import com.ethanpepro.hardcoremod.api.temperature.TemperatureRange;
import com.ethanpepro.hardcoremod.component.HardcoreModComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {
    @Shadow
    @Final
    private Random random;

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    private int ticks;

    @Shadow
    private int scaledWidth;

    @Shadow
    private int scaledHeight;

    @Shadow
    public abstract PlayerEntity getCameraPlayer();

    private static final Identifier MOD_GUI_ICONS_TEXTURE = new Identifier("hardcoremod", "textures/gui/icons.png");

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;F)V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F"), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderPumpkinOverlay()V")))
    private void render(MatrixStack matrices, float tickDelta, CallbackInfo info) {
        // TODO: Temperature effect overlay rendering (which was the point of *this* slice)
        // TODO: Multiple slices for proper layering
        // TODO: Finalize tick bounce based on extremity
        // TODO: Smooth color gradients (3 color transition) ("blue" at FREEZING, "white" at TEMPERATE, "red" at BURNING)
        // TODO: Flashing for temperature level change
        // TODO: Abstract out to a class and finalize (orb rendering was never meant to be here)
        PlayerEntity playerEntity = this.getCameraPlayer();

        if (playerEntity != null) {
            int x = this.scaledWidth / 2 - 8;
            int offset = (playerEntity.experienceLevel > 0) ? 52 : 48;
            int y = this.scaledHeight - offset;

            this.client.getTextureManager().bindTexture(MOD_GUI_ICONS_TEXTURE);

            int temperature = HardcoreModComponents.TEMPERATURE.get(playerEntity).getTemperatureLevel();

            // TODO: This can be modeled with a linear equation, too much screwing with Desmos
            float modifier = -(20.0f / 7.0f);

            int k = Math.round(modifier * Math.abs(temperature) + 20.0f);

            if (this.ticks % (k * 3 + 1) == 0) {
                y += (this.random.nextInt(3) - 1);
            }

            this.drawTexture(matrices, x, y, 0, 0, 16, 16);
        }
    }
}
