package com.unascribed.lib39.mesh.api;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class BlockNetworkNode {
	private final BlockPos pos;
	private final BlockNetworkNodeType type;
	
	public BlockNetworkNode(BlockPos pos, BlockNetworkNodeType type) {
		this.pos = pos.toImmutable();
		this.type = type;
	}
	
	public BlockPos getPos() {
		return pos;
	}
	public BlockNetworkNodeType getType() {
		return type;
	}
	
	public void serializeNbt(NbtCompound nbt) {
		
	}
	
	@Override
	public String toString() {
		return type.name()+"@"+pos.getX()+","+pos.getY()+","+pos.getZ();
	}
}