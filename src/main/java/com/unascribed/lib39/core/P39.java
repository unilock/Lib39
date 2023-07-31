package com.unascribed.lib39.core;

import com.google.common.collect.Iterables;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.registry.Holder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

/**
 * <b>NOT API. Do not use.</b>
 */
public class P39 {
	
	/*
	 * And on the pedestal, these words appear:
	 * My name is Port39, Dispatcher of Changes;
	 * Look on my Works, ye Mighty, and despair!
	 * Nothing beside remains. Round the decay
	 * Of that colossal Wreck, boundless and bare
	 * The lone and level sands stretch far away.
	 */

	
	// yeah sure this tag abstraction can stay why not. vanilla's api is trash
	
	public interface Tag<T> {
		Iterable<T> getAll();
		boolean has(T t);
	}

	public static <T> Tag<T> getTag(Registry<T> registry, Identifier id) {
		var tag = registry.getTag(TagKey.of(registry.getKey(), id)).get();
		return new Tag<T>() {
	
			
			@Override
			public Iterable<T> getAll() {
				return Iterables.transform(tag, h -> h.value());
			}
	
			@Override
			@SuppressWarnings({ "deprecation", "unchecked" })
			public boolean has(T t) {
				if (t == null) throw new NullPointerException();
				@SuppressWarnings("rawtypes")
				Holder holder = null;
				if (t instanceof Block b) holder = b.getBuiltInRegistryHolder();
				if (t instanceof Item i) holder = i.getBuiltInRegistryHolder();
				if (t instanceof Fluid f) holder = f.getBuiltInRegistryHolder();
				if (holder == null) throw new UnsupportedOperationException(t.getClass().getName());
				return tag.contains(holder);
			}
		};
	}
	
}
