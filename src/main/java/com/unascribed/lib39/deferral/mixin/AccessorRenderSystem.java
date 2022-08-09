package com.unascribed.lib39.deferral.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
@Mixin(RenderSystem.class)
public interface AccessorRenderSystem {

	@Accessor("shaderLightDirections")
	static Vec3f[] lib39Deferral$getShaderLightDirections() { throw new AbstractMethodError(); }
	
}
