package com.unascribed.lib39.lockpick.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.gson.JsonObject;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.JsonHelper;

@Environment(EnvType.CLIENT)
@Mixin(targets="net/minecraft/client/render/model/json/ModelElement$Deserializer")
public class MixinModelElementDeserializer {

	@Inject(at=@At("HEAD"), method="deserializeRotationAngle", cancellable=true)
	private void lib39Lockpick$deserializeRotationAngle(JsonObject object, CallbackInfoReturnable<Float> ci) {
		if (object.has("lib39:unlock_angle") && object.get("lib39:unlock_angle").getAsBoolean()) {
			ci.setReturnValue(JsonHelper.getFloat(object, "angle"));
		}
	}
	
}
