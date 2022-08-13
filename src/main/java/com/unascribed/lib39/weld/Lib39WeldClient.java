package com.unascribed.lib39.weld;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.unascribed.lib39.weld.api.BigBlock;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.WorldRenderer;

public class Lib39WeldClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		WorldRenderEvents.BLOCK_OUTLINE.register((wrc, boc) -> {
			if (boc.blockState().getBlock() instanceof BigBlock) {
				BlockState bs = boc.blockState();
				BigBlock b = (BigBlock)boc.blockState().getBlock();
				double minX = boc.blockPos().getX()-b.getX(bs);
				double minY = boc.blockPos().getY()-b.getY(bs);
				double minZ = boc.blockPos().getZ()-b.getZ(bs);
				minX -= wrc.camera().getPos().x;
				minY -= wrc.camera().getPos().y;
				minZ -= wrc.camera().getPos().z;
				double maxX = minX+b.getXSize();
				double maxY = minY+b.getYSize();
				double maxZ = minZ+b.getZSize();
				VertexConsumer vc = wrc.consumers().getBuffer(RenderLayer.getLines());
				WorldRenderer.drawBox(wrc.matrixStack(), vc, minX, minY, minZ, maxX, maxY, maxZ, 0, 0, 0, 0.4f);
				return false;
			}
			return true;
		});
	}
	
}
