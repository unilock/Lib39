package com.unascribed.lib39.fractal;

import java.util.List;

import com.unascribed.lib39.core.P39;
import com.unascribed.lib39.fractal.quack.ItemGroupParent;
import com.unascribed.lib39.fractal.quack.SubTabLocation;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;

public class Lib39FractalJEIPlugin implements IModPlugin {

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addGuiContainerHandler(CreativeInventoryScreen.class, new IGuiContainerHandler<CreativeInventoryScreen>() {
			@Override
			public List<Rect2i> getGuiExtraAreas(CreativeInventoryScreen screen) {
				ItemGroup selected = P39.screens().getSelectedItemGroup(screen);
				if (selected instanceof ItemGroupParent parent && screen instanceof SubTabLocation stl && parent.lib39Fractal$getChildren() != null && !parent.lib39Fractal$getChildren().isEmpty()) {
					return List.of(new Rect2i(stl.lib39Fractal$getX(), stl.lib39Fractal$getY(), stl.lib39Fractal$getW(), stl.lib39Fractal$getH()));
				}
				return List.of();
			}
		});
	}
	
	@Override
	public Identifier getPluginUid() {
		return new Identifier("lib39-fractal", "main");
	}

}
