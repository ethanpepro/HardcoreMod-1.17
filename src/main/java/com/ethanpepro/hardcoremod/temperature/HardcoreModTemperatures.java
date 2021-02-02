package com.ethanpepro.hardcoremod.temperature;

import com.ethanpepro.hardcoremod.api.temperature.TemperatureRegistry;
import com.ethanpepro.hardcoremod.temperature.modifier.AltitudeModifier;

public class HardcoreModTemperatures {
    public static void register() {
        TemperatureRegistry.registerModifier(new AltitudeModifier());
    }
}
