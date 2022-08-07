package com.unascribed.lib39.gesundheit.api;

import java.util.concurrent.ThreadLocalRandom;

import com.unascribed.lib39.gesundheit.quack.ParticleScreen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public final class GuiParticleAccess {

	/**
	 * Works the same as {@link ServerWorld#spawnParticles(ParticleEffect, double, double, double, int, double, double, double, double)}.
	 * Spawns particles in the current screen. Specifically meant to be safe to call from common
	 * code, so you can use this from within Item click handlers or such.
	 * <p>
	 * In a HandledScreen, this is relative to the top left <b>of the background</b> - so you can
	 * pass in slot coordinates. In any other screen, it's relative to the top left of
	 * the window.
	 */
	public static void spawnGuiParticles(ParticleEffect particle, double x, double y, double z, int count, double deltaX, double deltaY, double deltaZ, double speed) {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			var r = ThreadLocalRandom.current();
			for (int i = 0; i < count; ++i) {
				double xo = r.nextGaussian() * deltaX;
				double yo = r.nextGaussian() * deltaY;
				double zo = r.nextGaussian() * deltaZ;
				double vx = r.nextGaussian() * speed;
				double vy = r.nextGaussian() * speed;
				double vz = r.nextGaussian() * speed;
				spawnGuiParticleClient(particle, x + xo, y + yo, z + zo, vx, vy, vz);
			}
		}
	}
	
	/**
	 * Works the same as {@link World#addParticle(ParticleEffect, double, double, double, double, double, double)}.
	 * Spawns particles in the current screen. Specifically meant to be safe to call from common
	 * code, so you can use this from within Item click handlers or such.
	 * <p>
	 * In a HandledScreen, this is relative to the top left <b>of the background</b> - so you can
	 * pass in slot coordinates. In any other screen, it's relative to the top left of
	 * the window.
	 */
	public static void spawnGuiParticle(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			spawnGuiParticleClient(parameters, x, y, z, velocityX, velocityY, velocityZ);
		}
	}
	
	@Environment(EnvType.CLIENT)
	private static void spawnGuiParticleClient(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		if (MinecraftClient.getInstance().currentScreen instanceof ParticleScreen ps) {
			ps.lib39Gesundheit$getParticleWorld().addParticle(parameters, x, y, z, velocityX, velocityY, velocityZ);
		}
	}
	
	private GuiParticleAccess() {}
	
}
