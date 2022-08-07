package com.unascribed.lib39.gesundheit.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;

@Environment(EnvType.CLIENT)
@Mixin(HandledScreen.class)
public interface AccessorHandledScreen {

	@Accessor("x")
	int lib39Gesundheit$getX();
	@Accessor("y")
	int lib39Gesundheit$getY();
	
}
