package com.unascribed.lib39.ripple.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.SplashTextRenderer;

@Mixin(SplashTextRenderer.class)
public interface AccessorSplashTextRenderer {

	@Accessor("splashText")
	String lib39Ripple$getSplashText();
	
}
