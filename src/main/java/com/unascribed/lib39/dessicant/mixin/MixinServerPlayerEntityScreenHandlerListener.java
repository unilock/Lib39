package com.unascribed.lib39.dessicant.mixin;

import java.util.Optional;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
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

@Mixin(targets={
		"net/minecraft/class_3222$2",
		"net/minecraft/unmapped/C_mxrobsgg$C_wdomexrk",
		"net/minecraft/server/network/ServerPlayerEntity$2"
})
@Pseudo
public abstract class MixinServerPlayerEntityScreenHandlerListener {

	private ServerPlayerEntity lib39$player;

	@Inject(at=@At("RETURN"), method="<init>", remap=false)
	public void construct(ServerPlayerEntity player, CallbackInfo ci) {
		lib39$player = player;
	}
	
	@Inject(at=@At("HEAD"), method={"onSlotUpdate", "method_7635"}, remap=false)
	public void lib39Dessicant$onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack, CallbackInfo ci) {
		if (!(handler.getSlot(slotId) instanceof CraftingResultSlot) && handler == lib39$player.playerScreenHandler) {
			var r = P39.registries();
			Identifier id = r.getId(r.item(), stack.getItem());
			lib39$player.unlockRecipes(DessicantData.discoveries.get(id).stream()
				.map(lib39$player.world.getRecipeManager()::get)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toList()));
		}
	}

}
