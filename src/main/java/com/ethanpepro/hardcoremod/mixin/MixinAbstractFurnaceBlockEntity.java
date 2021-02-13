package com.ethanpepro.hardcoremod.mixin;

import com.ethanpepro.hardcoremod.HardcoreMod;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class MixinAbstractFurnaceBlockEntity extends LockableContainerBlockEntity implements SidedInventory, RecipeUnlocker, RecipeInputProvider, Tickable {
    @Shadow
    protected DefaultedList<ItemStack> inventory;

    @Shadow
    protected abstract boolean canAcceptRecipeOutput(@Nullable Recipe<?> recipe);

    protected MixinAbstractFurnaceBlockEntity(BlockEntityType<?> blockEntityType) {
        super(blockEntityType);
    }

    @Inject(method = "craftRecipe(Lnet/minecraft/recipe/Recipe;)V", at = @At("HEAD"))
    private void craftRecipe(@Nullable Recipe<?> recipe, CallbackInfo info) {
        if (recipe != null && this.canAcceptRecipeOutput(recipe)) {
            ItemStack itemStack = this.inventory.get(0);
            ItemStack itemStack2 = recipe.getOutput();
            ItemStack itemStack3 = this.inventory.get(2);

            HardcoreMod.LOGGER.info("this.inventory.get(0)={}, recipe.getOutput()={}, this.inventory.get(2)={}", Registry.ITEM.getId(itemStack.getItem()).toString(), Registry.ITEM.getId(itemStack2.getItem()).toString(), Registry.ITEM.getId(itemStack3.getItem()).toString());
        }
    }
}
