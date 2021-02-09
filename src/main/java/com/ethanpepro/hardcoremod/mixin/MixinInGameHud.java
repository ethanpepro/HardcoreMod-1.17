package com.ethanpepro.hardcoremod.mixin;

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

@Mixin(InGameHud.class)
public abstract class MixinInGameHud extends DrawableHelper {
    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    private int scaledWidth;

    @Shadow
    private int scaledHeight;

    @Shadow
    public abstract PlayerEntity getCameraPlayer();

    private static final Identifier MOD_GUI_ICONS_TEXTURE = new Identifier("hardcoremod", "textures/gui/icons.png");

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;F)V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F"), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderPumpkinOverlay()V")))
    public void render(MatrixStack matrices, float tickDelta, CallbackInfo info) {
        // TODO: Temperature effect overlay rendering (which was the point of *this* slice, move rendering elsewhere, like hotbar)
        // TODO: Multiple slices for proper layering and ordering
        // TODO: Finalize tick bounce based on temperature extremity
        // TODO: Smooth (proper) color gradients (3 color transition) ("blue" at FREEZING, "white" at TEMPERATE, "red" at BURNING)
        // TODO: Flashing for temperature level change
        // TODO: At threshold (temperature effect added) change texture to signify extremity
        // TODO: Abstract out to a class and finalize (orb rendering was never meant to be here)
        PlayerEntity playerEntity = this.getCameraPlayer();

        if (playerEntity != null && !this.client.options.hudHidden && this.client.interactionManager != null && this.client.interactionManager.hasStatusBars()) {
            int x = this.scaledWidth / 2 - 8;
            // TODO: Shift tooltip height of held items!
            int offset = (playerEntity.experienceLevel > 0) ? 54 : 48;
            int y = this.scaledHeight - offset;

            this.client.getTextureManager().bindTexture(MOD_GUI_ICONS_TEXTURE);

            this.drawTexture(matrices, x, y, 0, 0, 16, 16);
        }
    }
}
