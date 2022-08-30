package com.unascribed.lib39.weld;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.unascribed.lib39.weld.api.BigBlock;
import com.unascribed.lib39.weld.mixin.AccessorWorldRenderer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class Lib39WeldClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		WorldRenderEvents.BLOCK_OUTLINE.register((wrc, boc) -> {
			if (boc.blockState().getBlock() instanceof BigBlock) {
				BlockState bs = boc.blockState();
				BigBlock b = (BigBlock)boc.blockState().getBlock();
				VoxelShape shape = VoxelShapes.empty();
				BlockPos origin = boc.blockPos().add(-b.getX(bs), -b.getY(bs), -b.getZ(bs));
				BlockPos opposite = origin.add(b.getXSize(bs)-1, b.getYSize(bs)-1, b.getZSize(bs)-1);
				for (BlockPos bp : BlockPos.iterate(origin, opposite)) {
					BlockState obs = wrc.world().getBlockState(bp);
					if (obs.isOf(bs.getBlock())) {
						VoxelShape thisShape = obs.getOutlineShape(wrc.world(), bp, ShapeContext.of(boc.entity())).offset(bp.getX(), bp.getY(), bp.getZ());
						shape = VoxelShapes.combine(shape, thisShape, BooleanBiFunction.OR);
					}
				}
				VertexConsumer vc = wrc.consumers().getBuffer(RenderLayer.getLines());
				Vec3d cam = wrc.camera().getPos();
				AccessorWorldRenderer.drawShapeOutline(wrc.matrixStack(), vc, shape, -cam.x, -cam.y, -cam.z, 0, 0, 0, 0.4f);
				return false;
			}
			return true;
		});
	}
	
}
