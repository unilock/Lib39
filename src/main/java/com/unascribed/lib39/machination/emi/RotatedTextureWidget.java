package com.unascribed.lib39.machination.emi;

import org.joml.AxisAngle4f;
import org.joml.Quaternionf;

import com.unascribed.lib39.core.P39;

import dev.emi.emi.api.widget.TextureWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

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
	public void render(GuiGraphics draw, int mouseX, int mouseY, float delta) {
		MatrixStack matrices = draw.getMatrices();
		matrices.push();
		matrices.translate(x, y, 0);
		matrices.translate(width/2, height/2, 0);
		matrices.multiply(new Quaternionf(new AxisAngle4f(ang*MathHelper.RADIANS_PER_DEGREE, axisX, axisY, axisZ)));
		matrices.translate(-width/2, -height/2, 0);
		matrices.translate(-x, -y, 0);
		super.render(draw, mouseX, mouseY, delta);
		matrices.pop();
	}
}
