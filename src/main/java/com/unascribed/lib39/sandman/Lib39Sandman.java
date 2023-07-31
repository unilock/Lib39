package com.unascribed.lib39.sandman;

import java.lang.invoke.MethodHandles;
import java.util.Objects;
import java.util.function.Function;

import com.unascribed.lib39.core.api.util.ReflectionHelper;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedChunkManager;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.WorldChunk;

public class Lib39Sandman implements ModInitializer {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public
	static final Function<ThreadedChunkManager, Long2ObjectLinkedOpenHashMap<ChunkHolder>> chunkHolders =
			(Function)ReflectionHelper.of(MethodHandles.lookup(), ThreadedChunkManager.class)
				.obtainGetter(Long2ObjectLinkedOpenHashMap.class, "chunkHolders", "field_17220");

	@Override
	public void onInitialize() {
		ServerTickEvents.START_WORLD_TICK.register(TickAlwaysItemHandler::startServerWorldTick);
	}

	public static Iterable<WorldChunk> getLoadedChunks(ServerWorld world) {
		return () -> chunkHolders.apply(world.getChunkManager().delegate)
				.values().stream()
				.filter(Objects::nonNull)
				.filter(hc -> hc.getCurrentStatus() != null && hc.getCurrentStatus().isAtLeast(ChunkStatus.FULL))
				.map(hc -> (WorldChunk)world.getChunk(hc.getPos().x, hc.getPos().z, ChunkStatus.FULL, false))
				.filter(Objects::nonNull)
				.iterator();
	}
	
}
