package com.unascribed.lib39.ripple.mixin;

import java.util.List;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.unascribed.lib39.ripple.impl.SplashTextHandler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.SplashTextResourceSupplier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.profiler.Profiler;

@Environment(EnvType.CLIENT)
@Mixin(value=SplashTextResourceSupplier.class, priority=80000)
public class MixinSplashTextResourceSupplier {
	
	@Shadow @Final
	private List<String> splashTexts;

	@Inject(at=@At("RETURN"), method="apply")
	protected void lib39Core$apply(List<String> li, ResourceManager mgr, Profiler prof, CallbackInfo ci) {
		SplashTextHandler.modifyNormalSplashes(splashTexts);
	}
	
	@Inject(at=@At("HEAD"), method="get", cancellable=true)
	public void lib39Core$get(CallbackInfoReturnable<String> ci) {
		String initial = ci.getReturnValue();
		String replaced = SplashTextHandler.replaceSplash(splashTexts.size(), initial);
		if (replaced != initial) {
			ci.setReturnValue(replaced);
		}
	}
	
}
