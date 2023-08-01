package com.unascribed.lib39.keygen.mixin;

import java.util.LinkedHashMap;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.unascribed.lib39.keygen.Lib39Keygen;

import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

@Mixin(targets="net/minecraft/client/sound/SoundManager$SoundList")
public class MixinSoundList {

	@Shadow
	private Map<Identifier, Resource> soundCache;
	
	@Inject(at=@At("RETURN"), method="cacheSounds")
	private void lib39Keygen$cacheSounds(ResourceManager manager, CallbackInfo ci) {
		this.soundCache = new LinkedHashMap<>(soundCache);
		this.soundCache.putAll(manager.findResources("sounds", Lib39Keygen::isModuleFile));
	}
	
}
