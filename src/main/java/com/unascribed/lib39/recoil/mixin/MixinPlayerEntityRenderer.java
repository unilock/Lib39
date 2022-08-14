package com.unascribed.lib39.recoil.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.unascribed.lib39.recoil.api.DefaultPoseItem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel.ArmPose;
import net.minecraft.util.Hand;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public class MixinPlayerEntityRenderer {
	
	@Inject(at=@At("HEAD"), method="getArmPose", cancellable=true)
	private static void lib39Recoil$getArmPose(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<ArmPose> ci) {
		if (player.getStackInHand(hand).getItem() instanceof DefaultPoseItem dpi) {
			ci.setReturnValue(dpi.getDefaultPose(player, hand));
		}
	}
	
}
