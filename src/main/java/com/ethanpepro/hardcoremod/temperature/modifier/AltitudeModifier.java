package com.ethanpepro.hardcoremod.temperature.modifier;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class AltitudeModifier extends TemperatureModifier {
    private static final Identifier TEMPERATURE_MODIFIER_ID = new Identifier("hardcoremod", "altitude");

    public AltitudeModifier() {
        super(TEMPERATURE_MODIFIER_ID);
    }

    @Override
    public boolean isPlayerSpecific() {
        return false;
    }

    @Override
    public float getWorldModifier(@NotNull World world, @NotNull BlockPos pos) {
        return 7.0f;
    }
}
