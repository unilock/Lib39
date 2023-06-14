package com.unascribed.lib39.phantom.mixin;

import com.unascribed.lib39.core.P39;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.unascribed.lib39.phantom.quack.PhantomWorld;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {

	public MixinLivingEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	@Shadow
	public abstract void travel(Vec3d movementInput);
	
	private DamageSource lib39Phantom$customDamageSource = null;
	
	@Inject(at=@At("TAIL"), method="tick")
	public void lib39Phantom$tick(CallbackInfo ci) {
		if (getWorld().isClient) return;
		if (getWorld() instanceof PhantomWorld) {
			PhantomWorld yw = (PhantomWorld)getWorld();
			if (isOnGround() && fallDistance <= 0) {
				lib39Phantom$customDamageSource = null;
			} else if (lib39Phantom$customDamageSource == null) {
				BlockPos bp = getBlockPos();
				if (yw.lib39Phantom$isPhased(bp)) {
					DamageSource customSrc = yw.lib39Phantom$getDamageSource(bp);
					if (customSrc != null) {
						lib39Phantom$customDamageSource = customSrc;
					}
				}
			}
		}
	}
	
	@ModifyVariable(at=@At("HEAD"), method="damage", argsOnly=true, ordinal=0)
	public DamageSource lib39Phantom$modifyDamageSource(DamageSource src) {
		if (P39.worlds().isFallingDamageType(src) && lib39Phantom$customDamageSource != null) {
			return lib39Phantom$customDamageSource;
		}
		return src;
	}
	
}
