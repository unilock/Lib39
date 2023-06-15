package com.unascribed.lib39.machination.recipe;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import net.minecraft.registry.DynamicRegistryManager;
import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import com.unascribed.lib39.core.P39;
import com.unascribed.lib39.machination.Lib39Machination;
import com.unascribed.lib39.machination.ingredient.FluidIngredient;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class SoakingRecipe implements Recipe<Inventory> {

	protected final Identifier id;
	protected final String group;
	protected final Either<ItemStack, DefaultedList<Ingredient>> ingredients;
	protected final FluidIngredient catalyst;
	protected final Either<ItemStack, BlockState> result;
	protected final int time;
	protected final int multiDelay;
	protected final @Nullable SoundEvent sound;

	public SoakingRecipe(Identifier id, String group, Either<ItemStack, DefaultedList<Ingredient>> ingredients, FluidIngredient catalyst, Either<ItemStack, BlockState> result, int time, int multiDelay, SoundEvent sound) {
		this.id = id;
		this.group = group;
		this.ingredients = ingredients;
		this.catalyst = catalyst;
		this.result = result;
		this.time = time;
		this.multiDelay = multiDelay;
		this.sound = sound;
	}

	public FluidIngredient getCatalyst() {
		return catalyst;
	}

	public int getTime() {
		return time;
	}
	
	public int getMultiDelay() {
		return multiDelay;
	}
	
	public @Nullable SoundEvent getSound() {
		return sound;
	}
	
	public Either<ItemStack, BlockState> getResult() {
		return result;
	}

	@Override
	public boolean matches(Inventory inv, World world) {
		return false;
	}

	// Both craft() methods override the same, depending on version.

	// Pre 1.20
	public ItemStack craft(Inventory inv) {
		return getOutput().copy();
	}

	// 1.20
	public ItemStack method_8116(Inventory inv, DynamicRegistryManager manager) {
		return getOutput().copy();
	}

	// Both getOutput() methods here override the same, depending on version.

	// Pre 1.20
	public ItemStack getOutput() {
		return result.map(is -> is, bs -> new ItemStack(bs.getBlock()));
	}

	// 1.20
	public ItemStack method_8110(DynamicRegistryManager registryManager) {
		return getOutput();
	}

	@Override
	public boolean fits(int width, int height) {
		return false;
	}

	@Override
	public DefaultedList<Ingredient> getIngredients() {
		return ingredients.right().orElse(DefaultedList.of());
	}
	
	public Either<ItemStack, DefaultedList<Ingredient>> getSoakingIngredients() {
		return ingredients;
	}
	
	@Override
	public String getGroup() {
		return group;
	}

	@Override
	public Identifier getId() {
		return id;
	}
	
	@Override
	public boolean isIgnoredInRecipeBook() {
		return true;
	}

	@Override
	public RecipeType<?> getType() {
		return Lib39Machination.RecipeTypes.SOAKING;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return Lib39Machination.RecipeSerializers.SOAKING;
	}
	
	public static class Serializer implements RecipeSerializer<SoakingRecipe> {

		@Override
		public SoakingRecipe read(Identifier id, JsonObject obj) {
			String group = JsonHelper.getString(obj, "group", "");
			Either<ItemStack, DefaultedList<Ingredient>> ingredients;
			if (obj.has("ingredients")) {
				ingredients = Either.right(DefaultedList.copyOf(Ingredient.EMPTY, StreamSupport.stream(obj.get("ingredients").getAsJsonArray().spliterator(), false)
						.map(Ingredient::method_52177)
						.collect(Collectors.toList()).toArray(new Ingredient[0])));
			} else if (obj.has("single_ingredient")) {
				ingredients = Either.left(ShapedRecipe.outputFromJson(obj.getAsJsonObject("single_ingredient")));
			} else {
				throw new RuntimeException("Soaking recipes must define ingredients or single_ingredient");
			}
			FluidIngredient catalyst = FluidIngredient.fromJson(obj.get("catalyst"));
			Either<ItemStack, BlockState> result;
			JsonObject resultJson = obj.getAsJsonObject("result");
			if (resultJson.has("item")) {
				result = Either.left(ShapedRecipe.outputFromJson(resultJson));
			} else {
				result = Either.right(P39.parsing().parseBlockState(resultJson.get("block").getAsString()));
			}
			int time = JsonHelper.getInt(obj, "time", 0);
			int multiDelay = JsonHelper.getInt(obj, "multiDelay", 1);
			String soundId = JsonHelper.getString(obj, "sound", null);
			SoundEvent sound = null;
			var r = P39.registries();
			if (soundId != null) {
				sound = r.get(r.soundEvent(), new Identifier(soundId));
			}
			return new SoakingRecipe(id, group, ingredients, catalyst, result, time, multiDelay, sound);
		}

		@Override
		public SoakingRecipe read(Identifier id, PacketByteBuf buf) {
			String group = buf.readString();
			int ingredientCount = buf.readVarInt();
			Either<ItemStack, DefaultedList<Ingredient>> ingredients;
			if (ingredientCount == 0) {
				ingredients = Either.left(buf.readItemStack());
			} else {
				DefaultedList<Ingredient> ingredientsLi = DefaultedList.ofSize(ingredientCount, Ingredient.EMPTY);
				for (int i = 0; i < ingredientCount; i++) {
					ingredientsLi.set(i, Ingredient.fromPacket(buf));
				}
				ingredients = Either.right(ingredientsLi);
			}
			FluidIngredient catalyst = FluidIngredient.read(buf);
			Either<ItemStack, BlockState> result;
			if (buf.readBoolean()) {
				result = Either.left(buf.readItemStack());
			} else {
				result = Either.right(Block.getStateFromRawId(buf.readVarInt()));
			}
			int time = buf.readVarInt();
			int multiDelay = buf.readVarInt();
			SoundEvent sound = null;
			if (buf.readBoolean()) {
				var r = P39.registries();
				sound = r.get(r.soundEvent(), buf.readVarInt());
			}
			return new SoakingRecipe(id, group, ingredients, catalyst, result, time, multiDelay, sound);
		}

		@Override
		public void write(PacketByteBuf buf, SoakingRecipe recipe) {
			buf.writeString(recipe.group);
			buf.writeVarInt(recipe.ingredients.right().map(DefaultedList::size).orElse(0));
			recipe.ingredients
				.ifLeft(buf::writeItemStack)
				.ifRight(is -> is.forEach(i -> i.write(buf)));
			recipe.catalyst.write(buf);
			buf.writeBoolean(recipe.result.left().isPresent());
			recipe.getResult()
				.ifLeft(buf::writeItemStack)
				.ifRight(bs -> buf.writeVarInt(Block.STATE_IDS.getRawId(bs)));
			buf.writeVarInt(recipe.time);
			buf.writeVarInt(recipe.multiDelay);
			buf.writeBoolean(recipe.sound != null);
			var r = P39.registries();
			if (recipe.sound != null) buf.writeVarInt(r.getRawId(r.soundEvent(), recipe.sound));
		}
		
	}

}
