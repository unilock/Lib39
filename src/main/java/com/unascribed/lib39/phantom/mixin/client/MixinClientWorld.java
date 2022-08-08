package com.unascribed.lib39.phantom.mixin.client;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.unascribed.lib39.phantom.quack.PhantomWorld;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Holder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public abstract class MixinClientWorld extends World implements PhantomWorld {

	protected MixinClientWorld(MutableWorldProperties properties, RegistryKey<World> registryKey, Holder<DimensionType> dimension, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
		super(properties, registryKey, dimension, profiler, isClient, debugWorld, seed, maxChainedNeighborUpdates);
	}

	@Shadow
	private WorldRenderer worldRenderer;
	
	@Override
	public void lib39Phantom$scheduleRenderUpdate(BlockPos pos) {
		// state arguments aren't used, so don't waste time retrieving information
		worldRenderer.updateBlock(this, pos, null, null, 8);
	}
	
}
