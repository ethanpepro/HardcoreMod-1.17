package com.ethanpepro.hardcoremod.api.temperature.modifier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public interface ITemperatureModifier {
    @NotNull Identifier getIdentifier();

    boolean isPlayerSpecific();

    float getPlayerModifier(@NotNull PlayerEntity player);

    float getWorldModifier(@NotNull World world, @NotNull BlockPos pos);
}
