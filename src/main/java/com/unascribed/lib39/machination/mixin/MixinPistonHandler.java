package com.unascribed.lib39.machination.mixin;

import java.util.List;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.unascribed.lib39.machination.Lib39Machination;
import com.unascribed.lib39.machination.logic.SmashCloudLogic;
import com.unascribed.lib39.machination.recipe.PistonSmashingRecipe;

import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonHandler;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

@Mixin(PistonHandler.class)
public class MixinPistonHandler {

	@Shadow @Final
	private World world;
	@Shadow @Final
	private List<BlockPos> brokenBlocks;
	
	@Inject(at=@At(value="INVOKE", target="net/minecraft/block/BlockState.getPistonBehavior()Lnet/minecraft/block/piston/PistonBehavior;"), method="tryMove", cancellable=true)
	public void lib39Machination$trySmash(BlockPos pos, Direction dir, CallbackInfoReturnable<Boolean> ci, @Local(index = 7) BlockPos moving) {
		BlockState surrounding1 = world.getBlockState(moving.offset(dir));
		BlockState surrounding2 = world.getBlockState(moving.offset(dir.getOpposite()));
		if (surrounding1.getBlock() != surrounding2.getBlock()) return;
		Block catalyst = surrounding1.getBlock();
		BlockState movingState = world.getBlockState(moving);
		for (PistonSmashingRecipe r : world.getRecipeManager().listAllOfType(Lib39Machination.RecipeTypes.PISTON_SMASHING)) {
			if (r.getCatalyst().test(catalyst)) {
				if (r.getInput().test(movingState.getBlock())) {
					if (world.breakBlock(moving, false)) {
						double ofs = 0.5;
						if (world.getBlockState(moving.up()).isAir()) {
							ofs = 1;
						} else if (world.getBlockState(moving.down()).isAir()) {
							ofs = 0;
						}
						if (r.hasCloud()) {
							AreaEffectCloudEntity cloud = new AreaEffectCloudEntity(world, moving.getX()+0.5, moving.getY()+ofs, moving.getZ()+0.5);
							cloud.setColor(r.getCloudColor());
							cloud.setRadius(r.getCloudSize()/4f);
							cloud.setDuration(100);
							cloud.setCustomName(Text.literal(SmashCloudLogic.MAGIC+r.getId()));
							for (StatusEffectInstance sei : r.getCloudEffects()) {
								cloud.addEffect(sei);
							}
							world.spawnEntity(cloud);
						}
						if (!r.getResult(world.getRegistryManager()).isEmpty()) {
							ItemStack stack = r.craft(null, world.getRegistryManager());
							ItemEntity item = new ItemEntity(world, moving.getX()+0.5, moving.getY()+ofs, moving.getZ()+0.5, stack);
							var rnd = ThreadLocalRandom.current();
							item.setVelocity(rnd.nextGaussian()/8, (ofs-0.5)/6, rnd.nextGaussian()/8);
							world.spawnEntity(item);
						}
						ci.setReturnValue(true);
						return;
					}
				}
			}
		}
	}
	
}
