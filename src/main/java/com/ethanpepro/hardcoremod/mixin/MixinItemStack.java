package com.ethanpepro.hardcoremod.mixin;

import com.ethanpepro.hardcoremod.api.food.IExtendedFoodItem;
import com.ethanpepro.hardcoremod.api.food.IExtendedFoodItemStack;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// TODO: Clean
@Mixin(ItemStack.class)
public abstract class MixinItemStack implements IExtendedFoodItemStack {
    @Shadow
    public abstract Item getItem();

    @Shadow
    public abstract CompoundTag getOrCreateTag();

    @Shadow
    public @Nullable abstract CompoundTag getTag();

    @Shadow
    public abstract boolean isEmpty();

    @Override
    public long getCurrentAge() {
        return this.getTag() != null ? this.getTag().getLong("age") : 0;
    }

    // TODO: Just for fun, this can never work out though (see main class)
    @Inject(method = "getMaxCount()I", at = @At("HEAD"), cancellable = true)
    private void getMaxCount(CallbackInfoReturnable<Integer> info) {
        if (!this.isEmpty() && ((IExtendedFoodItem)this.getItem()).canRot()) {
            info.setReturnValue(1);
            info.cancel();
        }
    }

    @Inject(method = "inventoryTick(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;IZ)V", at = @At("TAIL"))
    private void inventoryTick(World world, Entity entity, int slot, boolean selected, CallbackInfo info) {
        if (!this.isEmpty() && ((IExtendedFoodItem)this.getItem()).canRot()) {
            if (!this.getOrCreateTag().contains("age")) {
                this.getOrCreateTag().putLong("age", world.getTime());
            }
        }
    }
}
