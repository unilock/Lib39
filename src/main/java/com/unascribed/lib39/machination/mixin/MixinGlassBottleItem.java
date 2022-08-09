package com.unascribed.lib39.machination.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.unascribed.lib39.machination.logic.SmashCloudLogic;
import com.unascribed.lib39.machination.recipe.PistonSmashingRecipe;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

@Mixin(GlassBottleItem.class)
public abstract class MixinGlassBottleItem {

	@Inject(at=@At("HEAD"), method="use", cancellable=true)
	public void lib39Machination$collectSmashCloud(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> ci) {
		ItemStack stack = user.getStackInHand(hand);
		PistonSmashingRecipe r = SmashCloudLogic.consumeGasCloud(world, user.getBoundingBox().expand(2));
		if (r != null) {
			user.incrementStat(Stats.CRAFTED.getOrCreateStat(r.getCloudOutput().getItem()));
			ci.setReturnValue(TypedActionResult.success(fill(stack, user, r.getCloudOutput().copy()), world.isClient()));
		}
	}

	@Shadow
	protected abstract ItemStack fill(ItemStack itemStack, PlayerEntity playerEntity, ItemStack itemStack2);

}
