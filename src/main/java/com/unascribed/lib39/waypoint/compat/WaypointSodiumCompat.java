package com.unascribed.lib39.waypoint.compat;

import com.unascribed.lib39.waypoint.SodiumAccess;

import me.jellysquid.mods.sodium.client.render.texture.SpriteUtil;

public class WaypointSodiumCompat {

	public static void init() {
		SodiumAccess.markSpriteActive = SpriteUtil::markSpriteActive;
	}
	
}
