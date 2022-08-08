package com.unascribed.lib39.waypoint.api;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.Direction;

public interface HaloBlockEntity {

	/**
	 * Determine whether or not the halo should currently be rendered for this block. You can check
	 * a LIT blockstate here.
	 * <p>
	 * <b>Called during baking</b>. To get this to be re-called, the return value of {@link #getStateObject()}
	 * needs to change.
	 * 
	 * @return {@code true} if the halo for this block should be rendered
	 */
	default boolean shouldRenderHalo() { return true; }
	/**
	 * Allows colorizing your halo model, if it's not already colored or your block is dyeable.
	 * <p>
	 * <b>Called during baking</b>. To get this to be re-called, the return value of {@link #getStateObject()}
	 * needs to change.
	 * 
	 * @return the packed RGB color to use for the halo of this block
	 */
	default int getGlowColor() { return -1; }
	/**
	 * Allows rotating the halo. Assumes that your model points up.
	 * <p>
	 * <b>Called during baking</b>. To get this to be re-called, the return value of {@link #getStateObject()}
	 * needs to change.
	 * 
	 * @return the direction this block is facing, or {@code null} to opt out of rotation
	 */
	default @Nullable Direction getFacing() { return null; }
	/**
	 * Return an arbitrary "state object", used by the renderer to determine when your block needs
	 * to be re-rendered and re-baked. Usually you just want to return {@link BlockEntity#getCachedState()}.
	 * 
	 * @return an arbitrary state object for determining rebakes
	 */
	Object getStateObject();
	
}
