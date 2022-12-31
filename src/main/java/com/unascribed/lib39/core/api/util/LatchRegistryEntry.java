package com.unascribed.lib39.core.api.util;

import net.minecraft.registry.Holder;

/**
 * Yarn-named version of {@link LatchHolder}.
 */
public class LatchRegistryEntry<T> extends LatchHolder<T> {
	
	/**
	 * @return an unset latch
	 */
	public static <T> LatchRegistryEntry<T> unset() {
		return new LatchRegistryEntry<>();
	}
	
	/**
	 * @return a set empty latch
	 */
	public static <T> LatchRegistryEntry<T> empty() {
		LatchRegistryEntry<T> lr = new LatchRegistryEntry<>();
		lr.setEmpty();
		return lr;
	}
	
	/**
	 * @return a set latch with the given value
	 */
	public static <T> LatchRegistryEntry<T> of(Holder<T> t) {
		LatchRegistryEntry<T> lr = new LatchRegistryEntry<>();
		lr.set(t);
		return lr;
	}
}
