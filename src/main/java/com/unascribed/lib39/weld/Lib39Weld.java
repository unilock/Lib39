package com.unascribed.lib39.weld;

import com.unascribed.lib39.core.P39;

import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class Lib39Weld implements ModInitializer {

	public static final SoundEvent SILENCE = new SoundEvent(new Identifier("lib39-weld", "silence"));
	
	@Override
	public void onInitialize() {
		Registry.register(P39.registries().soundEvent(), "lib39-weld:silence", SILENCE);
	}

}
