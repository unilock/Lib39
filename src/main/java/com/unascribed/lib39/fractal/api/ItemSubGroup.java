package com.unascribed.lib39.fractal.api;

import com.unascribed.lib39.fractal.quack.ItemGroupParent;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ItemSubGroup extends ItemGroup {
	// Will be replaced by a per-version implementation

	private ItemGroup parent;
	private int indexInParent;
	
	private ItemSubGroup(ItemGroup parent, Identifier id) {
		super(null, 0, null, null, null, null);
		this.parent = parent;
		ItemGroupParent igp = (ItemGroupParent)parent;
		this.indexInParent = igp.lib39Fractal$getChildren().size();
		igp.lib39Fractal$getChildren().add(this);
		if (igp.lib39Fractal$getSelectedChild() == null) {
			igp.lib39Fractal$setSelectedChild(this);
		}
	}
	
	public static ItemSubGroup create(ItemGroup parent, Identifier id) {
		return new ItemSubGroup(parent, id);
	}
	
	public ItemGroup getParent() {
		return parent;
	}
	
	public int getIndexInParent() {
		return indexInParent;
	}

	@Override
	public ItemStack getIcon() {
		return ItemStack.EMPTY;
	}
	
	// unmapped as it's missing from the intersection, but that's ok
	public ItemStack method_7750() {
		return ItemStack.EMPTY;
	}
}
