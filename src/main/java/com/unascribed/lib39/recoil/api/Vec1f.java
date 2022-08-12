package com.unascribed.lib39.recoil.api;

public class Vec1f {
	private float v;

	public Vec1f(float v) {
		this.v = v;
	}
	
	public float get() {
		return v;
	}
	
	public void set(float v) {
		this.v = v;
	}
	
	public void add(float f) {
		this.v += f;
	}
	
	public void scale(float f) {
		this.v *= f;
	}
	
}