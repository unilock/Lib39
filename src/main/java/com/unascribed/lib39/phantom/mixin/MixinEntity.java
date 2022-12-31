package com.unascribed.lib39.phantom.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.unascribed.lib39.phantom.quack.PhantomWorld;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

@Mixin(Entity.class)
public abstract class MixinEntity {

	@Shadow
	public boolean onGround;
	
	@Shadow
	public World world;
	
	@Shadow
	public abstract double getX();
	@Shadow
	public abstract double getEyeY();
	@Shadow
	public abstract double getZ();
	
	private BlockPos lib39Phantom$currentlyCollidingPos = null;
	
	@Inject(at=@At(value="INVOKE", target="net/minecraft/util/math/BlockPos.getX()I"),
			method="checkBlockCollision", locals=LocalCapture.CAPTURE_FAILHARD)
	public void lib39Phantom$storeMutableForBlock(CallbackInfo ci, Box box, BlockPos start, BlockPos end, BlockPos.Mutable mut) {
		lib39Phantom$currentlyCollidingPos = mut;
	}
	
	@Inject(at=@At("RETURN"), method="checkBlockCollision")
	public void lib39Phantom$forgetMutableForBlock(CallbackInfo ci) {
		lib39Phantom$currentlyCollidingPos = null;
	}
	
	@ModifyVariable(at=@At(value="INVOKE_ASSIGN", target="net/minecraft/world/World.getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"),
			ordinal=0, method="checkBlockCollision")
	public BlockState lib39Phantom$replaceBlockState(BlockState in) {
		if (lib39Phantom$currentlyCollidingPos == null) return in;
		if (world instanceof PhantomWorld && ((PhantomWorld)world).lib39Phantom$isPhased(lib39Phantom$currentlyCollidingPos)) {
			return Blocks.VOID_AIR.getDefaultState();
		}
		return in;
	}
	
	@ModifyVariable(at=@At(value="INVOKE_ASSIGN", target="net/minecraft/world/World.getFluidState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/fluid/FluidState;"),
			ordinal=0, method="updateSubmergedInWaterState")
	public FluidState lib39Phantom$replaceFluidStateForSubmerge(FluidState in) {
		if (in.isEmpty()) return in;
		if (world instanceof PhantomWorld && ((PhantomWorld)world).lib39Phantom$isPhased(new BlockPos(getX(), getEyeY()-0.1111111119389534, getZ()))) {
			return Fluids.EMPTY.getDefaultState();
		}
		return in;
	}
	
	@ModifyVariable(at=@At(value="INVOKE", target="net/minecraft/util/math/BlockPos$Mutable.set(III)Lnet/minecraft/util/math/BlockPos$Mutable;"),
			method="updateMovementInFluid", ordinal=0)
	public BlockPos.Mutable lib39Phantom$storeMutableForFluid(BlockPos.Mutable mut) {
		lib39Phantom$currentlyCollidingPos = mut;
		return mut;
	}
	
	@Inject(at=@At("RETURN"), method="updateMovementInFluid")
	public void lib39Phantom$forgetMutableForFluid(@Coerce Object tag, double d, CallbackInfoReturnable<Boolean> ci) {
		lib39Phantom$currentlyCollidingPos = null;
	}
	
	@ModifyVariable(at=@At(value="INVOKE_ASSIGN", target="net/minecraft/world/World.getFluidState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/fluid/FluidState;"),
			ordinal=0, method="updateMovementInFluid")
	public FluidState lib39Phantom$replaceFluidStateForTouch(FluidState in) {
		if (in.isEmpty()) return in;
		if (world instanceof PhantomWorld && ((PhantomWorld)world).lib39Phantom$isPhased(lib39Phantom$currentlyCollidingPos)) {
			return Fluids.EMPTY.getDefaultState();
		}
		return in;
	}
	
	// isInsideWall lambda
	@ModifyVariable(at=@At(value="INVOKE_ASSIGN", target="net/minecraft/world/World.getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"),
			ordinal=0, method={
					"m_ziqjvkiq(Lnet/minecraft/util/math/Box;Lnet/minecraft/util/math/BlockPos;)Z",
					"method_30022(Lnet/minecraft/util/math/Box;Lnet/minecraft/util/math/BlockPos;)Z"
			})
	public BlockState lib39Phantom$replaceBlockStateForSuffocation(BlockState in, Box box, BlockPos pos) {
		if (world instanceof PhantomWorld && ((PhantomWorld)world).lib39Phantom$isPhased(pos)) {
			return Blocks.VOID_AIR.getDefaultState();
		}
		return in;
	}
	
}
