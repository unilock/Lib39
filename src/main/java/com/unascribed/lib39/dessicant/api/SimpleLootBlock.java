package com.unascribed.lib39.dessicant.api;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;

public interface SimpleLootBlock {

	ItemStack getLoot(BlockState state);
	
}
