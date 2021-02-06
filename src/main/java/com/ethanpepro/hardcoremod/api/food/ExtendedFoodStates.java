package com.ethanpepro.hardcoremod.api.food;

import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

public enum ExtendedFoodStates {
    FRESH("fresh", Formatting.GREEN),
    RIPE("ripe", Formatting.DARK_GREEN),
    STALE("stale", Formatting.LIGHT_PURPLE),
    RANCID("rancid", Formatting.RED);

    private final String name;
    private final Formatting format;

    ExtendedFoodStates (String name, Formatting format) {
        this.name = name;
        this.format = format;
    }

    public String getName() {
        return this.name;
    }

    public Formatting getFormat() {
        return this.format;
    }

    // Based Ava#4982
    @NotNull
    public static ExtendedFoodStates getStateForPercentage(float percentage) {
        ExtendedFoodStates[] values = ExtendedFoodStates.values();
        return values[(int)MathHelper.clamp(values.length * percentage, 0, values.length - 1)];
    }
}
