package com.unascribed.lib39.core;

import java.util.Map;
import java.util.function.Consumer;

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

	private static <T> void runEntrypoint(String key, Class<T> clazz, Consumer<T> cons) {
		for (var ec : FabricLoader.getInstance().getEntrypointContainers(key, clazz)) {
			try {
				cons.accept(ec.getEntrypoint());
			} catch (Throwable t) {
				throw new RuntimeException("'"+ec.getProvider().getMetadata().getId()+"' threw an exception during "+key+"!", t);
			}
		}
	}

	private void onPostInitialize() {
		runEntrypoint("lib39:postinit", ModPostInitializer.class, ModPostInitializer::onPostInitialize);
	}

	private void onClientPostInitialize() {
		runEntrypoint("lib39:postinit_client", ClientModPostInitializer.class, ClientModPostInitializer::onPostInitializeClient);
	}

	private void onServerPostInitialize() {
		runEntrypoint("lib39:postinit_server", DedicatedServerModPostInitializer.class, DedicatedServerModPostInitializer::onPostInitializeServer);
	}

}
