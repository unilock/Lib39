package com.unascribed.lib39.gesundheit.mixin;

import java.util.Map;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.unascribed.lib39.gesundheit.GuiParticleManager;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
@Mixin(value=ParticleManager.class, priority=9000)
public class MixinParticleManager {

	@Shadow @Final
	private Int2ObjectMap<ParticleFactory<?>> factories;
	@Shadow @Final
	private Map<Identifier, Object> spriteAwareFactories;

	@Inject(at=@At("HEAD"), method="registerDefaultFactories", cancellable=true)
	public void lib39Gesundheit$registerDefaultFactories(CallbackInfo ci) {
		Object self = this;
		if (self instanceof GuiParticleManager) {
			var pm = MinecraftClient.getInstance().particleManager;
			factories.putAll(((AccessorParticleManager)pm).lib39Gesundheit$getFactories());
			spriteAwareFactories.putAll(((AccessorParticleManager)pm).lib39Gesundheit$getSpriteAwareFactories());
			ci.cancel();
		}
	}
	
}
