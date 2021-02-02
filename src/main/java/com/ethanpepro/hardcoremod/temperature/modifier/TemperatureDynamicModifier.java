package com.ethanpepro.hardcoremod.temperature.modifier;

import com.ethanpepro.hardcoremod.api.temperature.modifier.ITemperatureDynamicModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public abstract class TemperatureDynamicModifier implements ITemperatureDynamicModifier {
    private final Identifier identifier;

    public TemperatureDynamicModifier(Identifier identifier) {
        this.identifier = identifier;
    }

    @Override
    public @NotNull Identifier getIdentifier() {
        return this.identifier;
    }

    @Override
    public abstract boolean isPlayerSpecific();

    @Override
    public float getPlayerModifier(@NotNull PlayerEntity player, float currentTemperature) {
        return currentTemperature;
    }

    @Override
    public float getWorldModifier(@NotNull World world, @NotNull BlockPos pos, float currentTemperature) {
        return currentTemperature;
    }
}
