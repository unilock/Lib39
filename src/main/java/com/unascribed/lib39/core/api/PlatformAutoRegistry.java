package com.unascribed.lib39.core.api;

import net.minecraft.registry.Registry;

class PlatformAutoRegistry extends BaseAutoRegistry {
	
	PlatformAutoRegistry(String namespace) {
		super(namespace);
	}

	/**
	 * Scan a class {@code holdingClass} for static final fields of type {@code type}, and register them
	 * in the configured namespace with a path equal to the field's name as lower case in the given
	 * registry.
	 */
	public <T> void autoRegister(Registry<T> registry, Class<?> holdingClass, Class<? super T> type) {
		throw new AbstractMethodError();
	}
	
}
