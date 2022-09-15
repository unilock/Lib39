package com.unascribed.lib39.mesh.api;

import java.util.UUID;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public interface BlockNetworkType<N extends BlockNetworkNode, T extends BlockNetworkNodeType> {

	/**
	 * Called when a new BlockNetwork instance needs to be constructed. You can store custom data
	 * in subclasses.
	 */
	default BlockNetwork<N, T> construct(BlockNetworkManager owner, UUID id) {
		return new BlockNetwork<>(owner, this, id);
	}
	
	/**
	 * Called when a Node is being deserialized by a network on world load. Generally this should
	 * delegate to a constructor of your Node subclass.
	 */
	N deserializeNode(BlockPos pos, T type, NbtCompound nbt);
	/**
	 * Called when a new Node is being created for this network because a block has been placed.
	 * Generally this should delegate to a constructor of your Node subclass.
	 */
	N createNode(BlockPos pos, T type);
	
	/**
	 * Called every tick. Note that the blocks in your network may not be loaded; if you interact
	 * with the world, make sure to check isChunkLoaded first.
	 */
	default void tick(BlockNetwork<N, T> network) {}
	/**
	 * Called when a network changes, due to a node being added or removed.
	 */
	default void update(BlockNetwork<N, T> network) {}
	
	/**
	 * Return an array of every valid NodeType for this network type.
	 */
	T[] getNodeTypes();

}
