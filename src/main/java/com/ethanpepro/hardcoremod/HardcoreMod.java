package com.ethanpepro.hardcoremod;

import com.ethanpepro.hardcoremod.temperature.HardcoreModTemperatures;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HardcoreMod implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("hardcoremod");

    @Override
    public void onInitialize() {
        HardcoreMod.LOGGER.debug("onInitialize()");

        HardcoreModTemperatures.register();
    }
}
