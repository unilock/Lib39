package com.unascribed.lib39.dessicant.mixin;

import java.util.List;

import net.minecraft.class_8567;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.unascribed.lib39.dessicant.DessicantData;
import com.unascribed.lib39.dessicant.api.SimpleLootBlock;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

@Mixin(AbstractBlock.class)
public class MixinAbstractBlock {

	// TODO [jas]: Pre 1.20

//	@Inject(at=@At("RETURN"), method="getDroppedStacks", cancellable=true)
//	public void getDroppedStacks(BlockState state, LootContext.Builder builder, CallbackInfoReturnable<List<ItemStack>> ci) {
//		if (ci.getReturnValue().isEmpty()) {
//			AbstractBlock self = (AbstractBlock)(Object)this;
//			Identifier id = self.getLootTableId();
//			if (id == LootTables.EMPTY) {
//				return;
//			}
//			LootContext lootContext = builder.parameter(LootContextParameters.BLOCK_STATE, state).build(LootContextTypes.BLOCK);
//			ServerWorld serverWorld = lootContext.getWorld();
//			LootTable lootTable = serverWorld.getServer().getLootManager().getTable(id);
//			if (lootTable == LootTable.EMPTY && DessicantData.optedInNamespaces.contains(id.getNamespace()) && (self instanceof SimpleLootBlock || self.asItem() != Items.AIR)) {
//				ItemStack loot;
//				if (self instanceof SimpleLootBlock slb) {
//					loot = slb.getLoot(state);
//				} else {
//					loot = new ItemStack(self.asItem());
//				}
//				ci.setReturnValue(ImmutableList.of(loot));
//			}
//		}
//	}

	@Inject(at=@At("RETURN"), method="method_9560", cancellable=true)
	public void getDroppedStacks(BlockState state, class_8567.class_8568 builder, CallbackInfoReturnable<List<ItemStack>> ci) {
		if (ci.getReturnValue().isEmpty()) {
			AbstractBlock self = (AbstractBlock)(Object)this;
			Identifier id = self.getLootTableId();
			if (id == LootTables.EMPTY) {
				return;
			}
			class_8567 lootContext = builder.method_51874(LootContextParameters.BLOCK_STATE, state).method_51875(LootContextTypes.BLOCK);
			ServerWorld serverWorld = lootContext.method_51863();
			LootTable lootTable = serverWorld.getServer().getLootManager().getLootTable(id);
			if (lootTable == LootTable.EMPTY && DessicantData.optedInNamespaces.contains(id.getNamespace()) && (self instanceof SimpleLootBlock || self.asItem() != Items.AIR)) {
				ItemStack loot;
				if (self instanceof SimpleLootBlock slb) {
					loot = slb.getLoot(state);
				} else {
					loot = new ItemStack(self.asItem());
				}
				ci.setReturnValue(ImmutableList.of(loot));
			}
		}
	}
	
}
