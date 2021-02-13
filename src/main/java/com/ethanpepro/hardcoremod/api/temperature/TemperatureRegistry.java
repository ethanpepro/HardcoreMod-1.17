package com.ethanpepro.hardcoremod.api.temperature;

import com.ethanpepro.hardcoremod.api.temperature.modifier.ITemperatureDynamicModifier;
import com.ethanpepro.hardcoremod.api.temperature.modifier.ITemperatureModifier;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;

public class TemperatureRegistry {
    private static final Object2ObjectOpenHashMap<Identifier, ITemperatureModifier> temperatureModifiers = new Object2ObjectOpenHashMap<>();

    private static final Object2ObjectOpenHashMap<Identifier, ITemperatureDynamicModifier> temepratureDynamicModifiers = new Object2ObjectOpenHashMap<>();

    public static void registerModifier(ITemperatureModifier modifier) {
        Identifier identifier = modifier.getIdentifier();

        if (!temperatureModifiers.containsKey(identifier)) {
            temperatureModifiers.put(identifier, modifier);
        }
    }

    public @NotNull static ImmutableMap<Identifier, ITemperatureModifier> getTemperatureModifiers() {
        return ImmutableMap.copyOf(temperatureModifiers);
    }

    public static void registerDynamicModifier(ITemperatureDynamicModifier dynamicModifier) {
        Identifier identifier = dynamicModifier.getIdentifier();

        if (!temepratureDynamicModifiers.containsKey(identifier)) {
            temepratureDynamicModifiers.put(identifier, dynamicModifier);
        }
    }

    public @NotNull static ImmutableMap<Identifier, ITemperatureDynamicModifier> getTemperatureDynamicModifiers() {
        return ImmutableMap.copyOf(temepratureDynamicModifiers);
    }
}
