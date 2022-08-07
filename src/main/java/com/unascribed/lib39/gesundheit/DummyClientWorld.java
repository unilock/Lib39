package com.unascribed.lib39.gesundheit;

import com.google.gson.internal.UnsafeAllocator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

public class DummyClientWorld extends ClientWorld {

	private ParticleManager particleManager = MinecraftClient.getInstance().particleManager;
	
	private DummyClientWorld() throws Exception {
		super(UnsafeAllocator.create().newInstance(ClientPlayNetworkHandler.class),
				new Properties(Difficulty.PEACEFUL, false, false), World.NETHER,
				MinecraftClient.getInstance().world.getRegistryManager().get(Registry.DIMENSION_TYPE_KEY)
					.getHolderOrThrow(MinecraftClient.getInstance().world.method_44013()),
				0, 0, MinecraftClient.getInstance()::getProfiler,
				MinecraftClient.getInstance().worldRenderer,
				false, 133742069);
	}
	
	@Override
	public DynamicRegistryManager getRegistryManager() {
		return MinecraftClient.getInstance().world.getRegistryManager();
	}
	
	public void setParticleManager(ParticleManager particleManager) {
		this.particleManager = particleManager;
	}

	@Override
	public void addParticle(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		this.particleManager.addParticle(parameters, x, y, z, velocityX, velocityY, velocityZ);
	}

	@Override
	public void addParticle(ParticleEffect parameters, boolean alwaysSpawn, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		this.particleManager.addParticle(parameters, x, y, z, velocityX, velocityY, velocityZ);
	}

	@Override
	public void addImportantParticle(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		this.particleManager.addParticle(parameters, x, y, z, velocityX, velocityY, velocityZ);
	}

	@Override
	public void addImportantParticle(ParticleEffect parameters, boolean alwaysSpawn, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		this.particleManager.addParticle(parameters, x, y, z, velocityX, velocityY, velocityZ);
	}
	
	public static DummyClientWorld create() {
		try {
			return new DummyClientWorld();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
