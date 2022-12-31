package com.unascribed.lib39.core.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraft.util.Identifier;

/**
 * Utilities for automatically discovering and registering things in classes.
 */
// this class is split up in a weird way to compensate for 1.19.3's refactors without copy-pasting it everywhere
public final class AutoRegistry extends PlatformAutoRegistry {

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface RegisteredAs {
		String value();
	}

	/**
	 * Construct an AutoRegistry for the given namespace.
	 */
	public static AutoRegistry of(String namespace) {
		return new AutoRegistry(namespace);
	}
	
	private AutoRegistry(String namespace) {
		super(namespace);
	}

	/**
	 * Scan a class {@code holdingClass} for static final fields of type {@code type}, and register them
	 * in the given ad-hoc registry.
	 */
	public <T> void autoRegister(Consumer<T> adhocRegistry, Class<?> holdingClass, Class<T> type) {
		eachRegisterableField(holdingClass, type, null, (f, v, na) -> {
			adhocRegistry.accept(v);
		});
	}

	/**
	 * Scan a class {@code holdingClass} for static final fields of type {@code type}, and register them
	 * in the configured namespace with a path equal to the field's name as lower case in the given
	 * registry.
	 */
	public <T> void autoRegister(BiConsumer<Identifier, T> registry, Class<?> holdingClass, Class<T> type) {
		eachRegisterableField(holdingClass, type, RegisteredAs.class, (f, v, ann) -> {
			registry.accept(deriveId(f, ann), v);
		});
	}

}
