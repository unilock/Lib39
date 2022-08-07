package com.unascribed.lib39.core;

import java.util.Map;

import com.unascribed.lib39.core.api.ModPostInitializer;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class Lib39Mod implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {

	public static final Map<Identifier, SoundEvent> craftingSounds = Maps.newHashMap();
	public static final Multimap<Identifier, Identifier> discoveries = HashMultimap.create();
	
	@Override
	public void onInitialize() {
		
	}

	@Override
	public void onInitializeServer() {
		onPostInitialize();
	}

	@Override
	public void onInitializeClient() {
		onPostInitialize();
	}

	private void onPostInitialize() {
		for (var ec : FabricLoader.getInstance().getEntrypointContainers("lib39:postinit", ModPostInitializer.class)) {
			try {
				ec.getEntrypoint().onPostInitialize();
			} catch (Throwable t) {
				throw new RuntimeException("'"+ec.getProvider().getMetadata().getId()+"' threw an exception during postinitialization!", t);
			}
		}
	}

}
