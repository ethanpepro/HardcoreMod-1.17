package com.ethanpepro.hardcoremod.api.food;

import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

// TODO: Work this out?
public enum ExtendedFoodSpecialStates {
    // TODO: Reserve poisonous for food that does *not* rot and *not* for food! Get rid of it on normal items
    // TODO: Should poisonous items rot?
    POISONOUS("poisonous", Formatting.RED, true),
    // TODO: Should enchanted items rot?
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

    @Nullable
    public static ExtendedFoodSpecialStates getStateForSpecial(String special) {
        return Arrays.stream(values()).filter(value -> value.getName().equals(special)).findFirst().orElse(null);
    }

    // TODO: Not sure about this, better way?
    public static boolean containsVisibleSpecials(List<String> special) {
        for (String value : special) {
            ExtendedFoodSpecialStates state = getStateForSpecial(value);

            if (state != null && state.canDisplay()) {
                return true;
            }
        }

        return false;
    }
}
