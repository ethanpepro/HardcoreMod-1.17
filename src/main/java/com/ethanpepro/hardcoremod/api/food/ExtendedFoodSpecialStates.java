package com.ethanpepro.hardcoremod.api.food;

import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.List;

// TODO: Work this out?
public enum ExtendedFoodSpecialStates {
    POISONOUS("poisonous", Formatting.RED, true),
    ENCHANTED("enchanted", Formatting.AQUA, true);

    private final String name;
    private final Formatting format;
    private final boolean display;

    ExtendedFoodSpecialStates (String name, Formatting format, boolean display) {
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

    public static ExtendedFoodSpecialStates getStateForSpecial(String special) {
        return Arrays.stream(values()).filter(value -> value.getName().equals(special)).findFirst().orElse(null);
    }

    // TODO: Not sure about this, better way?
    public static boolean containsVisibleSpecials(List<String> special) {
        for (String value : special) {
            ExtendedFoodSpecialStates state = getStateForSpecial(value);

            if (state.canDisplay()) {
                return true;
            }
        }

        return false;
    }
}
