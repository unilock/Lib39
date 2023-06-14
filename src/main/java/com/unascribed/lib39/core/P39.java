package com.unascribed.lib39.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.function.Supplier;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexBuffer;

import com.google.common.base.Suppliers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.resource.pack.metadata.ResourceMetadataReader;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.WorldChunk;

/**
 * <b>NOT API. Do not use.</b>
 */
// P39 = Port39
public class P39 {

	public interface Tag<T> {
		Iterable<T> getAll();
		boolean has(T t);
	}
	
	interface Factory {
		P39.MetaPort meta();
		P39.TextPort text();
		P39.WorldsPort worlds();
		P39.RegistriesPort registries();
		P39.ResourcesPort resources();
		P39.ParsingPort parsing();
		@Environment(EnvType.CLIENT)
		P39.RenderingPort rendering();
		@Environment(EnvType.CLIENT)
		P39.ScreenPort screens();
	}
	
	// This is split up to avoid loading classes too early

	private static final Supplier<MetaPort> META = Suppliers.memoize(P39Impl.FACTORY::meta);
	private static final Supplier<TextPort> TEXT = Suppliers.memoize(P39Impl.FACTORY::text);
	private static final Supplier<WorldsPort> WORLDS = Suppliers.memoize(P39Impl.FACTORY::worlds);
	private static final Supplier<RegistriesPort> REGISTRIES = Suppliers.memoize(P39Impl.FACTORY::registries);
	private static final Supplier<ResourcesPort> RESOURCES = Suppliers.memoize(P39Impl.FACTORY::resources);
	private static final Supplier<ParsingPort> PARSING = Suppliers.memoize(P39Impl.FACTORY::parsing);
	private static final Supplier<RenderingPort> RENDERING;
	private static final Supplier<ScreenPort> SCREENS;
	
	static {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			RENDERING = Suppliers.memoize(P39Impl.FACTORY::rendering);
			SCREENS = Suppliers.memoize(P39Impl.FACTORY::screens);
		} else {
			RENDERING = null;
			SCREENS = null;
		}
	}

	public static P39.MetaPort meta() {
		return META.get();
	}

	public static P39.TextPort text() {
		return TEXT.get();
	}

	public static P39.WorldsPort worlds() {
		return WORLDS.get();
	}

	public static P39.RegistriesPort registries() {
		return REGISTRIES.get();
	}

	public static P39.ResourcesPort resources() {
		return RESOURCES.get();
	}

	public static P39.ParsingPort parsing() {
		return PARSING.get();
	}

	@Environment(EnvType.CLIENT)
	public static P39.RenderingPort rendering() {
		return RENDERING.get();
	}

	@Environment(EnvType.CLIENT)
	public static P39.ScreenPort screens() {
		return SCREENS.get();
	}

	
	public interface MetaPort {
		String target();
	}
	
	public interface TextPort {
		MutableText literal(String text);
		MutableText translatable(String key, Object... args);
	}
	
	public interface WorldsPort {
		void playSound(World world, PlayerEntity except, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch);
		Iterable<WorldChunk> getLoadedChunks(ServerWorld world);
		boolean isReceivingRedstonePower(World world, BlockPos pos);
	}
	
	public interface RegistriesPort {
		Registry<SoundEvent> soundEvent();
		Registry<Item> item();
		Registry<Block> block();
		Registry<Fluid> fluid();
		Registry<StatusEffect> statusEffect();
		Registry<RecipeType<?>> recipeType();
		Registry<RecipeSerializer<?>> recipeSerializer();
		
		RegistryKey<Registry<Biome>> biomeRegistryKey();
		
		<T> Tag<T> getTag(Registry<T> registry, Identifier id);
		<T> void register(Registry<T> registry, Identifier id, T value);

		<T> T get(Registry<T> registry, Identifier id);
		<T> T get(Registry<T> registry, int rawId);
		<T> Optional<T> getOrEmpty(Registry<T> registry, Identifier id);
		
		<T> Identifier getId(Registry<T> registry, T t);
		<T> int getRawId(Registry<T> registry, T t);
		
		SoundEvent createSoundEvent(Identifier id);
	}
	
	public interface ResourcesPort {
		@Nullable Resource get(ResourceFactory factory, Identifier id) throws IOException;
		<T> @Nullable T readMetadata(Resource resource, ResourceMetadataReader<T> reader) throws IOException;
		InputStream open(Resource resource) throws IOException;
	}
	
	public interface ParsingPort {
		BlockState parseBlockState(String str);
	}

	@Environment(EnvType.CLIENT)
	public interface RenderingPort {
		void rotate(MatrixStack matrices, float deg, float x, float y, float z);
		double[] transform(MatrixStack matrices, double x, double y, double z);
		
		Iterable<BakedQuad> getQuads(BakedModel bm, BlockState state, Direction face, World world);
		
		void upload(VertexBuffer vb, BufferBuilder vc);
		void draw(VertexBuffer buf, MatrixStack matrices, ShaderProgram shader);
		void drawTexture(MatrixStack matrices, int x, int y, int width, int height, float u, float v, int regionWidth, int regionHeight, int textureWidth, int textureHeight);
		void renderItem(ItemStack stack, int light, int overlay, MatrixStack matrices, VertexConsumerProvider vcp, int seed);
	}

	@Environment(EnvType.CLIENT)
	public interface ScreenPort {
		ItemGroup getSelectedItemGroup(CreativeInventoryScreen screen);
	}
	
	private P39() {}
	
}
