package com.unascribed.lib39.phantom.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.unascribed.lib39.phantom.quack.PhantomWorld;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;

@Environment(EnvType.CLIENT)
@Mixin(targets="net/minecraft/client/render/chunk/RenderChunk")
public abstract class MixinRenderChunk {

	@Shadow @Final
	private WorldChunk worldChunk;
	
	@Inject(at=@At("HEAD"), method="getBlockEntityAtPos", cancellable=true)
	public void lib39Phantom$getBlockEntity(BlockPos pos, CallbackInfoReturnable<BlockEntity> ci) {
		if (worldChunk != null && worldChunk.getWorld() instanceof PhantomWorld yw) {
			if (yw.lib39Phantom$isPhased(worldChunk.getPos(), pos)) {
				ci.setReturnValue(null);
			}
		}
	}
	
	@Inject(at=@At("HEAD"), method="getBlockStateAtPos", cancellable=true)
	public void lib39Phantom$getBlockState(BlockPos pos, CallbackInfoReturnable<BlockState> ci) {
		if (worldChunk != null && worldChunk.getWorld() instanceof PhantomWorld yw) {
			if (yw.lib39Phantom$isPhased(worldChunk.getPos(), pos)) {
				ci.setReturnValue(Blocks.VOID_AIR.getDefaultState());
			}
		}
	}
	
}
