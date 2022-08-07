package com.unascribed.lib39.fractal.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.unascribed.lib39.fractal.api.ItemSubGroup;
import com.unascribed.lib39.fractal.quack.ItemGroupParent;

import com.google.common.collect.Lists;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

@Mixin(ItemGroup.class)
public class MixinItemGroup implements ItemGroupParent {

	private final List<ItemSubGroup> lib39Fractal$children = Lists.newArrayList();
	private ItemSubGroup lib39Fractal$selectedChild = null;
	
	@Inject(at=@At("HEAD"), method="appendStacks", cancellable=true)
	public void appendStacksHead(DefaultedList<ItemStack> stacks, CallbackInfo ci) {
		if (lib39Fractal$selectedChild != null) {
			lib39Fractal$selectedChild.appendStacks(stacks);
			ci.cancel();
		}
	}
	
	@Inject(at=@At("TAIL"), method="appendStacks", cancellable=true)
	public void appendStacksTail(DefaultedList<ItemStack> stacks, CallbackInfo ci) {
		if (lib39Fractal$children != null) {
			for (ItemSubGroup child : lib39Fractal$children) {
				child.appendStacks(stacks);
			}
		}
	}
	
	@Override
	public List<ItemSubGroup> lib39Fractal$getChildren() {
		return lib39Fractal$children;
	}
	
	@Override
	public ItemSubGroup lib39Fractal$getSelectedChild() {
		return lib39Fractal$selectedChild;
	}
	
	@Override
	public void lib39Fractal$setSelectedChild(ItemSubGroup group) {
		lib39Fractal$selectedChild = group;
	}
	
}
