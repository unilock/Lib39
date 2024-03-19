package com.unascribed.lib39.phantom.mixin.client.sodium;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.unascribed.lib39.phantom.quack.PhantomWorld;
import me.jellysquid.mods.sodium.client.world.WorldSlice;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;

@Mixin(WorldSlice.class)
@Pseudo
public class MixinWorldSlice {
	@Shadow @Final
	private ClientWorld world;

	@Inject(at=@At("HEAD"), method="getBlockState(III)Lnet/minecraft/block/BlockState;", cancellable=true, require=0)
	public void lib39Phantom$getBlockState(int x, int y, int z, CallbackInfoReturnable<BlockState> ci) {
		if (this.world instanceof PhantomWorld phantomWorld && phantomWorld.lib39Phantom$isPhased(x, y, z)) {
			ci.setReturnValue(Blocks.VOID_AIR.getDefaultState());
		}
	}
}
