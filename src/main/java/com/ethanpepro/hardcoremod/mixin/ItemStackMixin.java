package com.ethanpepro.hardcoremod.mixin;

import com.ethanpepro.hardcoremod.HardcoreMod;
import com.ethanpepro.hardcoremod.api.food.*;
import com.google.common.collect.ImmutableMap;
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

    // TODO: Abstract out
    private float getRotPercentage(Item item, World world) {
        return (float)(world.getTime() - (((ExtendedFoodItemStack)this).getCurrentAge())) / ((ExtendedFoodItem)item).getMaxAge();
    }

    // TODO: dropStack, dropItem, Piglin exceptions
    // TODO: For the crafting system, since this is just a massive place to throw notes, take the average rot % of the ingredients to make new item rot %
    // TODO: System for transferring rot between crafting items, especially ingredient -> ingredient block
    // TODO: Make individually stackable after 100%?
    // TODO: Bigger issue, villager trades with food are impossible
    // TODO: Tick elsewhere (mob drops will not get tagged, etc.)
    @Inject(method = "inventoryTick(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;IZ)V", at = @At("TAIL"))
    public void inventoryTick(World world, Entity entity, int slot, boolean selected, CallbackInfo info) {
        if (((ExtendedFoodItem)this.getItem()).canRot()) {
            if (!this.getOrCreateTag().contains("age")) {
                this.getOrCreateTag().putLong("age", world.getTime());
            }
        }
    }

    // TODO: Check for edge cases!
    // TODO: Server
    // TODO: Buckets and other special or general (non-food) items
    @Inject(method = "finishUsing(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    public void finishUsing(World world, LivingEntity user, CallbackInfoReturnable<ItemStack> info) {
        ImmutableMap<Item, ExtendedFoodComponent> foodRegistry = ExtendedFoodRegistry.getExtendedFoodRegistry();
        Item item = this.getItem();
        ExtendedFoodComponent component = foodRegistry.get(item);

        if (foodRegistry.containsKey(item)) {
            if (((ExtendedFoodItem)this.getItem()).canRot() || component.special.contains(ExtendedFoodSpecialStates.POISONOUS.getName())) {
                // TODO: Scale % chance of illness based on rot % > 0.5f (configurable)
                float rot = this.getRotPercentage(item, world);

                // TODO: Avoid multiple checks, resolve this once API is set in
                // TODO: Food marked "poisonous" should be 100%? Or scale too?
                // TODO: Not in here, but food at 100% should be tooltipped poisonous?
                // TODO: Not in here too, but maybe (internal) "unsafe" tooltip on food marked non-poisonous? Separate poison and spoilage
                if (rot > 0.5f || component.special.contains(ExtendedFoodSpecialStates.POISONOUS.getName())) {
                    HardcoreMod.LOGGER.info("!");
                }
            }
        }
    }
}
