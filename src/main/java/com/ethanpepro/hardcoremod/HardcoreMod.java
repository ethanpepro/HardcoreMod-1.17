package com.ethanpepro.hardcoremod;

import com.ethanpepro.hardcoremod.api.food.ExtendedFoodRegistry;
import com.ethanpepro.hardcoremod.temperature.HardcoreModTemperatures;
import com.ethanpepro.hardcoremod.api.food.ExtendedFoodComponent;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.item.Item;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;

// This is a landing page for general to-do stuff

// TODO: Absolutely need a (properly, not you Tweed) synced config system

// TODO: For food preservation systems, like ice boxes, periodically apply a long ageExtension modifier (this would be done as a tag) to age to get a new difference for a time in the future when the food spoils
// TODO: An ice box example would have a value contained within it that has a long maxAgeExtension that once is reached, the food cannot be preserved any longer
// TODO: It cannot reverse current % rot, but extend how long it stays fresh

// TODO: Make all food items non-stackable? Go back to the old days? Adjust loot tables and stuff too?
// TODO: If that happens, all ingredients would have to be unstackable, more work to do
// TODO: A merging system that remembers the % of each ingredient is completely unfeasible, although would make storage and trading work just fine (which would work around ingredients and food being unstackable due to tag mismatch)

// TODO: This would involve reworking villager trades
// TODO: Catch-all system for items not getting tagged (entity interaction/world-generated objects/smelting)

// TODO: Crafting, items that are rotten cannot be crafted with (maybe?)
// TODO: Ingredients that have block versions with have the output block tagged with the average rot % of the ingredients
// TODO: Block versions of ingredients cannot be crafted down back (gaining the system by average % and items cannot be forced to be unstackable)
// TODO: Maybe some convoluted rotting bundle system on block crafting down to item where once right-clicked, converts into ingredients with the proper tags
// TODO: Or override block crafting and just shift + right-click to craft down

// TODO: Methods to catch when items don't have tags already (only inventory and item drops are tagged right now)

// TODO: Preset system for which food data to load (on easy, items would rot slower, on hardcore, items rot "realistically")

// TODO: Finalize tag layout system

public class HardcoreMod implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("hardcoremod");

    @Override
    public void onInitialize() {
        HardcoreMod.LOGGER.debug("onInitialize()");

        HardcoreModTemperatures.register();

        // TODO: Same for temperature block heat modifiers
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return new Identifier("hardcoremod", "hardcoremod_resources");
            }

            // TODO: Abstract out handling
            // TODO: ITEM, BLOCK
            @Override
            public void apply(ResourceManager manager) {
                ExtendedFoodRegistry.clearExtendedFood();

                for (Identifier identifier : manager.findResources("rot", path -> path.endsWith(".json"))) {
                    HardcoreMod.LOGGER.debug("Processing resource: {}", identifier.toString());

                    try (InputStream stream = manager.getResource(identifier).getInputStream()) {
                        JsonParser parser = new JsonParser();
                        JsonObject root = parser.parse(new JsonReader(new InputStreamReader(stream))).getAsJsonObject();

                        Set<Map.Entry<String, JsonElement>> entries = root.entrySet();
                        for (Map.Entry<String, JsonElement> entry : entries) {
                            String key = entry.getKey();
                            Item item = Registry.ITEM.get(new Identifier(key));
                            if (!Registry.ITEM.getId(item).toString().equals(key)) {
                                throw new Exception("You fucked up putting this in: " + key);
                            }

                            Gson gson = new Gson();
                            JsonObject object = entry.getValue().getAsJsonObject();
                            ExtendedFoodComponent component = gson.fromJson(object, ExtendedFoodComponent.class);

                            ExtendedFoodRegistry.registerExtendedFood(item, component);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
