package com.unascribed.lib39.recoil.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;

public final class RecoilEvents {

	public static final Event<CameraSetup> CAMERA_SETUP = EventFactory.createArrayBacked(CameraSetup.class, (invokers) ->
		(camera, cameraEntity, perspective, tickDelta, ctrl) -> { for (var t : invokers) t.onCameraSetup(camera, cameraEntity, perspective, tickDelta, ctrl); });

	public static final Event<UpdateFOV> UPDATE_FOV = EventFactory.createArrayBacked(UpdateFOV.class, (invokers) ->
		(fov, tickDelta) -> { for (var t : invokers) t.onUpdateFOV(fov, tickDelta); });
	
	public static final Event<UpdateEntityRenderDistance> UPDATE_ENTITY_RENDER_DISTANCE = EventFactory.createArrayBacked(UpdateEntityRenderDistance.class, (invokers) ->
		(erd) -> { for (var t : invokers) t.onUpdateEntityRenderDistance(erd); });
	
	public static final Event<RenderCrosshairs> RENDER_CROSSHAIRS = EventFactory.createArrayBacked(RenderCrosshairs.class, (invokers) ->
		(matrices) -> {
			for (var t : invokers) {
				if (t.onRenderCrosshairs(matrices)) return true;
			}
			return false;
		});

	
	public interface CameraSetup {
		/**
		 * Called when the camera is being set up. You can change the rotation and location of the
		 * camera here for various effects.
		 * 
		 * @param camera the Camera instance
		 * @param cameraEntity the Entity the Camera belongs to (usually, but not always, a PlayerEntity)
		 * @param perspective the current perspective (cycled with F5)
		 * @param tickDelta the current tick delta
		 * @param ctrl a holder for the camera's rotation and position - mutable
		 */
		void onCameraSetup(Camera camera, Entity cameraEntity, Perspective perspective, float tickDelta, CameraControl ctrl);
	}
	
	public interface UpdateFOV {
		/**
		 * Called when the player's field-of-view is being determined. To properly compound with
		 * other mods, you should use multiplication or lerping from the current value.
		 * 
		 * @param value the FOV holder
		 * @param tickDelta the current tick delta
		 */
		void onUpdateFOV(Vec1f value, float tickDelta);
	}
	
	public interface UpdateEntityRenderDistance {
		/**
		 * Called when the entity render distance multiplier is being determined. 1 will use the
		 * player's setting as-is; return smaller numbers to reduce render distance, larger numbers
		 * to increase it. To properly compound with other mods, you should only multiply the
		 * value.
		 * 
		 * @param value the entity render distance holder
		 */
		void onUpdateEntityRenderDistance(Vec1f value);
	}
	
	public interface RenderCrosshairs {
		/**
		 * Called when the crosshairs are being rendered. No render setup has been done yet - if
		 * you want inverse-color rendering, you'll need to set it up yourself.
		 * @return {@code true} to suppress vanilla crosshairs rendering
		 */
		boolean onRenderCrosshairs(GuiGraphics ctx);
	}
	
	private RecoilEvents() {}
	
}
