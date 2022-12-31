package com.unascribed.lib39.gesundheit;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.unascribed.lib39.core.P39;
import com.unascribed.lib39.util.api.DelegatingVertexConsumer;

import net.minecraft.client.util.math.MatrixStack;

class TransformingVertexConsumer extends DelegatingVertexConsumer {

	private final MatrixStack matrices;
	
	public TransformingVertexConsumer(VertexConsumer delegate, MatrixStack matrices) {
		super(delegate);
		this.matrices = matrices;
	}
	
	@Override
	public VertexConsumer vertex(double x, double y, double z) {
		var vec = P39.rendering().transform(matrices, x, y, z);
		return super.vertex(vec[0], vec[1], vec[2]);
	}
	
}