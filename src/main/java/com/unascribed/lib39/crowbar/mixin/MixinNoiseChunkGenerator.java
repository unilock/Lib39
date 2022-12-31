package com.unascribed.lib39.crowbar.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.unascribed.lib39.crowbar.api.WorldGenerationEvents;
import com.unascribed.lib39.crowbar.api.WorldGenerationEvents.GeneratorContext;

import net.minecraft.structure.StructureManager;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;

@Mixin(NoiseChunkGenerator.class)
public abstract class MixinNoiseChunkGenerator {
	
	@Inject(at=@At("TAIL"), method="method_12110")
	public void lib39Crowbar$buildSurface(ChunkRegion region, StructureManager structureManager, @Coerce Object randomState, Chunk chunk, CallbackInfo ci) {
		WorldGenerationEvents.AFTER_BUILD_SURFACE.invoker().buildSurface(new GeneratorContext(region, structureManager, chunk));
	}
	
}
