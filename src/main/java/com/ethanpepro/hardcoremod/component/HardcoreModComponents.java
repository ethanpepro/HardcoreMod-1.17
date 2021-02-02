package com.ethanpepro.hardcoremod.component;

import com.ethanpepro.hardcoremod.component.temperature.TemperatureComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import nerdhub.cardinal.components.api.util.RespawnCopyStrategy;
import net.minecraft.util.Identifier;

public class HardcoreModComponents implements EntityComponentInitializer {
    public static final ComponentKey<TemperatureComponent> TEMPERATURE = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier("hardcoremod", "temperature"), TemperatureComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry entityComponentFactoryRegistry) {
        entityComponentFactoryRegistry.registerForPlayers(TEMPERATURE, TemperatureComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
    }
}
