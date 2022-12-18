package com.unascribed.lib39.core.api;

public interface DedicatedServerModPostInitializer {

	/**
	 * Called after all ModPostInitializers have been called, but potentially before non-post server
	 * initializers have been called.
	 */
	void onPostInitializeServer();
}
