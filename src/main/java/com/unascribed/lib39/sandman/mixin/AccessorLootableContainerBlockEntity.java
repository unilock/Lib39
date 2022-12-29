package com.unascribed.lib39.sandman.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.util.Identifier;

@Mixin(LootableContainerBlockEntity.class)
public interface AccessorLootableContainerBlockEntity {

	@Accessor("lootTableId")
	Identifier lib39Sandman$getLootTableId();
	
}
