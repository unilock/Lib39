package com.unascribed.lib39.phantom.quack;

import javax.annotation.Nullable;

import com.unascribed.lib39.phantom.api.PhaseableWorld;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public interface PhantomWorld extends PhaseableWorld {

	boolean lib39Phantom$isPhased(int x, int y, int z);
	boolean lib39Phantom$isPhased(BlockPos pos);
	boolean lib39Phantom$isPhased(ChunkPos chunkPos, BlockPos pos);
	@Nullable DamageSource lib39Phantom$getDamageSource(BlockPos pos);
	void lib39Phantom$addPhaseBlock(BlockPos pos, int lifetime, int delay, @Nullable DamageSource customSrc);
	void lib39Phantom$removePhaseBlock(BlockPos pos);
	
	void lib39Phantom$scheduleRenderUpdate(BlockPos pos);
	
	void lib39Phantom$setUnmask(boolean unmask);
	
	@Override
	default void phaseBlock(BlockPos pos, int ticks, int delay, @Nullable DamageSource customSrc) {
		lib39Phantom$addPhaseBlock(pos, ticks, delay, customSrc);
	}
	
	@Override
	default void unmaskPhasedBlocks() {
		lib39Phantom$setUnmask(true);
	}
	
	@Override
	default void remaskPhasedBlocks() {
		lib39Phantom$setUnmask(false);
	}
	
	@Override
	default boolean isPhased(BlockPos pos) {
		return lib39Phantom$isPhased(pos);
	}
	
}
