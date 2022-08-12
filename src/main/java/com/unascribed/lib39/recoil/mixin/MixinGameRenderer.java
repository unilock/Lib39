package com.unascribed.lib39.recoil.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.unascribed.lib39.recoil.api.RecoilEvents;
import com.unascribed.lib39.recoil.api.Vec1f;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public class MixinGameRenderer {

	@Inject(at=@At("RETURN"), method="getFov", cancellable=true)
	private void getFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> ci) {
		float orig = ci.getReturnValueF();
		Vec1f val = new Vec1f(orig);
		RecoilEvents.UPDATE_FOV.invoker().onUpdateFOV(val, tickDelta);
		if (val.get() != orig) {
			ci.setReturnValue((double)val.get());
		}
	}
	
}
