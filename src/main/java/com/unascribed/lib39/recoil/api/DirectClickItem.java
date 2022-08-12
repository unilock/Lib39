package com.unascribed.lib39.recoil.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public interface DirectClickItem {

	/**
	 * Called when a player presses the "attack" key while holding this item. (Left-click by default)
	 * <p>
	 * Returning a successful ActionResult <i>on the client</i> will suppress all vanilla
	 * functionality and cause this to be called again on the server. Generally, as such, this
	 * method should start with {@code if (player.world.isClient) return ActionResult.SUCCESS;},
	 * but you can conditionally suppress vanilla logic should you desire.
	 * <p>
	 * If this returns {@code SUCCESS} on the server, the player's hand will be swung.
	 * <p>
	 * You probably also want to override {@link Item#canMine} to always return {@code false}.
	 * 
	 * @param player the player that attacked with this item
	 * @param hand the hand (currently, always MAIN_HAND, but this may change)
	 * @return your reaction to this attack
	 */
	ActionResult onDirectAttack(PlayerEntity player, Hand hand);
	/**
	 * Called when a player presses the "use" key while holding this item. (Right-click by default)
	 * <p>
	 * Returning a successful ActionResult <i>on the client</i> will suppress all vanilla
	 * functionality and cause this to be called again on the server. Generally, as such, this
	 * method should start with {@code if (player.world.isClient) return ActionResult.SUCCESS;},
	 * but you can conditionally suppress vanilla logic should you desire.
	 * <p>
	 * If this returns {@code SUCCESS} on the server, the player's hand will be swung.
	 * 
	 * @param player the player that used this item
	 * @param hand the hand (currently, always MAIN_HAND, but this may change)
	 * @return your reaction to this use
	 */
	ActionResult onDirectUse(PlayerEntity player, Hand hand);
	
}
