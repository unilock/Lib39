package com.unascribed.lib39.core.api;

import java.time.MonthDay;
import java.util.function.Supplier;

import com.unascribed.lib39.core.impl.SplashTextHandler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * Helper class for registering splash texts.
 */
@Environment(EnvType.CLIENT)
public final class SplashTextRegistry {
	/**
	 * Register a splash text that will always show up.
	 * <p>
	 * You don't need an example for this one.
	 *
	 * @param text the splash text to register
	 */
	public static void registerStatic(String text) {
		SplashTextHandler.registerStatic(text);
	}
	
	/**
	 * Register a splash text that will show up on the given day.
	 * <p>
	 * Vanilla example: The "OOoooOOOoooo! Spooky!" splash text:
	 * <pre>
	 * SplashTextRegistry.registerTemporal("OOoooOOOoooo! Spooky!", MonthDay.of(Month.OCTOBER, 31));
	 * </pre>
	 * <p>
	 * If multiple lib39 users register splashes on the same day, one of them will be picked at
	 * random. However, lib39 temporal splashes will always replace vanilla splashes and those added
	 * by other mods, as there's no way to detect other temporal splashes.
	 *
	 * @param text the splash text to register
	 * @param when the month and day this splash should show up during
	 */
	public static void registerTemporal(String text, MonthDay when) {
		SplashTextHandler.registerTemporal(text, when);
	}
	
	/**
	 * Register a splash text that does some kind of dynamic logic when it is chosen.
	 * <p>
	 * Vanilla example: The "{USERNAME} IS YOU" splash text:
	 * <pre>
	 * SplashTextRegistry.registerDynamic(() -> MinecraftClient.getInstance().getSession().getUsername().toUpperCase(Locale.ROOT)+" IS YOU");
	 * </pre>
	 * <p>
	 * The given supplier is called <i>only when the splash is first chosen</i>, not every frame.
	 *
	 * @param supplier the supplier to register
	 */
	public static void registerDynamic(Supplier<String> supplier) {
		SplashTextHandler.registerDynamic(supplier);
	}
	
	/**
	 * Remove a <b>vanilla</b> splash text from the game. Useful if you're adding a variant or
	 * corrected version.
	 * 
	 * @param text the splash text to remove
	 */
	public static void remove(String text) {
		SplashTextHandler.remove(text);
	}

	private SplashTextRegistry() {}
}
