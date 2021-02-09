package com.ethanpepro.hardcoremod.api.food;

import java.util.List;

// TODO: For food preservation systems, like ice boxes, periodically apply a long ageExtension modifier (this would be done as a tag) to age to get a new difference for a time in the future when the food spoils
// TODO: An ice box example would have a value contained within it that has a long maxAgeExtension that once is reached, the food cannot be preserved any longer
// TODO: It cannot reverse current % rot, but extend how long?
// TODO; Make all food items non-stackable? Go back to the old days? Adjust loot tables and stuff too!
public class ExtendedFoodComponent {
    // Identifier output;
    public long age;
    public List<String> special;
}
