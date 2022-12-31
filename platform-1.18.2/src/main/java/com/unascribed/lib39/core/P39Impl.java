package com.unascribed.lib39.core;

import java.lang.invoke.MethodHandles;
import java.util.Objects;
import java.util.function.Function;

import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.unascribed.lib39.core.P39.*;
import com.unascribed.lib39.core.api.util.ReflectionHelper;

import com.google.common.collect.Iterables;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.command.argument.BlockArgumentParser;
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
import net.minecraft.resource.pack.metadata.ResourceMetadataReader;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.TagKey;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Holder;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.Vector4f;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.WorldChunk;

class P39Impl {
	
	static final P39.Factory FACTORY = new Factory() {
		@Override
		public P39.MetaPort meta() {
			return new P39.MetaPort() {
				@Override
				public String target() {
					return "1.18.2";
				}
			};
		}
		
		@Override
		public P39.TextPort text() {
			return new P39.TextPort() {
				@Override
				public MutableText literal(String text) {
					return new LiteralText(text);
				}
			
				@Override
				public MutableText translatable(String key, Object... args) {
					return new TranslatableText(key, args);
				}
			};
		}
		
		@Override
		public P39.WorldsPort worlds() {
			return new P39.WorldsPort() {
				
				private static final Function<ThreadedAnvilChunkStorage, Long2ObjectLinkedOpenHashMap<ChunkHolder>> chunkHolders =
						(Function)ReflectionHelper.of(MethodHandles.lookup(), ThreadedAnvilChunkStorage.class)
							.obtainGetter(Long2ObjectLinkedOpenHashMap.class, "chunkHolders", "field_17220");

				@Override
				public Iterable<WorldChunk> getLoadedChunks(ServerWorld world) {
					return () -> chunkHolders.apply(world.getChunkManager().threadedAnvilChunkStorage)
							.values().stream()
							.filter(Objects::nonNull)
							.filter(hc -> hc.getCurrentStatus() != null && hc.getCurrentStatus().isAtLeast(ChunkStatus.FULL))
							.map(hc -> (WorldChunk)world.getChunk(hc.getPos().x, hc.getPos().z, ChunkStatus.FULL, false))
							.filter(Objects::nonNull)
							.iterator();
				}

				@Override
				public void playSound(World world, PlayerEntity except, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch) {
					world.playSound(except, x, y, z, sound, category, volume, pitch);
				}
				
			};
		}
		
		@Override
		public P39.RegistriesPort registries() {
			return new P39.RegistriesPort() {

				@Override
				public Registry<SoundEvent> soundEvent() {
					return Registry.SOUND_EVENT;
				}

				@Override
				public Registry<Item> item() {
					return Registry.ITEM;
				}

				@Override
				public Registry<Block> block() {
					return Registry.BLOCK;
				}

				@Override
				public Registry<Fluid> fluid() {
					return Registry.FLUID;
				}

				@Override
				public Registry<StatusEffect> statusEffect() {
					return Registry.STATUS_EFFECT;
				}

				@Override
				public Registry<RecipeType<?>> recipeType() {
					return Registry.RECIPE_TYPE;
				}

				@Override
				public Registry<RecipeSerializer<?>> recipeSerializer() {
					return Registry.RECIPE_SERIALIZER;
				}

				@Override
				public RegistryKey<Registry<Biome>> biomeRegistry() {
					return Registry.BIOME_KEY;
				}

				@Override
				public <T> Tag<T> tag(Registry<T> registry, Identifier id) {
					var tag = registry.getTag(TagKey.of(registry.getKey(), id)).get();
					return new Tag<T>() {

						@Override
						public Iterable<T> getAll() {
							return Iterables.transform(tag, h -> h.value());
						}

						@Override
						public boolean has(T t) {
							if (t == null) throw new NullPointerException();
							Holder holder = null;
							if (t instanceof Block b) holder = b.getBuiltInRegistryHolder();
							if (t instanceof Item i) holder = i.getBuiltInRegistryHolder();
							if (t instanceof Fluid f) holder = f.getBuiltInRegistryHolder();
							if (holder == null) throw new UnsupportedOperationException(t.getClass().getName());
							return tag.contains(holder);
						}
					};
				}
				
			};
		}
		
		@Override
		public P39.ResourcesPort resources() {
			return new P39.ResourcesPort() {

				@Override
				public <T> T readMetadata(Resource resource, ResourceMetadataReader<T> reader) {
					return resource.getMetadata(reader);
				}
				
			};
		}
		
		@Override
		public P39.ParsingPort parsing() {
			return new P39.ParsingPort() {

				@Override
				public BlockState parseBlockState(String str) {
					try {
						return new BlockArgumentParser(new StringReader(str), false).parse(false).getBlockState();
					} catch (CommandSyntaxException e) {
						throw new IllegalArgumentException(e);
					}
				}
				
			};
		}
		
		@Override
		@Environment(EnvType.CLIENT)
		public P39.RenderingPort rendering() {
			return new P39.RenderingPort() {

				@Override
				public void rotate(MatrixStack matrices, float deg, float x, float y, float z) {
					Vec3f vec;
					if (x == 1 && y == 0 && z == 0) {
						vec = Vec3f.POSITIVE_X;
					} else if (x == 0 && y == 1 && z == 0) {
						vec = Vec3f.POSITIVE_Y;
					} else if (x == 0 && y == 0 && z == 1) {
						vec = Vec3f.POSITIVE_Z;
					} else {
						vec = new Vec3f(x, y, z);
					}
					matrices.multiply(vec.getDegreesQuaternion(deg));
				}

				@Override
				@Environment(EnvType.CLIENT)
				public double[] transform(MatrixStack matrices, double x, double y, double z) {
					Vector4f vec = new Vector4f((float)x, (float)y, (float)z, 1);
					vec.transform(matrices.peek().getModel());
					return new double[] {vec.getX(), vec.getY(), vec.getZ()};
				}

				@Override
				@Environment(EnvType.CLIENT)
				public Iterable<BakedQuad> getQuads(BakedModel bm, BlockState state, Direction face, World world) {
					return bm.getQuads(state, face, world.random);
				}

				@Override
				@Environment(EnvType.CLIENT)
				public void upload(VertexBuffer vb, BufferBuilder vc) {
					vb.bind();
					vb.upload(vc);
				}

				@Override
				@Environment(EnvType.CLIENT)
				public void draw(VertexBuffer buf, MatrixStack matrices, ShaderProgram shader) {
					buf.setShader(matrices.peek().getModel(), RenderSystem.getProjectionMatrix(), shader);
				}
				
			};
		}
		
		@Override
		@Environment(EnvType.CLIENT)
		public P39.ScreenPort screens() {
			return new P39.ScreenPort() {

				@Override
				@Environment(EnvType.CLIENT)
				public ItemGroup getSelectedItemGroup(CreativeInventoryScreen screen) {
					return ItemGroup.GROUPS[screen.getSelectedTab()];
				}
				
			};
		}
	};

}