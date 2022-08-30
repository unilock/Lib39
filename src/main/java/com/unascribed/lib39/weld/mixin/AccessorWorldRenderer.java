package com.unascribed.lib39.weld.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.mojang.blaze3d.vertex.VertexConsumer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.shape.VoxelShape;

@Mixin(WorldRenderer.class)
@Environment(EnvType.CLIENT)
public interface AccessorWorldRenderer {

	@Invoker("drawShapeOutline")
	static void drawShapeOutline(MatrixStack matrices, VertexConsumer consumer, VoxelShape voxelShape, double x, double y, double z, float red, float green, float blue, float alpha) {
		throw new AbstractMethodError();
	}
	
}
