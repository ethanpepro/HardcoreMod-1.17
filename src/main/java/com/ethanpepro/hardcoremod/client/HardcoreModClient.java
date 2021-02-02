package com.ethanpepro.hardcoremod.client;

import com.ethanpepro.hardcoremod.HardcoreMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class HardcoreModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HardcoreMod.LOGGER.debug("onInitializeClient()");
    }
}
