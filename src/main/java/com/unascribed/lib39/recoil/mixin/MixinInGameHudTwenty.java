package com.unascribed.lib39.recoil.mixin;

import com.unascribed.lib39.core.mixinsupport.AutoMixinEligible;
import com.unascribed.lib39.recoil.api.RecoilEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
@AutoMixinEligible(ifConfigSet="platform 1.20")
public class MixinInGameHudTwenty {

	@Inject(at=@At("HEAD"), method="method_1736", cancellable=true)
	private void lib39recoil$post120$renderCrosshair(GuiGraphics state, CallbackInfo ci) {
		if (RecoilEvents.RENDER_CROSSHAIRS.invoker().onRenderCrosshairs(state.method_51448())) {
			ci.cancel();
		}
	}
	
}
