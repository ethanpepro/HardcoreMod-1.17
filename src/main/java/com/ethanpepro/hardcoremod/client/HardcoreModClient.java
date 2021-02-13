package com.ethanpepro.hardcoremod.client;

import com.ethanpepro.hardcoremod.HardcoreMod;
import com.ethanpepro.hardcoremod.config.HardcoreModConfig;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigHolder;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.ActionResult;

@Environment(EnvType.CLIENT)
public class HardcoreModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HardcoreMod.LOGGER.debug("onInitializeClient()");

        ConfigHolder<HardcoreModConfig> holder = AutoConfig.register(HardcoreModConfig.class, GsonConfigSerializer::new);

        holder.registerSaveListener((manager, data) -> {
            return ActionResult.SUCCESS;
        });

        holder.registerLoadListener((manager, newData) -> {
            return ActionResult.SUCCESS;
        });
    }
}
