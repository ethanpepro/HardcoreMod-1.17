package com.ethanpepro.hardcoremod.mixin;

import com.ethanpepro.hardcoremod.api.food.IExtendedFoodItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class MixinItemEntity extends Entity {
    public MixinItemEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
    private void init(World world, double x, double y, double z, ItemStack stack, CallbackInfo info) {
        if (!stack.isEmpty() && ((IExtendedFoodItem)stack.getItem()).canRot()) {
            if (!stack.getOrCreateTag().contains("age")) {
                stack.getOrCreateTag().putLong("age", world.getTime());
            }
        }
    }
}
