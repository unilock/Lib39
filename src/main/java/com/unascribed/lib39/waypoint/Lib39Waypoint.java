package com.unascribed.lib39.waypoint;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class Lib39Waypoint implements ClientModInitializer {

	public static boolean retrievingHalo;

	@Override
	public void onInitializeClient() {
		ClientTickEvents.START_CLIENT_TICK.register(mc -> HaloRenderer.tick());
		WorldRenderEvents.AFTER_ENTITIES.register(HaloRenderer::render);
		
		ModelPredicateProviderRegistry.register(new Identifier("lib39", "halo"), (stack, world, entity, seed) -> {
			System.out.println(stack+" "+retrievingHalo);
			return retrievingHalo ? 1 : 0;
		});
		
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public void reload(ResourceManager manager) {
				HaloRenderer.clearCache();
			}
			
			@Override
			public Identifier getFabricId() {
				return new Identifier("lib39", "clear_render_cache");
			}
		});
	}

}
