package com.unascribed.lib39.machination.emi;

import com.unascribed.lib39.core.P39;

import dev.emi.emi.api.widget.TextureWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class RotatedTextureWidget extends TextureWidget {
	private final float ang, axisX, axisY, axisZ;
	
	public RotatedTextureWidget(Identifier texture, int x, int y, int width, int height, int u, int v, int regionWidth, int regionHeight, int textureWidth, int textureHeight,
			float ang, float axisX, float axisY, float axisZ) {
		super(texture, x, y, width, height, u, v, regionWidth, regionHeight, textureWidth, textureHeight);
		this.ang = ang;
		this.axisX = axisX;
		this.axisY = axisY;
		this.axisZ = axisZ;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		matrices.push();
		matrices.translate(x, y, 0);
		matrices.translate(width/2, height/2, 0);
		P39.rendering().rotate(matrices, ang, axisX, axisY, axisZ);
		matrices.translate(-width/2, -height/2, 0);
		matrices.translate(-x, -y, 0);
		super.render(matrices, mouseX, mouseY, delta);
		matrices.pop();
	}
}
