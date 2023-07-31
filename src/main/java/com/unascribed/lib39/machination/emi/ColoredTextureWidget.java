package com.unascribed.lib39.machination.emi;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.emi.emi.api.widget.TextureWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ColoredTextureWidget extends TextureWidget {

	private final int color;
	
	public ColoredTextureWidget(Identifier texture, int x, int y, int width, int height, int u, int v, int regionWidth, int regionHeight, int textureWidth, int textureHeight, int color) {
		super(texture, x, y, width, height, u, v, regionWidth, regionHeight, textureWidth, textureHeight);
		this.color = color;
	}


	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
	}

	@Override
	public void render(GuiGraphics draw, int mouseX, int mouseY, float delta) {
		RenderSystem.setShaderColor((color >> 16 & 0xFF)/255f, (color >> 8 & 0xFF)/255f, (color >> 0 & 0xFF)/255f, (color >> 24 & 0xFF)/255f);
		draw.drawTexture(this.texture, x, y, width, height, u, v, regionWidth, regionHeight, textureWidth, textureHeight);
		RenderSystem.setShaderColor(1, 1, 1, 1);
	}
	
}
