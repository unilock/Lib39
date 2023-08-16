package com.unascribed.lib39.phantom.mixin;

import java.util.Iterator;
import java.util.concurrent.locks.StampedLock;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.unascribed.lib39.phantom.PhaseQueueEntry;
import com.unascribed.lib39.phantom.quack.PhantomWorld;

import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ReferenceMap;
import it.unimi.dsi.fastutil.longs.Long2ReferenceOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

@Mixin(World.class)
public abstract class MixinWorld implements PhantomWorld {

	private final StampedLock lib39Phantom$phaseLock = new StampedLock();
	
	private final Long2IntMap lib39Phantom$phase = new Long2IntOpenHashMap();
	private final Long2ReferenceMap<DamageSource> lib39Phantom$phaseSources = new Long2ReferenceOpenHashMap<>();
	private final Long2ReferenceMap<PhaseQueueEntry> lib39Phantom$phaseQueue = new Long2ReferenceOpenHashMap<>();
	
	
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
				Iterator<Long2IntMap.Entry> iter = lib39Phantom$phase.long2IntEntrySet().iterator();
				while (iter.hasNext()) {
					Long2IntMap.Entry en = iter.next();
					int v = en.getIntValue()-1;
					if (v <= 0) {
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
						lib39Phantom$phaseSources.remove(en.getLongKey());
						lib39Phantom$scheduleRenderUpdate(BlockPos.fromLong(en.getLongKey()));
						iter.remove();
					} else {
						en.setValue(v);
					}
				}
			}
			if (!lib39Phantom$phaseQueue.isEmpty()) {
				Iterator<Long2ReferenceMap.Entry<PhaseQueueEntry>> iter = lib39Phantom$phaseQueue.long2ReferenceEntrySet().iterator();
				while (iter.hasNext()) {
					Long2ReferenceMap.Entry<PhaseQueueEntry> en = iter.next();
					if (en.getValue().delayLeft-- <= 0) {
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
						lib39Phantom$addPhaseBlock(BlockPos.fromLong(en.getLongKey()), en.getValue().lifetime, -1, en.getValue().customSrc);
						iter.remove();
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
	
	@Override
	public boolean lib39Phantom$isPhased(ChunkPos chunkPos, BlockPos pos) {
		if (lib39Phantom$unmasked) return false;
		return lib39Phantom$isPhased(chunkPos.getStartX()+(pos.getX()&15), pos.getY(), chunkPos.getStartZ()+(pos.getZ()&15));
	}
	
	@Override
	public boolean lib39Phantom$isPhased(int x, int y, int z) {
		long pos = BlockPos.asLong(x, y, z);
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
	public boolean lib39Phantom$isPhased(BlockPos pos) {
		long l = pos.asLong();
		long stamp = lib39Phantom$phaseLock.tryOptimisticRead();
		boolean phased = lib39Phantom$phase.containsKey(l);
		if (!lib39Phantom$phaseLock.validate(stamp)) {
			stamp = lib39Phantom$phaseLock.readLock();
			try {
				phased = lib39Phantom$phase.containsKey(l);
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
			return lib39Phantom$phaseSources.get(pos.asLong());
		} finally {
			lib39Phantom$phaseLock.unlockRead(stamp);
		}
	}

	@Override
	public void lib39Phantom$addPhaseBlock(BlockPos pos, int lifetime, int delay, DamageSource customSrc) {
		long l = pos.asLong();
		long stamp = lib39Phantom$phaseLock.writeLock();
		try {
			if (delay <= 0) {
				lib39Phantom$phase.put(l, lifetime);
				if (customSrc != null) lib39Phantom$phaseSources.put(l, customSrc);
				lib39Phantom$scheduleRenderUpdate(pos);
			} else {
				lib39Phantom$phaseQueue.put(l, new PhaseQueueEntry(lifetime, delay, customSrc));
			}
		} finally {
			lib39Phantom$phaseLock.unlockWrite(stamp);
		}
	}

	@Override
	public void lib39Phantom$removePhaseBlock(BlockPos pos) {
		long stamp = lib39Phantom$phaseLock.writeLock();
		try {
			lib39Phantom$phase.remove(pos.asLong());
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
