package com.unascribed.lib39.machination;

import com.unascribed.lib39.core.P39;
import com.unascribed.lib39.core.api.AutoRegistry;
import com.unascribed.lib39.machination.logic.SoakingHandler;
import com.unascribed.lib39.machination.recipe.PistonSmashingRecipe;
import com.unascribed.lib39.machination.recipe.SoakingRecipe;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;

public class Lib39Machination implements ModInitializer {

	public static class RecipeTypes {
		public static final RecipeType<PistonSmashingRecipe> PISTON_SMASHING = create("piston_smashing");
		public static final RecipeType<SoakingRecipe> SOAKING = create("soaking");
	}
	
	public static class RecipeSerializers {
		public static final PistonSmashingRecipe.Serializer PISTON_SMASHING = new PistonSmashingRecipe.Serializer();
		public static final SoakingRecipe.Serializer SOAKING = new SoakingRecipe.Serializer();
	}

	private static final AutoRegistry autoreg = AutoRegistry.of("lib39");
	
	@Override
	public void onInitialize() {
		autoreg.autoRegister(P39.registries().recipeType(), RecipeTypes.class, RecipeType.class);
		autoreg.autoRegister(P39.registries().recipeSerializer(), RecipeSerializers.class, RecipeSerializer.class);
		
		ServerTickEvents.START_WORLD_TICK.register(SoakingHandler::startServerWorldTick);
		ServerTickEvents.END_WORLD_TICK.register(SoakingHandler::endServerWorldTick);
	}
	
	private static <T extends Recipe<?>> RecipeType<T> create(String id) {
		String fullId = "lib39:"+id;
		return new RecipeType<T>() {
			@Override
			public String toString() {
				return fullId;
			}
		};
	}
	
}
