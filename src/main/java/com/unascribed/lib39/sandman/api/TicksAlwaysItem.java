package com.unascribed.lib39.sandman.api;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface TicksAlwaysItem {
	/**
	 * Called when this item ticks inside of a block's inventory.
	 * @param stack the stack being ticked
	 * @param world the world the block is within
	 * @param pos the position of the block
	 * @param slot the slot of the block's inventory this item is in
	 */
	void blockInventoryTick(ItemStack stack, World world, BlockPos pos, int slot);
	/**
	 * Called when this item ticks inside of an entity's inventory.
	 * @param stack the stack being ticked
	 * @param world the world the entity is within
	 * @param entity the entity being ticked
	 * @param slot the slot of the entity's inventory the item is in
	 * @param selected {@code true} if the item is currently held in one of the entity's hands
	 */
	void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected);
}
