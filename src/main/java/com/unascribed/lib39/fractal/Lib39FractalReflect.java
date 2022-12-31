package com.unascribed.lib39.fractal;

import java.lang.invoke.MethodHandles;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.unascribed.lib39.core.api.util.ReflectionHelper;

import net.minecraft.item.ItemGroup;

public class Lib39FractalReflect {

	private static final ReflectionHelper<ItemGroup> helper = ReflectionHelper.of(MethodHandles.lookup(), ItemGroup.class);
	
	public static final Supplier<ItemGroup[]> GET_GROUPS = helper.obtainStaticGetter(ItemGroup[].class, "field_7921", "GROUPS");
	public static final Consumer<ItemGroup[]> SET_GROUPS = helper.obtainStaticSetter(ItemGroup[].class, "field_7921", "GROUPS");
	
}
