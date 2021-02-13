package com.ethanpepro.hardcoremod.api.food;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.Item;
import org.jetbrains.annotations.NotNull;

public class ExtendedFoodRegistry {
    private static final Object2ObjectOpenHashMap<Item, ExtendedFoodComponent> extendedFoodRegistry = new Object2ObjectOpenHashMap<>();

    public static void registerExtendedFood(Item item, ExtendedFoodComponent component) {
        if (!extendedFoodRegistry.containsKey(item)) {
            extendedFoodRegistry.put(item, component);
        }
    }

    public static void clearExtendedFood() {
        extendedFoodRegistry.clear();
    }

    public @NotNull static ImmutableMap<Item, ExtendedFoodComponent> getExtendedFoodRegistry() {
        return ImmutableMap.copyOf(extendedFoodRegistry);
    }
}
