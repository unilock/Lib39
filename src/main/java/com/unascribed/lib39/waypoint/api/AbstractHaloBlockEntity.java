package com.unascribed.lib39.waypoint.api;

import com.unascribed.lib39.waypoint.HaloRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractHaloBlockEntity extends BlockEntity implements HaloBlockEntity {

	private boolean clientCreated = false;

	public AbstractHaloBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	
	@Environment(EnvType.CLIENT)
	public void clientTick() {
		if (!clientCreated) {
			HaloRenderer.notifyCreated(this);
			clientCreated = true;
		}
	}
	
	@Override
	public Object getStateObject() {
		return getCachedState();
	}
	
	public static void tick(World world, BlockPos bp, BlockState state, BlockEntity be) {
		if (world.isClient && be instanceof AbstractHaloBlockEntity yt) {
			yt.clientTick();
		}
	}

}
