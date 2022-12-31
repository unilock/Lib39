package com.unascribed.lib39.fractal;

import java.util.List;

import com.unascribed.lib39.core.P39;
import com.unascribed.lib39.fractal.quack.ItemGroupParent;
import com.unascribed.lib39.fractal.quack.SubTabLocation;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.screen.ExclusionZones;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.item.ItemGroup;

public class Lib39FractalREIPlugin implements REIClientPlugin {

	@Override
	public void registerExclusionZones(ExclusionZones zones) {
		zones.register(CreativeInventoryScreen.class, (screen) -> {
			ItemGroup selected = P39.screens().getSelectedItemGroup(screen);
			ItemGroupParent parent = (ItemGroupParent)selected;
			if (screen instanceof SubTabLocation stl && parent.lib39Fractal$getChildren() != null && !parent.lib39Fractal$getChildren().isEmpty()) {
				return List.of(new Rectangle(stl.lib39Fractal$getX(), stl.lib39Fractal$getY(), stl.lib39Fractal$getW(), stl.lib39Fractal$getH()));
			}
			return List.of();
		});
	}

}
