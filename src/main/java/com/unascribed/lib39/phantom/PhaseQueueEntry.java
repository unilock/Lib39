package com.unascribed.lib39.phantom;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.damage.DamageSource;

public class PhaseQueueEntry {

	public final int lifetime;
	public int delayLeft;
	@Nullable
	public final DamageSource customSrc;
	
	public PhaseQueueEntry(int lifetime, int delayLeft, DamageSource customSrc) {
		this.lifetime = lifetime;
		this.delayLeft = delayLeft;
		this.customSrc = customSrc;
	}
	
}
