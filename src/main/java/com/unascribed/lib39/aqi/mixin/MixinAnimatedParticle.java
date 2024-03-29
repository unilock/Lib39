package com.unascribed.lib39.aqi.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.AnimatedParticle;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.world.ClientWorld;

@Environment(EnvType.CLIENT)
@Mixin(AnimatedParticle.class)
public abstract class MixinAnimatedParticle extends SpriteBillboardParticle {

	protected MixinAnimatedParticle(ClientWorld clientWorld, double d, double e, double f) {
		super(clientWorld, d, e, f);
	}

	@Inject(at=@At("RETURN"), method="getType", cancellable=true)
	public void getType(CallbackInfoReturnable<ParticleTextureSheet> ci) {
		if (ci.getReturnValue() == ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT && colorAlpha == 0.99f) {
			// firework particle
			ci.setReturnValue(ParticleTextureSheet.PARTICLE_SHEET_OPAQUE);
		}
	}
	
}
