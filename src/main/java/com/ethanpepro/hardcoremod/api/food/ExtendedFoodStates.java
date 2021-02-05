package com.ethanpepro.hardcoremod.api.food;

import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringIdentifiable;

import java.util.Arrays;

public enum ExtendedFoodStates {
    FRESH("fresh", Formatting.GREEN),
    PLAIN("plain", Formatting.DARK_GREEN),
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
        return format;
    }

    // Based Ava#4982
    public static ExtendedFoodStates getStateForPercentage(float percentage) {
        ExtendedFoodStates[] values = ExtendedFoodStates.values();
        return values[(int)Math.floor(values.length * percentage)];
    }
}

// TODO: SpecialExtendedFoodStates
// TODO: Matches data inside special: [] to a string inside a value in SpecialExtendedFoodStates, gets color
// TODO: Simple matching function, used for building tooltip
