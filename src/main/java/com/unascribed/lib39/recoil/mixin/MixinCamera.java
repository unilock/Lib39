package com.unascribed.lib39.recoil.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.unascribed.lib39.recoil.api.CameraControl;
import com.unascribed.lib39.recoil.api.RecoilEvents;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;

@Environment(EnvType.CLIENT)
@Mixin(Camera.class)
public abstract class MixinCamera {
	
	@Shadow
	private float pitch;
	@Shadow
	private float yaw;
	
	@Shadow
	protected abstract void setRotation(float yaw, float pitch);

	@Shadow
	protected abstract void setPos(Vec3d pos);

	@Shadow
	public abstract Vec3d getPos();
	
	@Shadow
	public abstract float getPitch();
	
	@Shadow
	public abstract float getYaw();
	
	@Inject(at=@At("TAIL"), method="update")
	public void update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
		Perspective p;
		if (thirdPerson) {
			p = inverseView ? Perspective.THIRD_PERSON_FRONT : Perspective.THIRD_PERSON_BACK;
		} else {
			p = Perspective.FIRST_PERSON;
		}
		Object self = this;
		CameraControl ctrl = new CameraControl(getPos(), getYaw(), getPitch());
		RecoilEvents.CAMERA_SETUP.invoker().onCameraSetup((Camera)self, focusedEntity, p, tickDelta, ctrl);
		if (ctrl.getPos() != getPos()) {
			setPos(ctrl.getPos());
		}
		if (ctrl.getYaw() != getYaw() || ctrl.getPitch() != getPitch()) {
			setRotation(ctrl.getYaw(), ctrl.getPitch());
		}
	}
	
}
