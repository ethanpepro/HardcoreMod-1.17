package com.ethanpepro.hardcoremod.temperature.modifier;

import com.ethanpepro.hardcoremod.api.temperature.TemperatureHelper;
import com.ethanpepro.hardcoremod.api.temperature.modifier.ITemperatureModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public abstract class TemperatureModifier implements ITemperatureModifier {
    private final Identifier identifier;

    public TemperatureModifier(Identifier identifier) {
        this.identifier = identifier;
    }

    // TODO: Check if a certain depth has been reached (prevents digging 1 x h x 1 holes to avoid underground chilling effects)
    protected boolean isUnderground(@NotNull World world, @NotNull BlockPos pos) {
        if (world.getTopPosition(Heightmap.Type.WORLD_SURFACE, pos).getY() <= pos.getY()) {
            return false;
        }

        for (BlockPos searchBlock : BlockPos.iterate(pos.add(-2, 0, -2), pos.add(2, 0, 2))) {
            if (world.isSkyVisible(searchBlock)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public @NotNull Identifier getIdentifier() {
        return this.identifier;
    }

    @Override
    public abstract boolean isPlayerSpecific();

    @Override
    public float getPlayerModifier(@NotNull PlayerEntity player) {
        return TemperatureHelper.EQUILIBRIUM_TEMPERATURE;
    }

    @Override
    public float getWorldModifier(@NotNull World world, @NotNull BlockPos pos) {
        return TemperatureHelper.EQUILIBRIUM_TEMPERATURE;
    }
}
