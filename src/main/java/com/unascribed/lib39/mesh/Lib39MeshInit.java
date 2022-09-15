package com.unascribed.lib39.mesh;

import com.unascribed.lib39.mesh.api.BlockNetworkManager;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class Lib39MeshInit implements ModInitializer {

	@Override
	public void onInitialize() {
		ServerTickEvents.START_WORLD_TICK.register((world) -> {
			BlockNetworkManager.get(world).tick();
		});
	}

}
