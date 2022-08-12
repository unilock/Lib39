package com.unascribed.lib39.recoil.mixin;

import org.spongepowered.asm.mixin.Final;
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
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.BlockView;

@Environment(EnvType.CLIENT)
@Mixin(Camera.class)
public abstract class MixinCamera {
	
	@Shadow
	private float pitch;
	@Shadow
	private float yaw;
	
	@Shadow @Final
	private Vec3f horizontalPlane;
	@Shadow @Final
	private Vec3f verticalPlane;
	@Shadow @Final
	private Vec3f diagonalPlane;
	@Shadow @Final
	private Quaternion rotation;
	
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
		CameraControl ctrl = new CameraControl(getPos(), getYaw(), getPitch(), 0);
		RecoilEvents.CAMERA_SETUP.invoker().onCameraSetup((Camera)self, focusedEntity, p, tickDelta, ctrl);
		if (ctrl.getPos() != getPos()) {
			setPos(ctrl.getPos());
		}
		if (ctrl.getYaw() != getYaw() || ctrl.getPitch() != getPitch() || ctrl.getRoll() != 0) {
			if (ctrl.getRoll() != 0) {
				// oh no...
				pitch = ctrl.getPitch();
				yaw = ctrl.getYaw();
				rotation.set(0, 0, 0, 1);
				rotation.hamiltonProduct(Vec3f.POSITIVE_Y.getDegreesQuaternion(-yaw));
				rotation.hamiltonProduct(Vec3f.POSITIVE_X.getDegreesQuaternion(pitch));
				rotation.hamiltonProduct(Vec3f.POSITIVE_Z.getDegreesQuaternion(ctrl.getRoll()));
				horizontalPlane.set(0, 0, 1);
				horizontalPlane.rotate(rotation);
				verticalPlane.set(0, 1, 0);
				verticalPlane.rotate(rotation);
				diagonalPlane.set(1, 0, 0);
				diagonalPlane.rotate(rotation);
			} else {
				setRotation(ctrl.getYaw(), ctrl.getPitch());
			}
		}
	}
	
}
