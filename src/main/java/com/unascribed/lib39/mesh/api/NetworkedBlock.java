package com.unascribed.lib39.mesh.api;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Demo implementation of a Block that participates in a BlockNetwork. If you need to extend
 * something other than Block, you can copy the method implementations in this class.
 */
public abstract class NetworkedBlock<N extends BlockNetworkNode, T extends BlockNetworkNodeType> extends Block {

	protected final BlockNetworkType<N, T> type;
	
	public NetworkedBlock(BlockNetworkType<N, T> type, Settings settings) {
		super(settings);
		this.type = type;
	}
	
	protected abstract T getNodeType(BlockState state, World world, BlockPos pos);

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.isOf(newState.getBlock())) {
			if (world instanceof ServerWorld sw) {
				BlockNetworkManager.get(sw).destroy(type, pos);
			}
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}
	
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		super.onBlockAdded(state, world, pos, oldState, notify);
		if (!state.isOf(oldState.getBlock())) {
			if (world instanceof ServerWorld sw) {
				BlockNetworkManager.get(sw).introduce(type, pos, getNodeType(state, world, pos));
			}
		}
	}

}
