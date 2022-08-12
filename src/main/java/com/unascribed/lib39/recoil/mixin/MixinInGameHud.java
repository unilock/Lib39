package com.unascribed.lib39.recoil.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.unascribed.lib39.recoil.api.RecoilEvents;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class MixinInGameHud {

	@Inject(at=@At("HEAD"), method="renderCrosshair", cancellable=true)
	private void renderCrosshair(MatrixStack matrices, CallbackInfo ci) {
		if (RecoilEvents.RENDER_CROSSHAIRS.invoker().onRenderCrosshairs(matrices)) {
			ci.cancel();
		}
	}
	
}
