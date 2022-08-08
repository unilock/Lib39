package com.unascribed.lib39.phantom.mixin;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.StampedLock;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.unascribed.lib39.phantom.PhaseQueueEntry;
import com.unascribed.lib39.phantom.quack.PhantomWorld;

import com.google.common.collect.Maps;

import net.minecraft.block.BlockState;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

@Mixin(World.class)
public abstract class MixinWorld implements PhantomWorld {

	private final StampedLock lib39Phantom$phaseLock = new StampedLock();
	
	private final Map<BlockPos, AtomicInteger> lib39Phantom$phase = Maps.newHashMap();
	private final Map<BlockPos, DamageSource> lib39Phantom$phaseSources = Maps.newHashMap();
	private final Map<BlockPos, PhaseQueueEntry> lib39Phantom$phaseQueue = Maps.newHashMap();
	
	
	@Shadow
	public abstract BlockState getBlockState(BlockPos pos);
	@Shadow
	public abstract void updateListeners(BlockPos pos, BlockState oldState, BlockState newState, int flags);

	@Inject(at=@At("HEAD"), method="tickBlockEntities()V")
	public void lib39Phantom$tickBlockEntities(CallbackInfo ci) {
		boolean writing = false;
		long stamp = lib39Phantom$phaseLock.readLock();
		try {
			if (!lib39Phantom$phase.isEmpty()) {
				Iterator<Map.Entry<BlockPos, AtomicInteger>> iter = lib39Phantom$phase.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<BlockPos, AtomicInteger> en = iter.next();
					if (en.getValue().decrementAndGet() <= 0) {
						if (!writing) {
							long wstamp = lib39Phantom$phaseLock.tryConvertToWriteLock(stamp);
							if (wstamp == 0) {
								lib39Phantom$phaseLock.unlockRead(stamp);
								stamp = lib39Phantom$phaseLock.writeLock();
							} else {
								stamp = wstamp;
							}
							writing = true;
						}
						iter.remove();
						lib39Phantom$phaseSources.remove(en.getKey());
						lib39Phantom$scheduleRenderUpdate(en.getKey());
					}
				}
			}
			if (!lib39Phantom$phaseQueue.isEmpty()) {
				Iterator<Map.Entry<BlockPos, PhaseQueueEntry>> iter = lib39Phantom$phaseQueue.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<BlockPos, PhaseQueueEntry> en = iter.next();
					if (en.getValue().delayLeft.decrementAndGet() <= 0) {
						if (!writing) {
							long wstamp = lib39Phantom$phaseLock.tryConvertToWriteLock(stamp);
							if (wstamp == 0) {
								lib39Phantom$phaseLock.unlockRead(stamp);
								stamp = lib39Phantom$phaseLock.writeLock();
							} else {
								stamp = wstamp;
							}
							writing = true;
						}
						iter.remove();
						lib39Phantom$addPhaseBlock(en.getKey(), en.getValue().lifetime, -1, en.getValue().customSrc);
					}
				}
			}
		} finally {
			lib39Phantom$phaseLock.unlock(stamp);
		}
	}
	
	@Override
	public void lib39Phantom$scheduleRenderUpdate(BlockPos pos) {
	}
	
	private final ThreadLocal<BlockPos.Mutable> lib39Phantom$scratchPos = ThreadLocal.withInitial(BlockPos.Mutable::new);
	
	@Override
	public boolean lib39Phantom$isPhased(ChunkPos chunkPos, BlockPos pos) {
		if (lib39Phantom$unmasked) return false;
		return lib39Phantom$isPhased(lib39Phantom$scratchPos.get().set(chunkPos.getStartX(), 0, chunkPos.getStartZ()).move(pos.getX()&15, pos.getY(), pos.getZ()&15));
	}
	
	@Override
	public boolean lib39Phantom$isPhased(int x, int y, int z) {
		return lib39Phantom$isPhased(lib39Phantom$scratchPos.get().set(x, y, z));
	}
	
	@Override
	public boolean lib39Phantom$isPhased(BlockPos pos) {
		long stamp = lib39Phantom$phaseLock.tryOptimisticRead();
		boolean phased = lib39Phantom$phase.containsKey(pos);
		if (!lib39Phantom$phaseLock.validate(stamp)) {
			stamp = lib39Phantom$phaseLock.readLock();
			try {
				phased = lib39Phantom$phase.containsKey(pos);
			} finally {
				lib39Phantom$phaseLock.unlockRead(stamp);
			}
		}
		return phased;
	}
	
	@Override
	public @Nullable DamageSource lib39Phantom$getDamageSource(BlockPos pos) {
		long stamp = lib39Phantom$phaseLock.readLock();
		try {
			return lib39Phantom$phaseSources.get(pos);
		} finally {
			lib39Phantom$phaseLock.unlockRead(stamp);
		}
	}

	@Override
	public void lib39Phantom$addPhaseBlock(BlockPos pos, int lifetime, int delay, DamageSource customSrc) {
		long stamp = lib39Phantom$phaseLock.writeLock();
		try {
			BlockPos imm = pos.toImmutable();
			if (delay <= 0) {
				lib39Phantom$phase.put(imm, new AtomicInteger(lifetime));
				if (customSrc != null) lib39Phantom$phaseSources.put(imm, customSrc);
				lib39Phantom$scheduleRenderUpdate(pos);
			} else {
				lib39Phantom$phaseQueue.put(imm, new PhaseQueueEntry(lifetime, delay, customSrc));
			}
		} finally {
			lib39Phantom$phaseLock.unlockWrite(stamp);
		}
	}

	@Override
	public void lib39Phantom$removePhaseBlock(BlockPos pos) {
		long stamp = lib39Phantom$phaseLock.writeLock();
		try {
			lib39Phantom$phase.remove(pos);
		} finally {
			lib39Phantom$phaseLock.unlockWrite(stamp);
		}
	}
	
	private boolean lib39Phantom$unmasked;
	
	@Override
	public void lib39Phantom$setUnmask(boolean unmask) {
		this.lib39Phantom$unmasked = unmask;
	}
	
}
