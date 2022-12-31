package com.unascribed.lib39.weld;

import com.unascribed.lib39.core.P39;

import net.fabricmc.api.ModInitializer;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class Lib39Weld implements ModInitializer {

	private static final Identifier SILENCE_ID = new Identifier("lib39-weld", "silence");
	
	public static final SoundEvent SILENCE = P39.registries().createSoundEvent(SILENCE_ID);
	
	@Override
	public void onInitialize() {
		P39.registries().register(P39.registries().soundEvent(), SILENCE_ID, SILENCE);
	}

}
