package com.unascribed.lib39.recoil.api;

import net.minecraft.client.render.entity.model.BipedEntityModel.ArmPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;

public interface DefaultPoseItem {

	/**
	 * Modify the default arm pose for a living entity that is holding this item.
	 * @param entity the entity holding the item
	 * @param stack the itemstack
	 * @param hand the hand the item is in
	 * @return the pose to use, such as {@code BOW_AND_ARROW}
	 */
	ArmPose getDefaultPose(LivingEntity entity, Hand hand);
	
}
