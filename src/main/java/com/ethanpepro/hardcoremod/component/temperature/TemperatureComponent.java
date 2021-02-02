package com.ethanpepro.hardcoremod.component.temperature;

import com.ethanpepro.hardcoremod.HardcoreMod;
import com.ethanpepro.hardcoremod.api.temperature.TemperatureHelper;
import com.ethanpepro.hardcoremod.api.temperature.TemperatureRange;
import com.ethanpepro.hardcoremod.component.HardcoreModComponents;
import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class TemperatureComponent implements ComponentV3, AutoSyncedComponent, ServerTickingComponent {
    // TODO: Remove once configs are in
    private static final int TEMPERATURE_UPDATE_INTERVAL = 20;
    private static final int MIN_TICK_LIMIT = 20;
    private static final int MAX_TICK_LIMIT = 800;

    private final PlayerEntity player;

    private int temperatureLevel;
    private int temperatureTickTimer;

    private int targetTemperature;

    private int targetTemperatureUpdateTimer;

    public TemperatureComponent(PlayerEntity player) {
        this.player = player;

        this.temperatureLevel = TemperatureHelper.EQUILIBRIUM_TEMPERATURE;
    }

    // TODO: Public getters outside of class only, private members inside of class only
    public int getTemperatureLevel() {
        return this.temperatureLevel;
    }

    public int getTargetTemperature() {
        return this.targetTemperature;
    }

    private int getTargetTemperatureForPlayer(@NotNull PlayerEntity player) {
        float targetTemperature = TemperatureHelper.EQUILIBRIUM_TEMPERATURE;

        targetTemperature += TemperatureHelper.getWorldTemperatureTarget(player.getEntityWorld(), player.getBlockPos());

        targetTemperature += TemperatureHelper.getPlayerTemperatureTarget(player);

        return TemperatureHelper.clampTemperatureInRange(targetTemperature);
    }

    // TODO: Can be simplified?
    private int getTemperatureTickLimit() {
        int tickRange = MAX_TICK_LIMIT - MIN_TICK_LIMIT;
        int temperatureRange = TemperatureRange.BURNING.getUpperBound() - TemperatureRange.FREEZING.getLowerBound();
        int currentRange = Math.abs(this.temperatureLevel - this.targetTemperature);

        return Math.max(MIN_TICK_LIMIT, MAX_TICK_LIMIT - Math.round(((float)currentRange * (float)tickRange) / (float)temperatureRange));
    }

    @Override
    public void serverTick() {
        this.targetTemperatureUpdateTimer++;

        if (this.targetTemperatureUpdateTimer >= TEMPERATURE_UPDATE_INTERVAL) {
            this.targetTemperatureUpdateTimer = 0;

            this.targetTemperature = this.getTargetTemperatureForPlayer(this.player);

            HardcoreMod.LOGGER.debug("[serverTick] {} -> {} ({}/{})", this.temperatureLevel, this.targetTemperature, this.temperatureTickTimer, this.getTemperatureTickLimit());
        }

        this.temperatureTickTimer++;

        if (this.temperatureTickTimer >= this.getTemperatureTickLimit()) {
            this.temperatureTickTimer = 0;

            if (this.temperatureLevel != this.targetTemperature) {
                this.temperatureLevel += Integer.signum(this.targetTemperature - this.temperatureLevel);
            }

            // TODO: Shove back in effects here
            // TODO: Hyperthermia at TemperatureHelper.HYPERTHERMIA_THRESHOLD
            // TODO: Hypothermia at TemperatureHelper.HYPOTHERMIA_THRESHOLD

            HardcoreModComponents.TEMPERATURE.sync(this.player);
        }
    }

    @Override
    public void readFromNbt(CompoundTag compoundTag) {
        this.temperatureLevel = compoundTag.getInt("temperatureLevel");
        this.temperatureTickTimer = compoundTag.getInt("temperatureTickTimer");

        HardcoreModComponents.TEMPERATURE.sync(this.player);
    }

    @Override
    public void writeToNbt(CompoundTag compoundTag) {
        compoundTag.putInt("temperatureLevel", this.temperatureLevel);
        compoundTag.putInt("temperatureTickTimer", this.temperatureTickTimer);
    }

    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player) {
        // TODO: Compare in server
        HardcoreMod.LOGGER.debug("player.getCameraEntity()={}, player={}", player.getCameraEntity() == this.player, player == this.player);

        return player == this.player;
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeVarInt(this.temperatureLevel);
        buf.writeVarInt(this.temperatureTickTimer);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.temperatureLevel = buf.readVarInt();
        this.temperatureTickTimer = buf.readVarInt();

        HardcoreModComponents.TEMPERATURE.sync(this.player);
    }
}
