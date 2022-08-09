package com.unascribed.lib39.machination.quack;

import com.google.common.collect.Multimap;
import com.google.common.collect.Table;

import net.minecraft.entity.ItemEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;

public interface WetWorld {

	Multimap<BlockPos, ItemEntity> lib39Machination$getSoakingMap();
	Table<BlockPos, Fluid, Integer> lib39Machination$getTimeTable();

}
