package com.unascribed.lib39.dessicant.mixin;

import java.io.InputStream;
import java.util.function.BiConsumer;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.gson.JsonObject;
import com.unascribed.lib39.dessicant.EnhancedLangLoader;

import net.minecraft.util.Language;

@Mixin(Language.class)
public class MixinLanguage {

	@Inject(at=@At(value="INVOKE", target="com/google/gson/JsonObject.entrySet()Ljava/util/Set;"),
			method="load(Ljava/io/InputStream;Ljava/util/function/BiConsumer;)V", cancellable=true)
	private static void load(InputStream inputStream, BiConsumer<String, String> entryConsumer, CallbackInfo ci,
			@Local JsonObject obj) {
		if (obj.has("lib39:enable_enhanced_lang")) {
			EnhancedLangLoader.load("", obj, entryConsumer);
			ci.cancel();
		}
	}
	
}
