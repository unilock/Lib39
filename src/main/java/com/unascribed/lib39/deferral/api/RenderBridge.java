package com.unascribed.lib39.deferral.api;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.nio.FloatBuffer;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL21;
import com.mojang.blaze3d.systems.RenderSystem;
import com.unascribed.lib39.deferral.Lib39Deferral;
import net.minecraft.client.util.math.MatrixStack;

public class RenderBridge extends GL21 {

	private static final FloatBuffer MATRIX_BUFFER = BufferUtils.createFloatBuffer(4*4);
	
	public static boolean canUseCompatFunctions() {
		return Lib39Deferral.didLoadCompatMode;
	}
	
	public static void glMultMatrixf(Matrix4f mat) {
		mat.get(MATRIX_BUFFER);
		MATRIX_BUFFER.rewind();
		glMultMatrixf(MATRIX_BUFFER);
	}
	
	public static void glDefaultBlendFunc() {
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public static void glPushMCMatrix() {
		glMatrixMode(GL_PROJECTION);
		glPushMatrix();
		glLoadIdentity();
		glMultMatrixf(RenderSystem.getProjectionMatrix());
		glMatrixMode(GL_MODELVIEW);
		glPushMatrix();
		glMultMatrixf(RenderSystem.getModelViewMatrix());
	}
	
	public static void glPushMCMatrix(MatrixStack matrices) {
		glPushMCMatrix();
		glMultMatrixf(matrices.peek().getModel());
	}
	
	public static void glPopMCMatrix() {
		glMatrixMode(GL_PROJECTION);
		glPopMatrix();
		glMatrixMode(GL_MODELVIEW);
		glPopMatrix();
	}
	
	private static final MethodHandle shaderLightDirections; static {
		try {
			shaderLightDirections = MethodHandles.privateLookupIn(RenderSystem.class, MethodHandles.lookup())
					.findStaticGetter(RenderSystem.class, "shaderLightDirections", Vector3f[].class);
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}
	
	public static void glCopyMCLight() {
		Vector3f[] lights;
		try {
			lights = (Vector3f[]) shaderLightDirections.invokeExact();
		} catch (Throwable e) {
			throw new AssertionError(e);
		}
		glPushMatrix();
		glLoadIdentity();
		glEnable(GL_LIGHT0);
		glEnable(GL_LIGHT1);
		glLightfv(GL_LIGHT0, GL_DIFFUSE, new float[] {0.6f, 0.6f, 0.6f, 1.0f});
		glLightfv(GL_LIGHT0, GL_AMBIENT, new float[] {0.0f, 0.0f, 0.0f, 1.0f});
		glLightfv(GL_LIGHT0, GL_SPECULAR, new float[] {0.0f, 0.0f, 0.0f, 1.0f});
		glLightfv(GL_LIGHT0, GL_POSITION, new float[] {lights[0].x(), lights[0].y(), lights[0].z(), 0});
		glLightfv(GL_LIGHT1, GL_DIFFUSE, new float[] {0.6f, 0.6f, 0.6f, 1.0f});
		glLightfv(GL_LIGHT1, GL_AMBIENT, new float[] {0.0f, 0.0f, 0.0f, 1.0f});
		glLightfv(GL_LIGHT1, GL_SPECULAR, new float[] {0.0f, 0.0f, 0.0f, 1.0f});
		glLightfv(GL_LIGHT1, GL_POSITION, new float[] {lights[1].x(), lights[1].y(), lights[1].z(), 0});
		glLightModelfv(GL_LIGHT_MODEL_AMBIENT, new float[] {0.4F, 0.4F, 0.4F, 1.0F});
		glPopMatrix();
	}
	
}
