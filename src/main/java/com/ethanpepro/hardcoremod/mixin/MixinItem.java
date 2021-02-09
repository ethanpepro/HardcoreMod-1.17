package com.ethanpepro.hardcoremod.mixin;

import com.ethanpepro.hardcoremod.api.food.*;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public abstract class MixinItem implements ItemConvertible, IExtendedFoodItem {
    @Shadow
    public abstract Item asItem();

    @Override
    public long getMaxAge() {
        ImmutableMap<Item, ExtendedFoodComponent> foodRegistry = ExtendedFoodRegistry.getExtendedFoodRegistry();
        Item item = this.asItem();
        ExtendedFoodComponent component = foodRegistry.get(item);

        return foodRegistry.containsKey(item) ? component.age : 0;
    }

    @Override
    public boolean canRot() {
        return this.getMaxAge() > 0;
    }

    @SuppressWarnings("ConstantConditions")
    private float getRotPercentage(ItemStack stack, @Nullable World world) {
        return world != null ? (float)(world.getTime() - ((IExtendedFoodItemStack)(Object)stack).getCurrentAge()) / this.getMaxAge() : 0.0f;
    }

    @SuppressWarnings("ConstantConditions")
    @Environment(EnvType.CLIENT)
    @Inject(method = "appendTooltip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Ljava/util/List;Lnet/minecraft/client/item/TooltipContext;)V", at = @At("TAIL"))
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo info) {
        ImmutableMap<Item, ExtendedFoodComponent> foodRegistry = ExtendedFoodRegistry.getExtendedFoodRegistry();
        Item item = stack.getItem();
        ExtendedFoodComponent component = foodRegistry.get(item);

        if (world != null && foodRegistry.containsKey(item)) {
            tooltip.add(LiteralText.EMPTY);
            tooltip.add(new TranslatableText("item.rot.status").formatted(Formatting.GRAY));

            if (this.canRot()) {
                float rot = ((IExtendedFoodItemStack)(Object)stack).getCurrentAge() != 0 ? this.getRotPercentage(stack, world) : 0.0f;
                ExtendedFoodStates state = ExtendedFoodStates.getStateForPercentage(rot);

                tooltip.add(new TranslatableText("item.rot.state." + state.getName()).formatted(state.getFormat()));
                // TODO: Make config option
                if (!world.getLevelProperties().isHardcore()) {
                    tooltip.add((new TranslatableText("item.rot.format", (int)(Math.min(rot, 1.0f) * 100), new TranslatableText("item.rot.spoiled"))).formatted(Formatting.GRAY));
                }
            } else if (!ExtendedFoodSpecialStates.containsVisibleSpecials(component.special)) {
                tooltip.add(new TranslatableText("item.rot.state.other.preserved").formatted(Formatting.GOLD));
            }

            for (String special : component.special) {
                ExtendedFoodSpecialStates state = ExtendedFoodSpecialStates.getStateForSpecial(special);

                if (state != null && state.canDisplay()) {
                    tooltip.add(new TranslatableText("item.rot.state.special." + state.getName()).formatted(state.getFormat()));
                }
            }
        }
    }
}
