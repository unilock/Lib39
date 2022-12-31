package com.unascribed.lib39.fractal.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.unascribed.lib39.core.mixinsupport.AutoMixinEligible;
import com.unascribed.lib39.fractal.api.ItemSubGroup;
import com.unascribed.lib39.fractal.quack.ItemGroupParent;

import com.google.common.collect.Lists;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

@Mixin(ItemGroup.class)
@AutoMixinEligible(unlessConfigSet="platform 1.19.3")
public class MixinItemGroup implements ItemGroupParent {

	private final List<ItemSubGroup> lib39Fractal$children = Lists.newArrayList();
	private ItemSubGroup lib39Fractal$selectedChild = null;

	@Inject(at=@At("HEAD"), method={"appendStacks","method_7738"}, remap=false, cancellable=true)
	public void lib39$appendStacksHead(DefaultedList<ItemStack> stacks, CallbackInfo ci) {
		if (lib39Fractal$selectedChild != null) {
			lib39Fractal$selectedChild.method_7738(stacks);
			ci.cancel();
		}
	}
	
	@Inject(at=@At("TAIL"), method={"appendStacks","method_7738"}, remap=false, cancellable=true)
	public void appendStacksTail(DefaultedList<ItemStack> stacks, CallbackInfo ci) {
		if (lib39Fractal$children != null) {
			for (ItemSubGroup child : lib39Fractal$children) {
				child.method_7738(stacks);
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

	// from Polymer
	private static ItemGroup[] LIB39FRACTAL$GROUPS_OLD;
	@Mutable @Shadow @Final
	public static ItemGroup[] field_7921;

	@Inject(at=@At(value="FIELD", target="net/minecraft/item/ItemGroup.field_7921:[Lnet/minecraft/item/ItemGroup;", shift=Shift.BEFORE),
			method="<init>", require=0)
	private void lib39Fractal$replaceArrayToSkipAdding(int index, String id, CallbackInfo ci) {
		Object self = this;
		if (self instanceof ItemSubGroup) {
			LIB39FRACTAL$GROUPS_OLD = field_7921;
			field_7921 = new ItemGroup[1];
		}
	}

	@Inject(at=@At(value="FIELD", target="net/minecraft/item/ItemGroup.GROUPS:[Lnet/minecraft/item/ItemGroup;", shift=Shift.BEFORE),
			method="<init>", require=0)
	private void lib39Fractal$replaceArrayToSkipAddingDeobf(int index, String id, CallbackInfo ci) {
		Object self = this;
		if (self instanceof ItemSubGroup) {
			LIB39FRACTAL$GROUPS_OLD = field_7921;
			field_7921 = new ItemGroup[1];
		}
	}

	@Inject(at=@At("TAIL"),
			method="<init>")
	private void lib39Fractal$unreplaceArray(int index, String id, CallbackInfo ci) {
		Object self = this;
		if (self instanceof ItemSubGroup) {
			field_7921 = LIB39FRACTAL$GROUPS_OLD;
		}
	}
	
}
