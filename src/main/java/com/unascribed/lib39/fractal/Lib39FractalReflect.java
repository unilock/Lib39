package com.unascribed.lib39.fractal;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.unascribed.lib39.core.api.util.ReflectionHelper;

import com.unascribed.lib39.fractal.api.ItemSubGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public class Lib39FractalReflect {

	private static final ReflectionHelper<ItemGroup> helper = ReflectionHelper.of(MethodHandles.lookup(), ItemGroup.class);
	
	public static final Supplier<ItemGroup[]> GET_GROUPS = helper.obtainStaticGetter(ItemGroup[].class, "field_7921", "GROUPS");
	public static final Consumer<ItemGroup[]> SET_GROUPS = helper.obtainStaticSetter(ItemGroup[].class, "field_7921", "GROUPS");

	public static final MethodHandle APPEND_STACKS = helper.tryObtainVirtual(MethodType.methodType(Void.class, ItemGroup.class, DefaultedList.class), "appendStacks", "method_7738");

	public static void appendStacks(ItemGroup inst, DefaultedList<ItemStack> stacks) {
		try {
			APPEND_STACKS.invoke(inst, stacks);
		} catch (Throwable e) {
			throw new RuntimeException("No exception expected here", e);
		}
	}
	
}
