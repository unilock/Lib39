package com.unascribed.lib39.machination.emi;

import com.unascribed.lib39.machination.Lib39Machination;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class Lib39MachinationEmiPlugin implements EmiPlugin {
	
	public static final EmiRecipeCategory PISTON_SMASHING = category("piston_smashing", EmiStack.of(Items.PISTON));
	public static final EmiRecipeCategory SOAKING = category("soaking", EmiStack.of(Fluids.WATER));
	
	@Override
	public void register(EmiRegistry registry) {
		registry.addCategory(PISTON_SMASHING);
		registry.addCategory(SOAKING);
		
		registry.addWorkstation(PISTON_SMASHING, EmiStack.of(Items.PISTON));
		registry.addWorkstation(PISTON_SMASHING, EmiStack.of(Items.STICKY_PISTON));
		
		registry.getRecipeManager().listAllOfType(Lib39Machination.RecipeTypes.PISTON_SMASHING).stream()
				.map(EmiPistonSmashingRecipe::new)
				.forEach(registry::addRecipe);
		
		registry.getRecipeManager().listAllOfType(Lib39Machination.RecipeTypes.SOAKING).stream()
				.map(EmiSoakingRecipe::new)
				.forEach(registry::addRecipe);
	}
	
	private static EmiRecipeCategory category(String id, EmiRenderable icon) {
		EmiRecipeCategory cat = new EmiRecipeCategory(new Identifier("lib39", id), icon);
		cat.simplified = new EmiTexture(new Identifier("lib39-machination", "textures/gui/emi_simple/"+id+".png"), 0, 0, 16, 16, 16, 16, 16, 16);
		return cat;
	}

}
