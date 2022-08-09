package com.unascribed.lib39.deferral.api;

public interface GLCompatVoter {

	/**
	 * Vote on whether or not the OpenGL Compatibility Profile should be enabled. If there is at
	 * least one {@code true} vote and the current platform supports it, it will be enabled.
	 * <p>
	 * You can use this to offer a configuration option to not enable compat profile.
	 * 
	 * @return your mod's vote for compat profile enablement
	 */
	boolean wantsCompatibilityProfile();
	
}
