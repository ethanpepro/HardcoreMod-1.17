package com.ethanpepro.hardcoremod.config;

import com.ethanpepro.hardcoremod.config.screen.HardcoreModConfigScreen;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;

public class HardcoreModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return HardcoreModConfigScreen::new;
    }
}
