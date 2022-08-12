package com.unascribed.lib39.recoil.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.unascribed.lib39.recoil.api.RecoilEvents;
import com.unascribed.lib39.recoil.api.Vec1f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;

@Environment(EnvType.CLIENT)
@Mixin(Entity.class)
public class MixinEntity {

	@Redirect(at=@At(value="FIELD", target="net/minecraft/entity/Entity.renderDistanceMultiplier:D"), method="shouldRender(D)Z", require=0)
	private double getRenderDistanceMultiplier() {
		return Entity.getRenderDistanceMultiplier();
	}
	
	@Inject(at=@At("RETURN"), method="getRenderDistanceMultiplier", cancellable=true)
	private static void getRenderDistanceMultiplier(CallbackInfoReturnable<Double> ci) {
		Vec1f val = new Vec1f(1);
		RecoilEvents.UPDATE_ENTITY_RENDER_DISTANCE.invoker().onUpdateEntityRenderDistance(val);
		if (val.get() != 1) {
			ci.setReturnValue(ci.getReturnValueD()*val.get());
		}
	}
	
}
