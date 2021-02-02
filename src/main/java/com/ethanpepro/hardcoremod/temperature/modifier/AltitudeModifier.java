package com.ethanpepro.hardcoremod.temperature.modifier;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class AltitudeModifier extends TemperatureModifier {
    public AltitudeModifier() {
        super(new Identifier("hardcoremod", "altitude"));
    }

    @Override
    public boolean isPlayerSpecific() {
        return false;
    }

    @Override
    public float getWorldModifier(@NotNull World world, @NotNull BlockPos pos) {
        return 0.0f;
    }
}
