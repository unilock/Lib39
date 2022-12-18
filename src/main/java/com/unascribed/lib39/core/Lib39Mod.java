package com.unascribed.lib39.core;

import java.util.Map;

import com.unascribed.lib39.core.api.ClientModPostInitializer;
import com.unascribed.lib39.core.api.DedicatedServerModPostInitializer;
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
		onServerPostInitialize();
	}

	@Override
	public void onInitializeClient() {
		onPostInitialize();
		onClientPostInitialize();
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

	private void onClientPostInitialize() {
		for (var ec: FabricLoader.getInstance().getEntrypointContainers("lib39:postinit_client", ClientModPostInitializer.class)) {
			try {
				ec.getEntrypoint().onPostInitializeClient();
			} catch (Throwable t) {
				throw new RuntimeException("'"+ec.getProvider().getMetadata().getId()+"' threw an exception during client postinitialization!", t);
			}
		}
	}

	private void onServerPostInitialize() {
		for (var ec: FabricLoader.getInstance().getEntrypointContainers("lib39:postinit_server", DedicatedServerModPostInitializer.class)) {
			try {
				ec.getEntrypoint().onPostInitializeServer();
			} catch (Throwable t) {
				throw new RuntimeException("'"+ec.getProvider().getMetadata().getId()+"' threw an exception during server postinitialization!", t);
			}
		}
	}

}
