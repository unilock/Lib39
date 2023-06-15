package com.unascribed.lib39.machination.emi;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.texture.NativeImage;
import com.unascribed.lib39.core.P39;
import dev.emi.emi.api.widget.TextureWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ColoredTextureWidget extends TextureWidget {

	private final int color;
	
	public ColoredTextureWidget(Identifier texture, int x, int y, int width, int height, int u, int v, int regionWidth, int regionHeight, int textureWidth, int textureHeight, int color) {
		super(texture, x, y, width, height, u, v, regionWidth, regionHeight, textureWidth, textureHeight);
		this.color = color;
	}


	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(getBlue(color)/255f, getGreen(color)/255f, getRed(color)/255f, getAlpha(color)/255f);
		RenderSystem.setShaderTexture(0, this.texture);
		P39.rendering().drawTexture(matrices, x, y, width, height, u, v, regionWidth, regionHeight, textureWidth, textureHeight);
		RenderSystem.setShaderColor(1, 1, 1, 1);
	}

	// Soft override for 1.20
	public void method_25394(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		render(guiGraphics.method_51448(), mouseX, mouseY, delta);
	}

	// Doesn't exist cross version, so copy it over

	private static int getAlpha(int color) {
		return color >> 24 & 0xFF;
	}

	private static int getRed(int color) {
		return color >> 0 & 0xFF;
	}

	private static int getGreen(int color) {
		return color >> 8 & 0xFF;
	}

	private static int getBlue(int color) {
		return color >> 16 & 0xFF;
	}
}
