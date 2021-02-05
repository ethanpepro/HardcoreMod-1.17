package com.ethanpepro.hardcoremod.api.food;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.Item;
import org.jetbrains.annotations.NotNull;

// TODO: Finalize name
public class FoodRegistry {
    private static final Object2ObjectOpenHashMap<Item, ExtendedFoodComponent> foodRegistry = new Object2ObjectOpenHashMap<>();

    public static void registerFood(Item item, ExtendedFoodComponent component) {
        if (!foodRegistry.containsKey(item)) {
            foodRegistry.put(item, component);
        }
    }

    public static void clearFood() {
        foodRegistry.clear();
    }

    @NotNull
    public static ImmutableMap<Item, ExtendedFoodComponent> getFoodRegistry() {
        return ImmutableMap.copyOf(foodRegistry);
    }
}
