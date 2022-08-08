package com.unascribed.lib39.keygen.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.Sound;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
@Mixin(Sound.class)
public class MixinSound {

	@Shadow @Final
	private Identifier id;
	
	@Inject(at=@At("HEAD"), method="getLocation", cancellable=true)
	public void lib39Keygen$getLocation(CallbackInfoReturnable<Identifier> ci) {
		if (id.getPath().endsWith(".xm") || id.getPath().endsWith(".s3m") || id.getPath().endsWith(".mod") ||
				id.getPath().endsWith(".xm.bz2") || id.getPath().endsWith(".s3m.bz2") || id.getPath().endsWith(".mod.bz2")) {
			ci.setReturnValue(new Identifier(id.getNamespace(), "sounds/"+id.getPath()));
		}
	}
	
}
