package com.unascribed.lib39.crowbar.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.unascribed.lib39.core.mixinsupport.AutoMixinEligible;
import com.unascribed.lib39.crowbar.api.WorldGenerationEvents;
import com.unascribed.lib39.crowbar.api.WorldGenerationEvents.GeneratorContext;

import net.minecraft.structure.StructureManager;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;

@Mixin(NoiseChunkGenerator.class)
@AutoMixinEligible(ifConfigSet="platform 1.18.2")
public abstract class MixinNoiseChunkGeneratorEighteen {

	@Inject(at=@At("TAIL"), method={"buildSurface", "method_12110"}, remap=false)
	public void lib39Crowbar$buildSurface(ChunkRegion region, StructureManager structureManager, Chunk chunk, CallbackInfo ci) {
		WorldGenerationEvents.AFTER_BUILD_SURFACE.invoker().buildSurface(new GeneratorContext(region, structureManager, chunk));
	}
	
}
