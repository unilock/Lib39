package com.unascribed.lib39.weld.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.unascribed.lib39.weld.api.BigBlock;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;

@Environment(EnvType.CLIENT)
@Mixin(ParticleManager.class)
public abstract class MixinParticleManager {

	@Shadow
	protected ClientWorld world;
	
	@Shadow
	public abstract void addBlockBreakingParticles(BlockPos pos, Direction dir);
	
	private boolean lib39Weld$reentering = false;
	
	@Inject(at=@At("HEAD"), method="addBlockBreakingParticles(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)V",
			cancellable=true)
	public void lib39Weld$addBlockBreakingParticles(BlockPos pos, Direction dir, CallbackInfo ci) {
		if (lib39Weld$reentering) return;
		BlockState bs = world.getBlockState(pos);
		if (bs.getBlock() instanceof BigBlock) {
			BigBlock b = (BigBlock)bs.getBlock();
			int bX = b.getX(bs);
			int bY = b.getY(bs);
			int bZ = b.getZ(bs);
			lib39Weld$reentering = true;
			try {
				// there's probably a better way to do this, but whatever
				if (dir.getAxis() == Axis.X) {
					for (int y = -bY; y < b.getYSize(bs)-bY; y++) {
						for (int z = -bZ; z < b.getZSize(bs)-bZ; z++) {
							if (y == 0 && z == 0) continue;
							addBlockBreakingParticles(pos.add(0, y, z), dir);
						}
					}
				} else if (dir.getAxis() == Axis.Z) {
					for (int y = -bY; y < b.getYSize(bs)-bY; y++) {
						for (int x = -bX; x < b.getXSize(bs)-bX; x++) {
							if (y == 0 && x == 0) continue;
							addBlockBreakingParticles(pos.add(x, y, 0), dir);
						}
					}
				} else if (dir.getAxis() == Axis.Y) {
					for (int x = -bX; x < b.getXSize(bs)-bX; x++) {
						for (int z = -bZ; z < b.getZSize(bs)-bZ; z++) {
							if (x == 0 && z == 0) continue;
							addBlockBreakingParticles(pos.add(x, 0, z), dir);
						}
					}
				}
			} finally {
				lib39Weld$reentering = false;
			}
		}
	}
	
}
