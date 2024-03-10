package com.unascribed.lib39.phantom.mixin.client.sodium;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.unascribed.lib39.phantom.quack.PhantomWorld;
import me.jellysquid.mods.sodium.client.world.WorldSlice;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;

@Mixin(WorldSlice.class)
@Pseudo
public class MixinWorldSlice {
	
	// This is a very weird mixin because it applies to two different versions of Sodium's WorldSlice.

	private PhantomWorld lib39Phantom$world;
	
//	@Dynamic
//	private int baseX;
//	@Dynamic
//	private int baseY;
//	@Dynamic
//	private int baseZ;
	
//	@Inject(at=@At("TAIL"), method="init", require=0, remap=false)
//	public void lib39Phantom$init(@Coerce Object builder, World world, ChunkSectionPos chunkPos, WorldChunk[] chunks, CallbackInfo ci) {
//		if (world instanceof PhantomWorld) {
//			lib39Phantom$world = (PhantomWorld)world;
//		}
//	}
	
	@Inject(at=@At("TAIL"), method="<init>", require=0, remap=false)
	public void lib39Phantom$init(ClientWorld world, CallbackInfo ci) {
		if (world instanceof PhantomWorld) {
			lib39Phantom$world = (PhantomWorld)world;
		}
	}
	
	@Inject(at=@At("HEAD"), method="getBlockState(III)Lnet/minecraft/block/BlockState;", cancellable=true, require=0)
	public void lib39Phantom$getBlockState(int x, int y, int z, CallbackInfoReturnable<BlockState> ci) {
		if (lib39Phantom$world != null && lib39Phantom$world.lib39Phantom$isPhased(x, y, z)) {
			ci.setReturnValue(Blocks.VOID_AIR.getDefaultState());
		}
	}
	
//	@Inject(at=@At("HEAD"), method="getBlockStateRelative(III)Lnet/minecraft/block/BlockState;", cancellable=true, require=0)
//	public void lib39Phantom$getBlockStateRelative(int x, int y, int z, CallbackInfoReturnable<BlockState> ci) {
//		if (lib39Phantom$world != null && lib39Phantom$world.lib39Phantom$isPhased(baseX+x, baseY+y, baseZ+z)) {
//			ci.setReturnValue(Blocks.VOID_AIR.getDefaultState());
//		}
//	}
	
}
