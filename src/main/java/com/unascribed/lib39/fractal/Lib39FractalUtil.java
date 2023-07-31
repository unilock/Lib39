package com.unascribed.lib39.fractal;

import java.lang.invoke.MethodHandles;
import java.util.function.Supplier;

import com.unascribed.lib39.core.api.util.ReflectionHelper;

import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.item.ItemGroup;

public class Lib39FractalUtil {

	private static final Supplier<ItemGroup> selectedTab = ReflectionHelper.of(MethodHandles.lookup(), CreativeInventoryScreen.class)
			.obtainStaticGetter(ItemGroup.class, "field_2896", "selectedTab");

	public static ItemGroup getSelectedItemGroup(CreativeInventoryScreen screen) {
		return selectedTab.get();
	}

}
