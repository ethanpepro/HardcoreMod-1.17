package com.ethanpepro.hardcoremod.mixin;

import com.ethanpepro.hardcoremod.api.food.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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

    @Override
    public long getCurrentAge() {
        return this.getTag() != null ? this.getTag().getLong("age") : 0;
    }

    @Inject(method = "inventoryTick(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;IZ)V", at = @At("TAIL"))
    public void inventoryTick(World world, Entity entity, int slot, boolean selected, CallbackInfo info) {
        if (((IExtendedFoodItem)this.getItem()).canRot()) {
            if (!this.getOrCreateTag().contains("age")) {
                this.getOrCreateTag().putLong("age", world.getTime());
            }
        }
    }

    @Inject(method = "finishUsing(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    public void finishUsing(World world, LivingEntity user, CallbackInfoReturnable<ItemStack> info) {

    }
}
