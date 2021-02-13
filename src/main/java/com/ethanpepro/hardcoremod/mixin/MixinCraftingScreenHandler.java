package com.ethanpepro.hardcoremod.mixin;

import com.ethanpepro.hardcoremod.HardcoreMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingScreenHandler.class)
public abstract class MixinCraftingScreenHandler extends AbstractRecipeScreenHandler<CraftingInventory> {
    public MixinCraftingScreenHandler(ScreenHandlerType<?> screenHandlerType, int i) {
        super(screenHandlerType, i);
    }

    @Inject(method = "updateResult(ILnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/inventory/CraftingInventory;Lnet/minecraft/inventory/CraftingResultInventory;)V", at = @At("HEAD"))
    private static void updateResult(int syncId, World world, PlayerEntity player, CraftingInventory craftingInventory, CraftingResultInventory resultInventory, CallbackInfo info) {
        for (int i = 0; i < craftingInventory.size(); i++) {
            ItemStack stack = craftingInventory.getStack(i);
            if (!stack.isEmpty()) {
                HardcoreMod.LOGGER.info("craftingInventory[{}]={}", i, Registry.ITEM.getId(stack.getItem()).toString());
            }
        }
    }
}
