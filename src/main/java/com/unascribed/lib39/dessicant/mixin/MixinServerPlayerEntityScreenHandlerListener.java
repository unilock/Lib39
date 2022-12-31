package com.unascribed.lib39.dessicant.mixin;

import java.util.Optional;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.unascribed.lib39.core.P39;
import com.unascribed.lib39.dessicant.DessicantData;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

@Mixin(targets="net/minecraft/unmapped/C_mxrobsgg$C_wdomexrk")
public abstract class MixinServerPlayerEntityScreenHandlerListener {

	@Shadow
	ServerPlayerEntity field_29183;
	
	@Inject(at=@At("HEAD"), method="onSlotUpdate")
	public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack, CallbackInfo ci) {
		if (!(handler.getSlot(slotId) instanceof CraftingResultSlot) && handler == field_29183.playerScreenHandler) {
			Identifier id = P39.registries().item().getId(stack.getItem());
			field_29183.unlockRecipes(DessicantData.discoveries.get(id).stream()
				.map(field_29183.world.getRecipeManager()::get)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toList()));
		}
	}

}
