package com.ethanpepro.hardcoremod.mixin;

import com.ethanpepro.hardcoremod.api.food.ExtendedFoodItem;
import com.ethanpepro.hardcoremod.api.food.ExtendedFoodItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// TODO: Clean
@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ExtendedFoodItemStack {
    @Shadow
    public abstract Item getItem();

    @Shadow
    public abstract CompoundTag getOrCreateTag();

    @Shadow
    public @Nullable abstract CompoundTag getTag();

    @Override
    public long getCurrentAge() {
        return this.getTag() != null ? this.getTag().getLong("age") : 0;
    }

    @Inject(method = "inventoryTick", at = @At("TAIL"))
    public void inventoryTick(World world, Entity entity, int slot, boolean selected, CallbackInfo info) {
        if (((ExtendedFoodItem)this.getItem()).canRot()) {
            if (!this.getOrCreateTag().contains("age")) {
                this.getOrCreateTag().putLong("age", world.getTime());
            }
        }
    }
}
