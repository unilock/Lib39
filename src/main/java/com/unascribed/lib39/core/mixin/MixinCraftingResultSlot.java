package com.unascribed.lib39.core.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.unascribed.lib39.core.Lib39Mod;
import com.unascribed.lib39.core.P39;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.slot.CraftingResultSlot;

@Mixin(CraftingResultSlot.class)
public class MixinCraftingResultSlot {

	@Shadow @Final
	private CraftingInventory input;
	@Shadow @Final
	private PlayerEntity player;
	
	@Inject(at=@At("HEAD"), method="onCrafted(Lnet/minecraft/item/ItemStack;)V")
	protected void lib39Core$onCrafted(ItemStack stack, CallbackInfo ci) {
		if (player == null) return;
		if (player.world.isClient) return;
		Optional<CraftingRecipe> recipe = player.world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, input, player.world);
		if (recipe.isPresent() && Lib39Mod.craftingSounds.containsKey(recipe.get().getId())) {
			P39.worlds().playSound(player.world, null, player.getX(), player.getY(), player.getZ(),
					Lib39Mod.craftingSounds.get(recipe.get().getId()), player.getSoundCategory(), 1, 1);
		}
	}
	
}
