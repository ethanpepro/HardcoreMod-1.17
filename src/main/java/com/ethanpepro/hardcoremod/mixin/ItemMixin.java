package com.ethanpepro.hardcoremod.mixin;

import com.ethanpepro.hardcoremod.api.food.ExtendedFoodComponent;
import com.ethanpepro.hardcoremod.api.food.ExtendedFoodItem;
import com.ethanpepro.hardcoremod.api.food.ExtendedFoodItemStack;
import com.ethanpepro.hardcoremod.api.food.FoodRegistry;
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

// TODO: Clean
@Mixin(Item.class)
public abstract class ItemMixin implements ItemConvertible, ExtendedFoodItem {
    @Shadow
    public abstract Item asItem();

    @Override
    public long getMaxAge() {
        ImmutableMap<Item, ExtendedFoodComponent> foodRegistry = FoodRegistry.getFoodRegistry();
        Item item = this.asItem();

        if (!foodRegistry.containsKey(item)) {
            return 0;
        }

        return foodRegistry.get(item).age;
    }

    @Override
    public boolean canRot() {
        return this.getMaxAge() > 0;
    }

    // TODO: Make dynamic
    private String getStringForRot(float rot) {
        if (rot < 0.25f) {
            return "fresh";
        } else if (rot < 0.5f) {
            return "plain";
        } else if (rot < 0.75f) {
            return "stale";
        } else {
            return "rancid";
        }
    }

    // TODO: Make dynamic
    private Formatting getColorForRot(float rot) {
        if (rot < 0.25f) {
            return Formatting.GREEN;
        } else if (rot < 0.5f) {
            return Formatting.DARK_GREEN;
        } else if (rot < 0.75f) {
            return Formatting.GRAY;
        } else {
            return Formatting.RED;
        }
    }

    private float getRotPercentage(ItemStack stack, @Nullable World world) {
        return world != null ? (float)(world.getTime() - (((ExtendedFoodItemStack)(Object)stack).getCurrentAge())) / this.getMaxAge() : 0.0f;
    }

    // TODO: Cleanup
    @Environment(EnvType.CLIENT)
    @Inject(method = "appendTooltip", at = @At("TAIL"))
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo info) {
        ImmutableMap<Item, ExtendedFoodComponent> foodRegistry = FoodRegistry.getFoodRegistry();
        Item item = stack.getItem();
        ExtendedFoodComponent component = foodRegistry.get(item);

        if (foodRegistry.containsKey(item)) {
            tooltip.add(LiteralText.EMPTY);
            tooltip.add(new TranslatableText("item.rot.status").formatted(Formatting.GRAY));

            if (this.canRot()) {
                float rot = (((ExtendedFoodItemStack)(Object)stack)).getCurrentAge() != 0 ? this.getRotPercentage(stack, world) : 0.0f;

                tooltip.add(new TranslatableText("item.rot.state." + this.getStringForRot(rot)).formatted(this.getColorForRot(rot)));
                tooltip.add((new TranslatableText("item.rot.format", (int)(Math.min(rot, 1.0f) * 100), new TranslatableText("item.rot.spoiled"))).formatted(Formatting.GRAY));
            } else {
                if (component.special.contains("poisonous")) {
                    tooltip.add(new TranslatableText("item.rot.state.special.poisonous").formatted(Formatting.DARK_RED));
                } else if (component.special.contains("enchanted")) {
                    tooltip.add(new TranslatableText("item.rot.state.special.enchanted").formatted(Formatting.YELLOW));
                } else {
                    tooltip.add(new TranslatableText("item.rot.state.special.preserved").formatted(Formatting.GOLD));
                }
            }
        }
    }
}
