package com.unascribed.lib39.gesundheit.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;

@Environment(EnvType.CLIENT)
@Mixin(Particle.class)
public interface AccessorParticle {

	@Accessor("collidesWithWorld")
	void lib39Gesundheit$setCollidesWithWorld(boolean collidesWithWorld);
	
	@Accessor("x")
	double lib39Gesundheit$getX();
	@Accessor("y")
	double lib39Gesundheit$getY();
	@Accessor("z")
	double lib39Gesundheit$getZ();
	
}
