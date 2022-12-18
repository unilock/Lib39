package com.unascribed.lib39.core.api;

public interface ClientModPostInitializer {

	/**
	 * Called after all ModPostInitializers have been called, but potentially before non-post client
	 * initializers have been called.
	 */
	void onPostInitializeClient();
}
