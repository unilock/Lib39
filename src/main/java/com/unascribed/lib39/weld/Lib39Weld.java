package com.unascribed.lib39.weld;

import net.fabricmc.api.ModInitializer;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Lib39Weld implements ModInitializer {

	public static final SoundEvent SILENCE = new SoundEvent(new Identifier("lib39-weld", "silence"));
	
	@Override
	public void onInitialize() {
		Registry.register(Registry.SOUND_EVENT, "lib39-weld:silence", SILENCE);
	}

}
