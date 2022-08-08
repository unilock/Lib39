package com.unascribed.lib39.phantom;

import java.util.concurrent.atomic.AtomicInteger;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.damage.DamageSource;

public class PhaseQueueEntry {

	public final int lifetime;
	public final AtomicInteger delayLeft;
	@Nullable
	public final DamageSource customSrc;
	
	public PhaseQueueEntry(int lifetime, int delayLeft, DamageSource customSrc) {
		this.lifetime = lifetime;
		this.delayLeft = new AtomicInteger(delayLeft);
		this.customSrc = customSrc;
	}
	
}
