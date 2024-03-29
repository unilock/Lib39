package com.unascribed.lib39.lockpick.mixin;

import java.lang.reflect.Type;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.unascribed.lib39.lockpick.quack.InheritElements;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.json.JsonUnbakedModel;

@Environment(EnvType.CLIENT)
@Mixin(JsonUnbakedModel.Deserializer.class)
public class MixinJsonUnbakedModelDeserializer {

	@Inject(at=@At("RETURN"), method="deserialize", cancellable=true)
	public void lib39Lockpick$deserialize(JsonElement ele, Type type, JsonDeserializationContext ctx, CallbackInfoReturnable<JsonUnbakedModel> ci) throws JsonParseException {
		if (ele instanceof JsonObject && ele.getAsJsonObject().has("lib39:inherit_elements") && ele.getAsJsonObject().get("lib39:inherit_elements").getAsBoolean()) {
			((InheritElements)ci.getReturnValue()).lib39Lockpick$setInheritElements(true);
		}
	}
	
}
