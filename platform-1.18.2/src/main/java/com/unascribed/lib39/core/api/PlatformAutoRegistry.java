package com.unascribed.lib39.core.api;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.unascribed.lib39.core.api.util.LatchHolder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

class PlatformAutoRegistry extends BaseAutoRegistry {

	PlatformAutoRegistry(String namespace) {
		super(namespace);
	}

	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	<T> void assignHolder(Registry<T> registry, Class<?> holdingClass, String field, Identifier id, Class<? extends LatchHolder> type) {
		try {
			Field holderField = holdingClass.getDeclaredField(field);
			if (holderField.getType() == LatchHolder.class
					&& Modifier.isStatic(holderField.getModifiers()) && !Modifier.isTransient(holderField.getModifiers())) {
				((LatchHolder)holderField.get(null)).set(registry.getOrCreateHolder(RegistryKey.of(registry.getKey(), id)));
			}
		} catch (Exception e) {}
	}
	
}
