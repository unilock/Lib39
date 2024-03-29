package com.unascribed.lib39.machination.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.unascribed.lib39.machination.quack.WetWorld;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(ItemEntity.class)
public abstract class MixinItemEntity extends Entity {

	public MixinItemEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(at=@At("TAIL"), method="tick")
	public void lib39Machination$tick(CallbackInfo ci) {
		if (getWorld() instanceof WetWorld ww) {
			BlockPos pos = getBlockPos();
			if (!getWorld().getFluidState(pos).isEmpty()) {
				ww.lib39Machination$getSoakingMap().put(pos, (ItemEntity)(Object)this);
			}
		}
	}
	
}
