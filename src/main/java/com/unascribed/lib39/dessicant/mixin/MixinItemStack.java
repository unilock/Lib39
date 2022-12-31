package com.unascribed.lib39.dessicant.mixin;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.unascribed.lib39.core.P39;
import com.unascribed.lib39.dessicant.DessicantData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
@Mixin(ItemStack.class)
public class MixinItemStack {

	@Inject(at=@At(value="INVOKE", target="net/minecraft/item/Item.appendTooltip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Ljava/util/List;Lnet/minecraft/client/item/TooltipContext;)V"),
			method="getTooltip", locals=LocalCapture.CAPTURE_FAILHARD)
	public void lib39Dessicant$getTooltip(@Nullable PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> ci, List<Text> tooltip) {
		ItemStack self = (ItemStack)(Object)this;
		var r = P39.registries();
		if (DessicantData.optedInNamespaces.contains(r.getId(r.item(), self.getItem()).getNamespace())) {
			int i = 1;
			while (I18n.hasTranslation(self.getTranslationKey()+".tip."+i)) {
				tooltip.add(P39.text().translatable(self.getTranslationKey()+".tip."+i));
				i++;
			}
		}
	}
	
}
