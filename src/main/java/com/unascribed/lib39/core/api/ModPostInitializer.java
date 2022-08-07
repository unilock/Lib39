package com.unascribed.lib39.core.api;

public interface ModPostInitializer {

	/**
	 * Called after all ModInitializers have been called, but potentially before client and server
	 * initializers have been called.
	 */
	void onPostInitialize();
	
}
