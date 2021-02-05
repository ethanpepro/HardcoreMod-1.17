package com.ethanpepro.hardcoremod.mixin;

import com.ethanpepro.hardcoremod.api.temperature.TemperatureRange;
import com.ethanpepro.hardcoremod.api.temperature.TemperatureRegistry;
import com.ethanpepro.hardcoremod.api.temperature.modifier.ITemperatureDynamicModifier;
import com.ethanpepro.hardcoremod.api.temperature.modifier.ITemperatureModifier;
import com.ethanpepro.hardcoremod.component.HardcoreModComponents;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
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

import java.util.List;
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

    private int getColorForTemperature(int temperature) {
        // TODO: Keep percentage thing but redo algorithm
        float percentage = (1.0f / (TemperatureRange.BURNING.getUpperBound() - TemperatureRange.FREEZING.getLowerBound())) * temperature + 0.5f;

        int colorFreezing = 0x0551be;
        int colorTemperate = 0xffffff;
        int colorBurning = 0xb61e23;
        int color = 0x0;

        if (percentage < 0.5) {
            color = (int)((colorTemperate * percentage * 2.0f) +  colorFreezing * (0.5f - percentage) * 2.0f);
        } else {
            color = (int)(colorBurning * (percentage - 0.5f) * 2.0f + colorTemperate * (1.0f - percentage) * 2.0f);
        }

        return color;
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;F)V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F"), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderPumpkinOverlay()V")))
    public void render(MatrixStack matrices, float tickDelta, CallbackInfo info) {
        // TODO: Temperature effect overlay rendering (which was the point of *this* slice)
        // TODO: Multiple slices for proper layering
        // TODO: Finalize tick bounce based on extremity
        // TODO: Smooth color gradients (3 color transition) ("blue" at FREEZING, "white" at TEMPERATE, "red" at BURNING)
        // TODO: Flashing for temperature level change
        // TODO: Abstract out to a class and finalize (orb rendering was never meant to be here)
        PlayerEntity playerEntity = this.getCameraPlayer();

        if (playerEntity != null) {
            List<String> list = Lists.newArrayList();

            list.add("Registered temperature modifiers:");

            for (ITemperatureModifier modifier : TemperatureRegistry.getTemperatureModifiers().values()) {
                list.add("");
                list.add(String.format("- %s:%s", modifier.getIdentifier().getNamespace(), modifier.getIdentifier().getPath()));
            }

            list.add("");

            for (ITemperatureDynamicModifier dynamicModifier : TemperatureRegistry.getTemperatureDynamicModifiers().values()) {
                list.add("");
                list.add(String.format("- %s:%s", dynamicModifier.getIdentifier().getNamespace(), dynamicModifier.getIdentifier().getPath()));
            }

            for (int i = 0; i < list.size(); ++i) {
                String string = list.get(i);

                if (!Strings.isNullOrEmpty(string)) {
                    int j = 9;
                    int y = 2 + j * i;

                    drawStringWithShadow(matrices, this.client.textRenderer, string, 2, y, 0xffffff);
                }
            }
        }

        if (playerEntity != null) {
            int x = this.scaledWidth / 2 - 8;
            int offset = (playerEntity.experienceLevel > 0) ? 54 : 48;
            int y = this.scaledHeight - offset;

            this.client.getTextureManager().bindTexture(MOD_GUI_ICONS_TEXTURE);

            int temperature = HardcoreModComponents.TEMPERATURE.get(playerEntity).getTemperatureLevel();

            // TODO: This can be modeled with a linear equation, too much screwing with Desmos
            float modifier = -(20.0f / 7.0f);

            int k = Math.round(modifier * Math.abs(temperature) + 20.0f);

            if (this.ticks % (k * 3 + 1) == 0) {
                y += (this.random.nextInt(3) - 1);
            }

            // TODO: Scale properly, negative temperatures DO NOT WORK
            float p = Math.abs((temperature - 7) / 14.0f);

            int j = this.getColorForTemperature(temperature);
            float r = (float)(j >> 16 & 255) / 255.0f;
            float g = (float)(j >> 8 & 255) / 255.0f;
            float b = (float)(j & 255) / 255.0f;

            RenderSystem.color4f(r, g, b, 1.0f);

            this.drawTexture(matrices, x, y, 0, 0, 16, 16);

            // TODO: Needed?
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
}
