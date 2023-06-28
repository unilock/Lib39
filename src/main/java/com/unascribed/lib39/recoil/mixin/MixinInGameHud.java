package com.unascribed.lib39.recoil.mixin;

import com.unascribed.lib39.core.mixinsupport.AutoMixinEligible;
import net.minecraft.client.gui.GuiGraphics;
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
@AutoMixinEligible(unlessConfigSet="platform 1.20")
public class MixinInGameHud {

	@Inject(at=@At("HEAD"), method="method_1736", cancellable=true)
	private void lib39recoil$pre120$renderCrosshair(MatrixStack matrices, CallbackInfo ci) {
		if (RecoilEvents.RENDER_CROSSHAIRS.invoker().onRenderCrosshairs(matrices)) {
			ci.cancel();
		}
	}
	
}
