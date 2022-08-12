package com.unascribed.lib39.crowbar.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.chunk.Chunk;

public final class WorldGenerationEvents {

	public record GeneratorContext(ChunkRegion region, StructureManager structureManager, Chunk chunk) {}
	
	/**
	 * Called when vanilla surface building has completed.
	 */
	public static final Event<BuildSurface> AFTER_BUILD_SURFACE = EventFactory.createArrayBacked(BuildSurface.class, callbacks -> ctx -> {
		if (EventFactory.isProfilingEnabled()) {
			Profiler profiler = ctx.region.toServerWorld().getProfiler();
			profiler.push("lib39-crowbar:build_surface");

			for (BuildSurface event : callbacks) {
				profiler.push(EventFactory.getHandlerName(event));
				event.buildSurface(ctx);
				profiler.pop();
			}

			profiler.pop();
		} else {
			for (BuildSurface event : callbacks) {
				event.buildSurface(ctx);
			}
		}
	});
	
	/**
	 * Called when vanilla feature population has completed.
	 */
	public static final Event<BuildSurface> AFTER_GENERATE_FEATURES = EventFactory.createArrayBacked(BuildSurface.class, callbacks -> ctx -> {
		if (EventFactory.isProfilingEnabled()) {
			Profiler profiler = ctx.region.toServerWorld().getProfiler();
			profiler.push("lib39-crowbar:generate_features");

			for (BuildSurface event : callbacks) {
				profiler.push(EventFactory.getHandlerName(event));
				event.buildSurface(ctx);
				profiler.pop();
			}

			profiler.pop();
		} else {
			for (BuildSurface event : callbacks) {
				event.buildSurface(ctx);
			}
		}
	});

	@FunctionalInterface
	public interface BuildSurface {
		void buildSurface(GeneratorContext ctx);
	}
	
	private WorldGenerationEvents() {}
	
}
