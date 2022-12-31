package com.unascribed.lib39.fractal;

import com.unascribed.lib39.core.P39;
import com.unascribed.lib39.fractal.quack.ItemGroupParent;
import com.unascribed.lib39.fractal.quack.SubTabLocation;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.widget.Bounds;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.item.ItemGroup;

public class Lib39FractalEMIPlugin implements EmiPlugin {

	@Override
	public void register(EmiRegistry registry) {
		registry.addExclusionArea(CreativeInventoryScreen.class, (screen, out) -> {
			ItemGroup selected = P39.screens().getSelectedItemGroup(screen);
			if (selected instanceof ItemGroupParent parent && screen instanceof SubTabLocation stl && parent.lib39Fractal$getChildren() != null && !parent.lib39Fractal$getChildren().isEmpty()) {
				out.accept(new Bounds(stl.lib39Fractal$getX(), stl.lib39Fractal$getY(), stl.lib39Fractal$getW(), stl.lib39Fractal$getH()));
			}
		});
	}

}
