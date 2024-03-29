package com.unascribed.lib39.machination.logic;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;

import com.unascribed.lib39.machination.Lib39Machination;
import com.unascribed.lib39.machination.Lib39Machination.RecipeTypes;
import com.unascribed.lib39.machination.quack.WetWorld;
import com.unascribed.lib39.machination.recipe.SoakingRecipe;

import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;

public class SoakingHandler {

	public static void startServerWorldTick(ServerWorld world) {
		((WetWorld)world).lib39Machination$getSoakingMap().clear();
	}
	
	public static void endServerWorldTick(ServerWorld world) {
		Multimap<BlockPos, ItemEntity> soakingMap = ((WetWorld)world).lib39Machination$getSoakingMap();
		Table<BlockPos, Fluid, Integer> timeTable = ((WetWorld)world).lib39Machination$getTimeTable();
		Iterator<Table.Cell<BlockPos, Fluid, Integer>> iter = timeTable.cellSet().iterator();
		while (iter.hasNext()) {
			Table.Cell<BlockPos, Fluid, Integer> cell = iter.next();
			if (!soakingMap.containsKey(cell.getRowKey())
					|| !world.isChunkLoaded(cell.getRowKey())
					|| world.getFluidState(cell.getRowKey()).getFluid() != cell.getColumnKey()) {
				iter.remove();
			}
		}
		for (Map.Entry<BlockPos, Collection<ItemEntity>> en : soakingMap.asMap().entrySet()) {
			FluidState fs = world.getFluidState(en.getKey());
			if (fs.isEmpty()) continue;
			Fluid f = fs.getFluid();
			Set<ItemEntity> unmatched = Sets.newHashSet(en.getValue());
			while (!unmatched.isEmpty()) {
				SoakingRecipe recipe = null;
				Set<ItemEntity> matched = Sets.newHashSet();
				for (SoakingRecipe sr : world.getServer().getRecipeManager().listAllOfType(Lib39Machination.RecipeTypes.SOAKING)) {
					if (sr.getCatalyst().test(f)) {
						boolean matchedAll = true;
						Set<ItemEntity> maybeMatched = Sets.newHashSet();
						var ing = sr.getSoakingIngredients();
						if (ing.left().isPresent()) {
							Iterator<ItemEntity> unmIter = unmatched.iterator();
							ItemStack is = ing.left().get();
							matchedAll = false;
							while (unmIter.hasNext()) {
								ItemEntity ie = unmIter.next();
								if (ItemStack.canCombine(is, ie.getStack()) && ie.getStack().getCount() >= is.getCount()) {
									maybeMatched.add(ie);
									matchedAll = true;
								}
							}
						} else {
							for (Ingredient i : ing.right().get()) {
								boolean ingredientMatched = false;
								Iterator<ItemEntity> unmIter = unmatched.iterator();
								while (unmIter.hasNext()) {
									ItemEntity ie = unmIter.next();
									if (!ie.getStack().isEmpty() && i.test(ie.getStack())) {
										maybeMatched.add(ie);
										ingredientMatched = true;
										break;
									}
								}
								if (!ingredientMatched) {
									matchedAll = false;
									break;
								}
							}
						}
						if (matchedAll) {
							recipe = sr;
							matched.addAll(maybeMatched);
							unmatched.removeAll(maybeMatched);
							break;
						}
					}
				}
				if (recipe != null) {
					if (recipe.getTime() != 0) {
						if (timeTable.row(en.getKey()).compute(f, (_f, i) -> i == null ? 0 : i+1) < recipe.getTime()) {
							continue;
						}
						timeTable.put(en.getKey(), f, recipe.getTime()-recipe.getMultiDelay());
					}
					int toCraft = 1;
					int perCraft = recipe.getSoakingIngredients().left().map(ItemStack::getCount).orElse(1);
					if (recipe.getMultiDelay() == 0 && recipe.getResult().left().isPresent()) {
						toCraft = 64;
						for (ItemEntity ie : matched) {
							toCraft = Math.min(ie.getStack().getCount()/perCraft, toCraft);
						}
					}
					final int toCraftf = toCraft;
					for (ItemEntity ie : matched) {
						ItemStack is = ie.getStack();
						is.decrement(perCraft);
						ie.setStack(is);
						if (is.isEmpty()) {
							ie.discard();
						}
					}
					recipe.getResult()
						.ifLeft(is -> {
							is = is.copy();
							is.setCount(toCraftf);
							Block.dropStack(world, en.getKey(), is);
						})
						.ifRight(bs -> world.setBlockState(en.getKey(), bs));
					if (recipe.getSound() != null) {
						world.playSound(null, en.getKey(), recipe.getSound(), SoundCategory.BLOCKS, 1, 1);
					}
				} else {
					// we're out of matching recipes
					break;
				}
			}
		}
	}
	
}
