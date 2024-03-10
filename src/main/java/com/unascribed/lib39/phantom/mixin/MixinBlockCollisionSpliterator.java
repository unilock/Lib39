package com.unascribed.lib39.phantom.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.unascribed.lib39.phantom.quack.PhantomWorld;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockCollisions;
import net.minecraft.world.CollisionView;

@Mixin(BlockCollisions.class)
public class MixinBlockCollisionSpliterator {

	@Shadow @Final
	private BlockPos.Mutable pos;
	@Shadow @Final
	private CollisionView collisionGetter;
	
	@ModifyExpressionValue(at=@At(value="INVOKE", target="net/minecraft/world/BlockView.getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", ordinal=0),
			method="computeNext")
	public BlockState lib39Phantom$replaceBlockState(BlockState in) {
		if (collisionGetter instanceof PhantomWorld && ((PhantomWorld)collisionGetter).lib39Phantom$isPhased(pos)) {
			return Blocks.VOID_AIR.getDefaultState();
		}
		return in;
	}
	
}
