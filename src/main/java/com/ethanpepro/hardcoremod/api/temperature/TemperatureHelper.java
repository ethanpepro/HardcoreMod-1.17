package com.ethanpepro.hardcoremod.api.temperature;

import com.ethanpepro.hardcoremod.api.temperature.modifier.ITemperatureDynamicModifier;
import com.ethanpepro.hardcoremod.api.temperature.modifier.ITemperatureModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class TemperatureHelper {
    // TODO: Remove once configs are in
    public static final int EQUILIBRIUM_TEMPERATURE = TemperatureRange.TEMPERATE.getMiddle();
    public static final int HYPERTHERMIA_THRESHOLD = TemperatureRange.HOT.getMiddle();
    public static final int HYPOTHERMIA_THRESHOLD = TemperatureRange.CHILLY.getMiddle();

    public static int clampTemperatureInRange(float temperature) {
        return MathHelper.clamp(Math.round(temperature), TemperatureRange.FREEZING.getLowerBound(), TemperatureRange.BURNING.getUpperBound());
    }

    public static TemperatureRange getTemperatureRangeFromTemperature(int temperature) {
        for (TemperatureRange temperatureRange : TemperatureRange.values()) {
            if (temperatureRange.isInRange(temperature)) {
                return temperatureRange;
            }
        }

        return (temperature > 0) ? TemperatureRange.BURNING : TemperatureRange.FREEZING;
    }

    public static float getPlayerTemperatureTarget(@NotNull PlayerEntity player) {
        float newTemperature = EQUILIBRIUM_TEMPERATURE;

        // TODO: Optimize?
        for (ITemperatureModifier modifier : TemperatureRegistry.getTemperatureModifiers().values()) {
            if (modifier.isPlayerSpecific()) {
                newTemperature += modifier.getPlayerModifier(player);
            }
        }

        for (ITemperatureDynamicModifier dynamicModifier : TemperatureRegistry.getTemperatureDynamicModifiers().values()) {
            if (dynamicModifier.isPlayerSpecific()) {
                newTemperature = dynamicModifier.getPlayerModifier(player, newTemperature);
            }
        }

        return newTemperature;
    }

    public static float getWorldTemperatureTarget(@NotNull World world, @NotNull BlockPos pos) {
        float newTemperature = EQUILIBRIUM_TEMPERATURE;

        // TODO: Optimize?
        for (ITemperatureModifier modifier : TemperatureRegistry.getTemperatureModifiers().values()) {
            if (!modifier.isPlayerSpecific()) {
                newTemperature += modifier.getWorldModifier(world, pos);
            }
        }

        for (ITemperatureDynamicModifier dynamicModifier : TemperatureRegistry.getTemperatureDynamicModifiers().values()) {
            if (!dynamicModifier.isPlayerSpecific()) {
                newTemperature = dynamicModifier.getWorldModifier(world, pos, newTemperature);
            }
        }

        return newTemperature;
    }
}
