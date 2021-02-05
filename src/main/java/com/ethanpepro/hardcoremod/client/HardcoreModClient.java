package com.ethanpepro.hardcoremod.client;

import com.ethanpepro.hardcoremod.HardcoreMod;
import com.ethanpepro.hardcoremod.config.HardcoreModConfig;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class HardcoreModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HardcoreMod.LOGGER.debug("onInitializeClient()");

        AutoConfig.register(HardcoreModConfig.class, JanksonConfigSerializer::new);
    }
}
