package com.ethanpepro.hardcoremod.api.temperature;

import com.ethanpepro.hardcoremod.api.temperature.modifier.ITemperatureDynamicModifier;
import com.ethanpepro.hardcoremod.api.temperature.modifier.ITemperatureModifier;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;

public class TemperatureRegistry {
    private static final LinkedHashMap<Identifier, ITemperatureModifier> modifiers = new LinkedHashMap<>();

    private static final LinkedHashMap<Identifier, ITemperatureDynamicModifier> dynamicModifiers = new LinkedHashMap<>();

    public static void registerModifier(ITemperatureModifier modifier) {
        Identifier identifier = modifier.getIdentifier();

        if (!modifiers.containsKey(identifier)) {
            modifiers.put(identifier, modifier);
        }
    }

    @NotNull
    public static ImmutableMap<Identifier, ITemperatureModifier> getTemperatureModifiers() {
        return ImmutableMap.copyOf(modifiers);
    }

    public static void registerDynamicModifier(ITemperatureDynamicModifier dynamicModifier) {
        Identifier identifier = dynamicModifier.getIdentifier();

        if (!dynamicModifiers.containsKey(identifier)) {
            dynamicModifiers.put(identifier, dynamicModifier);
        }
    }

    @NotNull
    public static ImmutableMap<Identifier, ITemperatureDynamicModifier> getTemperatureDynamicModifiers() {
        return ImmutableMap.copyOf(dynamicModifiers);
    }
}
