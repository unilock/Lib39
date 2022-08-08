package com.unascribed.lib39.sandman;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class Lib39Sandman implements ModInitializer {

	@Override
	public void onInitialize() {
		ServerTickEvents.START_WORLD_TICK.register(TickAlwaysItemHandler::startServerWorldTick);
	}
	
}
