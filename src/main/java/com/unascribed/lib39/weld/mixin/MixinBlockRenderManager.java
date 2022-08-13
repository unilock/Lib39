package com.unascribed.lib39.weld.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.unascribed.lib39.weld.api.BigBlock;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

@Environment(EnvType.CLIENT)
@Mixin(BlockRenderManager.class)
public abstract class MixinBlockRenderManager {

	@Shadow
	public abstract void renderDamage(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrix, VertexConsumer vertexConsumer);
	
	private boolean lib39Weld$reentering;
	
	@Inject(at=@At("HEAD"), method="renderDamage(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/client/util/math/MatrixStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;)V",
			cancellable=true)
	public void lib39Weld$renderDamage(BlockState bs, BlockPos pos, BlockRenderView world, MatrixStack matrix, VertexConsumer vertexConsumer, CallbackInfo ci) {
		if (lib39Weld$reentering) return;
		if (bs.getBlock() instanceof BigBlock) {
			BigBlock b = (BigBlock)bs.getBlock();
			int bX = b.getX(bs);
			int bY = b.getY(bs);
			int bZ = b.getZ(bs);
			BlockPos origin = pos.add(-bX, -bY, -bZ);
			lib39Weld$reentering = true;
			try {
				for (int x = 0; x < b.getXSize(); x++) {
					for (int y = 0; y < b.getYSize(); y++) {
						for (int z = 0; z < b.getZSize(); z++) {
							BlockPos bp = origin.add(x, y, z);
							matrix.push();
								matrix.translate(bp.getX()-pos.getX(), bp.getY()-pos.getY(), bp.getZ()-pos.getZ());
								renderDamage(world.getBlockState(bp), bp, world, matrix, vertexConsumer);
							matrix.pop();
						}
					}
				}
			} finally {
				lib39Weld$reentering = false;
			}
			ci.cancel();
		}
	}
	
}
