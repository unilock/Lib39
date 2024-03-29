package com.unascribed.lib39.weld.api;

import com.google.common.primitives.Ints;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class BigBlockItem extends BlockItem {
	
	public BigBlockItem(BigBlock block, Settings settings) {
		super(block, settings);
	}

	@Override
	protected boolean place(ItemPlacementContext context, BlockState state) {
		if (!(state.getBlock() instanceof BigBlock)) return super.place(context, state);
		World w = context.getWorld();
		BigBlock b = (BigBlock)state.getBlock();
		BlockPos front;
		BlockPos back;
		// TODO this is bad and only really works for 1x*x1 and 2x*x2 blocks
		if (context.getSide().getAxis() == Direction.Axis.Y) {
			front = context.getBlockPos();
			back = front.offset(context.getPlayerFacing(), b.getXSize(state)-1);
		} else {
			back = context.getBlockPos();
			front = back.offset(context.getSide(), b.getZSize(state)-1);
		}
		BlockPos origin = new BlockPos(Ints.min(front.getX(), back.getX()), front.getY(), Ints.min(front.getZ(), back.getZ()));
		if (context.getSide() == Direction.DOWN) {
			origin = origin.down(b.getYSize(state)-1);
		}
		Box box = new Box(origin, origin.add(b.getXSize(state)-1, b.getYSize(state)-1, b.getZSize(state)-1));
		if (!w.getEntityCollisions(null, box).isEmpty()) {
			return false;
		}
		for (int p = 0; p < 2; p++) {
			for (int y = 0; y < b.getYSize(state); y++) {
				for (int x = 0; x < b.getXSize(state); x++) {
					for (int z = 0; z < b.getZSize(state); z++) {
						BlockPos bp = origin.add(x, y, z);
						ItemPlacementContext ctx = new ItemPlacementContext(context.getPlayer(), context.getHand(), context.getStack(),
								new BlockHitResult(context.getHitPos().add(x, y, z), context.getSide(), bp, context.hitsInsideBlock()));
						if (p == 0) {
							BlockState cur = w.getBlockState(bp);
							if (!cur.canReplace(ctx)) {
								return false;
							}
						} else if (p == 1) {
							if (!super.place(ctx, b.set(state, x, y, z))) {
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}

}
