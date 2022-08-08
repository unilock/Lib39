package com.unascribed.lib39.crowbar.api;

import java.util.function.BiConsumer;

import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;

public interface SurfaceRuleModifier {

	/**
	 * Called when the game is building the surface rules for what goes in the "dirt" layer in a
	 * normal biome (like plains).
	 * <p>
	 * These rules are used for all unrecognized biomes, so you can inject here to change the
	 * surface in a custom biome. Pass the ID of your biome and the blockstate you want to replace
	 * dirt into the given callback.
	 */
	void modifyDirtSurfaceRules(BiConsumer<Identifier, BlockState> out);
	/**
	 * Called when the game is building the surface rules for what goes in the "grass" layer in a
	 * normal biome (like plains).
	 * <p>
	 * These rules are used for all unrecognized biomes, so you can inject here to change the
	 * surface in a custom biome. Pass the ID of your biome and the blockstate you want to replace
	 * grass into the given callback.
	 */
	void modifyGrassSurfaceRules(BiConsumer<Identifier, BlockState> out);
	
}
