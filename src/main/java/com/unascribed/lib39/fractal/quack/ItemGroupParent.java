package com.unascribed.lib39.fractal.quack;

import java.util.List;

import com.unascribed.lib39.fractal.api.ItemSubGroup;

public interface ItemGroupParent {

	List<ItemSubGroup> lib39Fractal$getChildren();
	ItemSubGroup lib39Fractal$getSelectedChild();
	void lib39Fractal$setSelectedChild(ItemSubGroup group);
	
}
