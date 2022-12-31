package com.unascribed.lib39.core.api.util;

import net.minecraft.registry.Holder;

/**
 * A LatchReference and a Holder in one.
 */
public class LatchHolder<T> extends AbstractLatchReference<Holder<T>> {
	
	// Holder has changed a lot between versions; this class get overwritten with a version-specific one
	// Check the platforms for that

	protected LatchHolder() {
		throw new AbstractMethodError();
	}
	
	/**
	 * @return an unset latch
	 */
	public static <T> LatchHolder<T> unset() {
		return new LatchHolder<>();
	}
	
	/**
	 * @return a set empty latch
	 */
	public static <T> LatchHolder<T> empty() {
		LatchHolder<T> lr = new LatchHolder<>();
		lr.setEmpty();
		return lr;
	}
	
	/**
	 * @return a set latch with the given value
	 */
	public static <T> LatchHolder<T> of(Holder<T> t) {
		LatchHolder<T> lr = new LatchHolder<>();
		lr.set(t);
		return lr;
	}

}
