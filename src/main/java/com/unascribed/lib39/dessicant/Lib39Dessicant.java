package com.unascribed.lib39.dessicant;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class Lib39Dessicant implements ModInitializer {

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((server, mgr) -> {
			DessicantData.discoveries.clear();
		});
	}

}
