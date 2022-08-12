package com.unascribed.lib39.recoil.api;

import net.minecraft.util.math.Vec3d;

public final class CameraControl {
	
	private float yaw, pitch, roll;
	private Vec3d pos;

	public CameraControl(Vec3d pos, float yaw, float pitch, float roll) {
		this.pos = pos;
		this.yaw = yaw;
		this.pitch = pitch;
		this.roll = roll;
	}
	
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}
	
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	
	public void setRoll(float roll) {
		this.roll = roll;
	}
	
	public void setPos(Vec3d pos) {
		this.pos = pos;
	}
	
	public float getYaw() {
		return yaw;
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public float getRoll() {
		return roll;
	}

	public Vec3d getPos() {
		return pos;
	}
	
}