package com.ethanpepro.hardcoremod.mixin;

import com.ethanpepro.hardcoremod.api.food.IExtendedFoodItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.EnvironmentInterface;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.client.block.ChestAnimationProgress;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@EnvironmentInterface(value = EnvType.CLIENT, itf = ChestAnimationProgress.class)
@Mixin(ChestBlockEntity.class)
public abstract class MixinChestBlockEntity extends LootableContainerBlockEntity implements ChestAnimationProgress, Tickable {
    @Shadow
    private DefaultedList<ItemStack> inventory;

    protected MixinChestBlockEntity(BlockEntityType<?> blockEntityType) {
        super(blockEntityType);
    }

    @Inject(method = "onInvOpenOrClose()V", at = @At("TAIL"))
    private void onInvOpenOrClose(CallbackInfo info) {
        for (int i = 0; i < this.inventory.size(); i++) {
            ItemStack stack = this.inventory.get(i);
            Item item = stack.getItem();

            if (!stack.isEmpty() && ((IExtendedFoodItem)item).canRot()) {
                if (!stack.getOrCreateTag().contains("age")) {
                    stack.getOrCreateTag().putLong("age", world != null ? world.getTime() : -1);
                }
            }
        }
    }
}
