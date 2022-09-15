package com.unascribed.lib39.conflagration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.fabricmc.api.ModInitializer;

public class Lib39ConflagrationInit implements ModInitializer {

	public static boolean isInitialized = false;
	public static final List<Runnable> initTasks = Collections.synchronizedList(new ArrayList<>());
	
	@Override
	public void onInitialize() {
		isInitialized = true;
		synchronized (initTasks) {
			initTasks.forEach(Runnable::run);
			initTasks.clear();
		}
	}

}
