package com.unascribed.lib39.gesundheit;

import java.util.function.Supplier;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.Holder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class DummyClientWorld extends ClientWorld {

	public DummyClientWorld(ClientPlayNetworkHandler netHandler, Properties clientWorldProperties, RegistryKey<World> registryKey, Holder<DimensionType> dimensionType, int chunkManager, int simulationDistance, Supplier<Profiler> profiler, WorldRenderer worldRenderer, boolean debugWorld, long seed) {
		super(null, null, null, null, 0, 0, null, null, false, 0);
		throw new AbstractMethodError();
	}

	private ParticleManager particleManager = MinecraftClient.getInstance().particleManager;
	
	
	
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
		throw new AbstractMethodError();
	}
	
}
