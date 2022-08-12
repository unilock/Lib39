package com.unascribed.lib39.recoil.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.unascribed.lib39.recoil.api.DirectClickItem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {

	@Shadow
	public ClientPlayerEntity player;
	
	@Inject(at=@At("HEAD"), method="doAttack", cancellable=true)
	private void doAttack(CallbackInfoReturnable<Boolean> ci) {
		if (player != null && player.getMainHandStack().getItem() instanceof DirectClickItem dci) {
			if (!player.getItemCooldownManager().isCoolingDown(player.getMainHandStack().getItem())) {
				if (dci.onDirectAttack(player, Hand.MAIN_HAND).isAccepted()) {
					ClientPlayNetworking.send(new Identifier("lib39-recoil", "direct_attack"), PacketByteBufs.empty());
				}
			}
			ci.setReturnValue(false);
		}
	}
	
	@Inject(at=@At("HEAD"), method="doItemUse", cancellable=true)
	private void doItemUse(CallbackInfo ci) {
		if (player != null && player.getMainHandStack().getItem() instanceof DirectClickItem dci) {
			if (!player.getItemCooldownManager().isCoolingDown(player.getMainHandStack().getItem())) {
				if (dci.onDirectUse(player, Hand.MAIN_HAND).isAccepted()) {
					ClientPlayNetworking.send(new Identifier("lib39-recoil", "direct_use"), PacketByteBufs.empty());
				}
			}
			ci.cancel();
		}
	}
}
