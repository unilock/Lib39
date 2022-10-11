package com.unascribed.lib39.dessicant.mixin;

import java.io.InputStream;
import java.util.function.BiConsumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.google.gson.JsonObject;
import com.unascribed.lib39.dessicant.EnhancedLangLoader;

import net.minecraft.util.Language;

@Mixin(Language.class)
public class MixinLanguage {

	@Inject(at=@At(value="INVOKE", target="com/google/gson/JsonObject.entrySet()Ljava/util/Set;"),
			method="load", cancellable=true, locals=LocalCapture.CAPTURE_FAILHARD)
	private static void load(InputStream inputStream, BiConsumer<String, String> entryConsumer, CallbackInfo ci,
			JsonObject obj) {
		if (obj.has("lib39:enable_enhanced_lang")) {
			EnhancedLangLoader.load("", obj, entryConsumer);
			ci.cancel();
		}
	}
	
}
