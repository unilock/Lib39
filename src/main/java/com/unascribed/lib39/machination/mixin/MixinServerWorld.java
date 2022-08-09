package com.unascribed.lib39.machination.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.unascribed.lib39.machination.quack.WetWorld;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;

import net.minecraft.entity.ItemEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@Mixin(ServerWorld.class)
public class MixinServerWorld implements WetWorld {

	private final Multimap<BlockPos, ItemEntity> lib39Machination$soakingMap = HashMultimap.create();
	private final Table<BlockPos, Fluid, Integer> lib39Machination$timeTable = HashBasedTable.create();
	
	@Override
	public Multimap<BlockPos, ItemEntity> lib39Machination$getSoakingMap() {
		return lib39Machination$soakingMap;
	}
	
	@Override
	public Table<BlockPos, Fluid, Integer> lib39Machination$getTimeTable() {
		return lib39Machination$timeTable;
	}
	
}
