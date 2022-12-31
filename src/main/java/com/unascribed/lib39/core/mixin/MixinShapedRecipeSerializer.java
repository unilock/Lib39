package com.unascribed.lib39.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.gson.JsonObject;
import com.unascribed.lib39.core.Lib39Mod;
import com.unascribed.lib39.core.P39;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;

@Mixin(ShapedRecipe.Serializer.class)
public class MixinShapedRecipeSerializer {

	@Inject(at=@At("RETURN"), method="read(Lnet/minecraft/util/Identifier;Lcom/google/gson/JsonObject;)Lnet/minecraft/recipe/ShapedRecipe;")
	public void lib39Core$read(Identifier identifier, JsonObject jsonObject, CallbackInfoReturnable<Recipe<?>> ci) {
		if (jsonObject.has("lib39:sound")) {
			var r = P39.registries();
			Lib39Mod.craftingSounds.put(identifier, r.get(r.soundEvent(), new Identifier(jsonObject.get("lib39:sound").getAsString())));
		} else {
			Lib39Mod.craftingSounds.remove(identifier);
		}
	}
	
}
