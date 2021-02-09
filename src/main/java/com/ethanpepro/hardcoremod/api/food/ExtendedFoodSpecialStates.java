package com.ethanpepro.hardcoremod.api.food;

import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public enum ExtendedFoodSpecialStates {
    // TODO: Should poisonous items rot?
    POISONOUS("poisonous", Formatting.RED, true),
    // TODO: Should enchanted items rot?
    ENCHANTED("enchanted", Formatting.AQUA, true);

    private final String name;
    private final Formatting format;
    private final boolean display;

    ExtendedFoodSpecialStates(String name, Formatting format, boolean display) {
        this.name = name;
        this.format = format;
        this.display = display;
    }

    public String getName() {
        return this.name;
    }

    public Formatting getFormat() {
        return this.format;
    }

    public boolean canDisplay() {
        return this.display;
    }

    @Nullable
    public static ExtendedFoodSpecialStates getStateForSpecial(String special) {
        return Arrays.stream(values()).filter(value -> value.getName().equals(special)).findFirst().orElse(null);
    }

    public static boolean containsVisibleSpecials(List<String> special) {
        return special.stream().map(ExtendedFoodSpecialStates::getStateForSpecial).anyMatch(state -> state != null && state.canDisplay());
    }
}
