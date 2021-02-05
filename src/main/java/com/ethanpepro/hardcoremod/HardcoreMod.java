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

public class HardcoreMod implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("hardcoremod");

    @Override
    public void onInitialize() {
        HardcoreMod.LOGGER.debug("onInitialize()");

        HardcoreModTemperatures.register();

        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return new Identifier("hardcoremod", "hardcoremod_resources");
            }

            // TODO: Abstract out handling
            // TODO: Rename items.json to each creative category and iterate
            // TODO: ITEM, BLOCK
            @Override
            public void apply(ResourceManager manager) {
                ExtendedFoodRegistry.clearExtendedFood();

                for (Identifier identifier : manager.findResources("rot", path -> path.endsWith(".json"))) {
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
