package com.unascribed.lib39.phantom.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.unascribed.lib39.phantom.quack.PhantomWorld;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;

@Mixin(RaycastContext.class)
public class MixinRaycastContext {

	@Inject(at=@At("HEAD"), method="getBlockShape", cancellable=true)
	public void lib39Phantom$getBlockShape(BlockState state, BlockView world, BlockPos pos, CallbackInfoReturnable<VoxelShape> ci) {
		if (world instanceof PhantomWorld && ((PhantomWorld)world).lib39Phantom$isPhased(pos)) {
			ci.setReturnValue(VoxelShapes.empty());
		}
	}
	
	@Inject(at=@At("HEAD"), method="getFluidShape", cancellable=true)
	public void lib39Phantom$getFluidShape(FluidState state, BlockView world, BlockPos pos, CallbackInfoReturnable<VoxelShape> ci) {
		if (world instanceof PhantomWorld && ((PhantomWorld)world).lib39Phantom$isPhased(pos)) {
			ci.setReturnValue(VoxelShapes.empty());
		}
	}
	
}
