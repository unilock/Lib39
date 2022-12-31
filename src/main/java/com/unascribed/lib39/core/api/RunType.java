package com.unascribed.lib39.core.api;

import net.fabricmc.loader.api.FabricLoader;

public enum RunType {
	DEVELOPMENT,
	PRODUCTION,
	;
	
	public static RunType getCurrent() {
		return FabricLoader.getInstance().isDevelopmentEnvironment() ? DEVELOPMENT : PRODUCTION;
	}
}
