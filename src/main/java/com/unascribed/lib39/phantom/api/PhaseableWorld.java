package com.unascribed.lib39.phantom.api;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;

public interface PhaseableWorld {

	/**
	 * "Phase" this block out of the world for the given number of ticks.
	 * <p>
	 * While a block is phased, it does not collide and effectively does not exist. However, the
	 * phased blocks never actually leave the world, and phase states are not saved when chunks
	 * unload. This makes the phasing system quite safe to use, even on block entities.
	 * <p>
	 * <b>Note</b>: Phantom does not handle syncing block phasing for you. You will need to use your
	 * own packet, or call this method on both sides in an item use handler, etc.
	 * 
	 * @param pos the block to phase
	 * @param ticks the amount of ticks to phase it for
	 */
	default void phaseBlock(BlockPos pos, int ticks) { phaseBlock(pos, ticks, 0, null); }
	
	/**
	 * "Phase" this block out of the world for the given number of ticks, after the given delay in
	 * ticks has passed.
	 * <p>
	 * While a block is phased, it does not collide and effectively does not exist. However, the
	 * phased blocks never actually leave the world, and phase states are not saved when chunks
	 * unload. This makes the phasing system quite safe to use, even on block entities.
	 * <p>
	 * <b>Note</b>: Phantom does not handle syncing block phasing for you. You will need to use your
	 * own packet, or call this method on both sides in an item use handler, etc.
	 * 
	 * @param pos the block to phase
	 * @param ticks the amount of ticks to phase it for
	 * @param delay how long to wait until phasing the block
	 */
	default void phaseBlock(BlockPos pos, int ticks, int delay) { phaseBlock(pos, ticks, 0, null); }
	
	/**
	 * "Phase" this block out of the world for the given number of ticks, after the given delay in
	 * ticks has passed, and use the given DamageSource instead of vanilla's default FALL damage
	 * source if an entity fell through the blocks.
	 * <p>
	 * While a block is phased, it does not collide and effectively does not exist. However, the
	 * phased blocks never actually leave the world, and phase states are not saved when chunks
	 * unload. This makes the phasing system quite safe to use, even on block entities.
	 * <p>
	 * <b>Note</b>: Phantom does not handle syncing block phasing for you. You will need to use your
	 * own packet, or call this method on both sides in an item use handler, etc.
	 * 
	 * @param pos the block to phase
	 * @param ticks the amount of ticks to phase it for
	 * @param delay how long to wait until phasing the block
	 * @param customSrc the DamageSource to use
	 */
	void phaseBlock(BlockPos pos, int ticks, int delay, @Nullable DamageSource customSrc);
	
	/**
	 * Temporarily disable the phasing system. Useful for rendering code.
	 */
	void unmaskPhasedBlocks();
	
	/**
	 * Re-enable the phasing system. Should be called in a finally.
	 */
	void remaskPhasedBlocks();
	
	/**
	 * @return {@code true} if the given block is currently phased
	 */
	boolean isPhased(BlockPos pos);
	
}
