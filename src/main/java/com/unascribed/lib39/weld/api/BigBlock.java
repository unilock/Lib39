package com.unascribed.lib39.weld.api;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

import com.unascribed.lib39.core.api.util.ReflectionHelper;
import net.minecraft.class_8567;
import org.jetbrains.annotations.Nullable;

import com.unascribed.lib39.core.P39;
import com.unascribed.lib39.weld.Lib39Weld;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext.Builder;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public abstract class BigBlock extends Block {
	private static final MethodHandle getDroppedStacks = ReflectionHelper.of(MethodHandles.lookup(), Block.class)
			.tryObtainVirtual(MethodType.methodType(List.class, BlockState.class, Builder.class),
					"getDroppedStacks", "method_9560");

	public final Optional<IntProperty> xProp, yProp, zProp;
	private final int xSize, ySize, zSize;
	
	public BigBlock(IntProperty x, IntProperty y, IntProperty z, Settings settings) {
		super(settings);
		this.xSize = x == null ? 1 : Iterables.getLast(x.getValues())+1;
		this.ySize = y == null ? 1 : Iterables.getLast(y.getValues())+1;
		this.zSize = z == null ? 1 : Iterables.getLast(z.getValues())+1;
		xProp = Optional.ofNullable(x);
		yProp = Optional.ofNullable(y);
		zProp = Optional.ofNullable(z);
	}
	
	protected BlockState copyState(BlockState us, BlockState neighbor) {
		return us;
	}
	
	public int getX(BlockState state) {
		return xProp.map(state::get).orElse(0);
	}
	
	public int getY(BlockState state) {
		return yProp.map(state::get).orElse(0);
	}
	
	public int getZ(BlockState state) {
		return zProp.map(state::get).orElse(0);
	}
	
	/** @deprecated Use the {@link #getXSize(BlockState) state aware overload} */
	@Deprecated
	public int getXSize() { return getXSize(getDefaultState()); }
	/** @deprecated Use the {@link #getYSize(BlockState) state aware overload} */
	@Deprecated
	public int getYSize() { return getYSize(getDefaultState()); }
	/** @deprecated Use the {@link #getZSize(BlockState) state aware overload} */
	@Deprecated
	public int getZSize() { return getZSize(getDefaultState()); }
	
	public int getXSize(BlockState state) {
		return xSize;
	}

	public int getYSize(BlockState state) {
		return ySize;
	}

	public int getZSize(BlockState state) {
		return zSize;
	}

	public BlockState setX(BlockState state, int x) {
		return xProp.map(p -> state.with(p, x)).orElse(state);
	}
	
	public BlockState setY(BlockState state, int y) {
		return yProp.map(p -> state.with(p, y)).orElse(state);
	}
	
	public BlockState setZ(BlockState state, int z) {
		return zProp.map(p -> state.with(p, z)).orElse(state);
	}
	
	public BlockState set(BlockState state, int x, int y, int z) {
		return setX(setY(setZ(state, z), y), x);
	}

	public @Nullable BlockState getExpectedNeighbor(BlockState state, Direction dir) {
		int x = getX(state)+dir.getOffsetX();
		int y = getY(state)+dir.getOffsetY();
		int z = getZ(state)+dir.getOffsetZ();
		if (x < 0 || y < 0 || z < 0) return null;
		if (x >= getXSize(state) || y >= getYSize(state) || z >= getZSize(state)) return null;
		return set(state, x, y, z);
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		BlockState expected = getExpectedNeighbor(state, direction);
		if (expected == null) return state;
		if (newState.isOf(this)) {
			expected = copyState(expected, newState);
		}
		if (newState != expected) {
			if (this instanceof Waterloggable && state.get(Properties.WATERLOGGED)) {
				return Blocks.WATER.getDefaultState();
			}
			return Blocks.AIR.getDefaultState();
		}
		return copyState(state, newState);
	}
	
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		super.onStateReplaced(state, world, pos, newState, moved);
		world.scheduleBlockTick(pos, this, 1);
	}
	
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		super.onBlockAdded(state, world, pos, oldState, notify);
		world.scheduleBlockTick(pos, this, 1);
	}
	
	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (player.isCreative()) {
			BlockPos origin = pos.add(-getX(state), -getY(state), -getZ(state));
			for (int y = 0; y < getYSize(state); y++) {
				for (int x = 0; x < getXSize(state); x++) {
					for (int z = 0; z < getZSize(state); z++) {
						world.breakBlock(origin.add(x, y, z), false, player);
					}
				}
			}
		} else if (world.isClient && getX(state) == 0 && getY(state) == 0 && getZ(state) == 0) {
			BlockSoundGroup sg = getSoundGroup(state);
			world.playSound(pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, sg.getBreakSound(), SoundCategory.BLOCKS, (sg.getVolume() + 1) / 2f, sg.getPitch() * 0.8f, false);
		}
	}
	
	@Override
	public BlockSoundGroup getSoundGroup(BlockState state) {
		BlockSoundGroup sg = super.getSoundGroup(state);
		if (getX(state) == 0 && getY(state) == 0 && getZ(state) == 0) {
			return sg;
		}
		return new BlockSoundGroup(sg.volume, sg.pitch, Lib39Weld.SILENCE, sg.getStepSound(), sg.getPlaceSound(), sg.getHitSound(), sg.getFallSound());
	}
	
	@Override
	public void method_9588(BlockState state, ServerWorld world, BlockPos pos, RandomGenerator random) {
		_scheduledTick(state, world, pos);
	}
	
	// @Override
	public void method_9588(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		_scheduledTick(state, world, pos);
	}
	
	private void _scheduledTick(BlockState state, ServerWorld world, BlockPos pos) {
		for (Direction dir : Direction.values()) {
			BlockState expected = getExpectedNeighbor(state, dir);
			if (expected != null) {
				BlockState have = world.getBlockState(pos.offset(dir));
				if (have != expected) {
					world.breakBlock(pos, false);
					return;
				}
			}
		}
	}

	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.BLOCK;
	}
	
//	Pre 1.20 override
	public List<ItemStack> getDroppedStacks(BlockState state, Builder builder) {
		if (getX(state) != 0 || getY(state) != 0 || getZ(state) != 0) return ImmutableList.of();
		try {
			return (List<ItemStack>) getDroppedStacks.invoke(state, builder);
		} catch (Throwable e) {
			throw new RuntimeException("No Exception expected here", e);
		}
	}

	// 1.20+ override
	@Override
	public List<ItemStack> method_9560(BlockState state, class_8567.class_8568 builder) {
		if (getX(state) != 0 || getY(state) != 0 || getZ(state) != 0) return ImmutableList.of();
		return super.method_9560(state, builder);
	}
	
	public void alterDroppedEntity(BlockPos pos, BlockState state, ItemEntity entity) {
		var rnd = ThreadLocalRandom.current();
		double x = pos.getX()-getX(state)+(rnd.nextFloat() * (getXSize(state)/2D) + (getXSize(state)/4D));
		double y = pos.getY()-getY(state)+(rnd.nextFloat() * (getYSize(state)/2D) + (getYSize(state)/4D));
		double z = pos.getZ()-getZ(state)+(rnd.nextFloat() * (getZSize(state)/2D) + (getZSize(state)/4D));
		entity.setPosition(x, y, z);
	}

	public static void playSound(World world, PlayerEntity player, BlockPos pos, BlockState state, SoundEvent event, SoundCategory cat, float vol, float pitch) {
		if (!(state.getBlock() instanceof BigBlock)) {
			world.playSound(player, pos, event, cat, vol, pitch);
			return;
		}
		BigBlock b = (BigBlock)state.getBlock();
		double x = (pos.getX()-b.getX(state))+(b.getXSize(state)/2D);
		double y = (pos.getY()-b.getY(state))+(b.getYSize(state)/2D);
		double z = (pos.getZ()-b.getZ(state))+(b.getZSize(state)/2D);
		P39.worlds().playSound(world, player, x, y, z, event, cat, vol, pitch);
	}

	public static boolean isReceivingRedstonePower(World world, BlockPos pos, BlockState state) {
		if (!(state.getBlock() instanceof BigBlock)) {
			return P39.worlds().isReceivingRedstonePower(world, pos);
		}
		BigBlock b = (BigBlock)state.getBlock();
		int oX = pos.getX()-b.getX(state);
		int oY = pos.getY()-b.getY(state);
		int oZ = pos.getZ()-b.getZ(state);
		BlockPos.Mutable bp = new BlockPos.Mutable();
		for (int x = 0; x < b.getXSize(state); x++) {
			for (int y = 0; y < b.getYSize(state); y++) {
				for (int z = 0; z < b.getZSize(state); z++) {
					bp.set(oX+x, oY+y, oZ+z);
					if (P39.worlds().isReceivingRedstonePower(world, bp)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean anyNeighborsMatch(World w, BlockPos pos, BlockState state, Predicate<BlockState> pred) {
		if (!(state.getBlock() instanceof BigBlock)) {
			return false;
		}
		BigBlock b = (BigBlock)state.getBlock();
		int x = b.getX(state);
		int y = b.getY(state);
		int z = b.getZ(state);
		for (Direction d : Direction.values()) {
			int nX = x+d.getOffsetX();
			int nY = y+d.getOffsetY();
			int nZ = z+d.getOffsetZ();
			if (nX < 0 || nX >= b.getXSize(state)) continue;
			if (nY < 0 || nY >= b.getYSize(state)) continue;
			if (nZ < 0 || nZ >= b.getZSize(state)) continue;
			BlockPos bp = pos.offset(d);
			if (!w.getBlockState(bp).isOf(state.getBlock())) continue;
			if (pred.test(w.getBlockState(bp))) {
				return true;
			}
		}
		return false;
	}
	
}
