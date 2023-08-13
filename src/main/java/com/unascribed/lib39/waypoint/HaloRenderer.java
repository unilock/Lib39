package com.unascribed.lib39.waypoint;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import org.jetbrains.annotations.Nullable;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import com.mojang.blaze3d.vertex.VertexFormats;
import com.unascribed.lib39.core.Lib39Log;
import com.unascribed.lib39.util.api.DelegatingVertexConsumer;
import com.unascribed.lib39.util.api.MysticSet;
import com.unascribed.lib39.waypoint.api.HaloBlockEntity;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;

public class HaloRenderer {

	private static final MinecraftClient mc = MinecraftClient.getInstance();
	
	private static final Map<BlockPos, BlockEntity> lampsByBlock = new Object2ObjectOpenHashMap<>();
	private static final Map<BlockEntity, Object> lastState = new Object2ObjectOpenHashMap<>();
	private static final Multimap<ChunkSectionPos, BlockEntity> lampsBySection = Multimaps.newSetMultimap(new Object2ObjectOpenHashMap<>(), ReferenceOpenHashSet::new);
	private static final Map<ChunkSectionPos, VertexBuffer> buffers = new Object2ObjectOpenHashMap<>();
	private static final Map<ChunkSectionPos, Box> boundingBoxes = new Object2ObjectOpenHashMap<>();
	private static final Multimap<ChunkSectionPos, Sprite> sprites = Multimaps.newSetMultimap(new Object2ObjectOpenHashMap<>(), ReferenceOpenHashSet::new);

	public static void clearCache() {
		buffers.values().forEach(VertexBuffer::close);
		buffers.clear();
		boundingBoxes.clear();
		sprites.clear();
	}

	public static void render(World world, MatrixStack matrices, VertexConsumer vc, BlockState state,
			int color, @Nullable Direction facing, @Nullable BlockPos pos) {
		render(world, matrices, vc, state, color, facing, pos, s -> {});
	}
	
	public static void render(World world, MatrixStack matrices, VertexConsumer vc, BlockState state,
			int color, @Nullable Direction facing, @Nullable BlockPos pos,
			Consumer<Sprite> spriteMemoizer) {
		if (color == 0) color = 0x222222;
		float r = ((color >> 16)&0xFF)/255f;
		float g = ((color >> 8)&0xFF)/255f;
		float b = (color&0xFF)/255f;
		BakedModel base = MinecraftClient.getInstance().getBlockRenderManager().getModel(state);
		BakedModel bm;
		try {
			Lib39Waypoint.retrievingHalo = true;
			bm = base.getOverrides().apply(base, ItemStack.EMPTY, MinecraftClient.getInstance().world, MinecraftClient.getInstance().player, 39);
		} finally {
			Lib39Waypoint.retrievingHalo = false;
		}
		if (bm == null) return;
		DelegatingVertexConsumer dvc = new DelegatingVertexConsumer(vc) {
			@Override
			public void vertex(
					float x, float y, float z,
					float red, float green, float blue, float alpha,
					float u, float v,
					int overlay, int light,
					float normalX, float normalY, float normalZ
				) {
					vertex(x, y, z);
					uv(u, v);
					color(red, green, blue, alpha);
					normal(normalX, normalY, normalZ);
					next();
				}
		};

		matrices.push();

		if (facing != null) {
			int x = 0;
			int y = 0;
			switch (facing) {
				case DOWN: break;
				case WEST: x = 90; y = 90; break;
				case NORTH: x = 90; break;
				case SOUTH: x = 90; y = 180; break;
				case EAST: x = 90; y = 270; break;
				case UP: x = 180; break;
			}
			matrices.translate(0.5, 0.5, 0.5);
			matrices.multiply(new Quaternionf(new AxisAngle4f(y*MathHelper.RADIANS_PER_DEGREE, 0, 1, 0)));
			matrices.multiply(new Quaternionf(new AxisAngle4f(x*MathHelper.RADIANS_PER_DEGREE, 1, 0, 0)));
			matrices.translate(-0.5, -0.5, -0.5);
		}

		for (BakedQuad bq : bm.getQuads(state, null, world == null ? null : world.random)) {
			spriteMemoizer.accept(bq.getSprite());
			dvc.bakedQuad(matrices.peek(), bq, r, g, b, 0, 0);
		}
		for (Direction dir : Direction.values()) {
			if (pos == null || Block.shouldDrawSide(state, MinecraftClient.getInstance().world, pos, dir, pos.offset(dir))) {
				for (BakedQuad bq : bm.getQuads(state, dir, world == null ? null : world.random)) {
					spriteMemoizer.accept(bq.getSprite());
					dvc.bakedQuad(matrices.peek(), bq, r, g, b, 0, 0);
				}
			}
		}

		matrices.pop();
	}
	
	private static Boolean driverIsBuggy;

	public static void render(WorldRenderContext wrc) {
		if (driverIsBuggy == null) {
			driverIsBuggy = Pattern.compile("NVIDIA 39[0-9]\\.").matcher(RenderSystem.getBackendDescription()).find();
			if (driverIsBuggy) {
				clearCache();
				Lib39Log.error("====================== Lib39 Waypoint Error ======================");
				Lib39Log.error("       Your graphics driver is known to be buggy with VBOs.       ");
				Lib39Log.error("    To avoid a game crash, Lib39 Waypoint is disabling itself.    ");
				Lib39Log.error("  Any mods which utilize its features will not render correctly!  ");
				Lib39Log.error("  DO NOT REPORT BUGS RELATED TO HALO RENDERING ON THIS COMPUTER.  ");
				Lib39Log.error("==================================================================");
			}
		}
		if (driverIsBuggy) {
			return;
		}
		wrc.profiler().swap("lib39-waypoint");
		if (!lampsBySection.isEmpty()) {
			wrc.profiler().push("prepare");
			MysticSet<ChunkSectionPos> needsRebuild = MysticSet.of();
			for (BlockEntity be : lampsBySection.values()) {
				if (!(be instanceof HaloBlockEntity)) continue;
				Object s = ((HaloBlockEntity)be).getStateObject();
				ChunkSectionPos csp = ChunkSectionPos.from(be.getPos());
				if (lastState.get(be) != s || !buffers.containsKey(csp)) {
					lastState.put(be, s);
					needsRebuild = needsRebuild.add(csp);
				}
			}
			wrc.profiler().swap("rebuild");
			MatrixStack scratch = new MatrixStack();
			for (ChunkSectionPos csp : needsRebuild.mundane()) {
				Collection<BlockEntity> l = lampsBySection.get(csp);
				sprites.removeAll(csp);
				if (l.isEmpty()) {
					if (buffers.containsKey(csp)) {
						buffers.remove(csp).close();
						boundingBoxes.remove(csp);
					}
					continue;
				}
				Box bounds = null;
				// POSITION_TEXTURE_COLOR_NORMAL is one of the few generic shaders with fog support
				BufferBuilder vc = new BufferBuilder(24 * VertexFormats.POSITION_TEXTURE_COLOR_NORMAL.getVertexSize() * l.size());
				vc.begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_NORMAL);
				for (BlockEntity be : l) {
					if (!(be instanceof HaloBlockEntity) || !((HaloBlockEntity)be).shouldRenderHalo()) continue;
					scratch.push();
						scratch.translate(be.getPos().getX()-csp.getMinX(), be.getPos().getY()-csp.getMinY(), be.getPos().getZ()-csp.getMinZ());
						int color = ((HaloBlockEntity)be).getGlowColor();
						BlockState state = be.getCachedState();
						Direction facing = ((HaloBlockEntity)be).getFacing();
						render(mc.world, scratch, vc, state, color, facing, be.getPos(), s -> sprites.put(csp, s));
						Box myBox = new Box(be.getPos()).expand(0.5);
						if (bounds == null) {
							bounds = myBox;
						} else {
							bounds = bounds.union(myBox);
						}
					scratch.pop();
				}
				VertexBuffer vb = buffers.computeIfAbsent(csp, blah -> new VertexBuffer(VertexBuffer.Usage.STATIC));
				vb.bind();
				vb.upload(vc.end());
				buffers.put(csp, vb);
				boundingBoxes.put(csp, bounds);
			}
			wrc.profiler().swap("render");
			sprites.values().forEach(SodiumAccess.markSpriteActive);
			MatrixStack matrices = wrc.matrixStack();
			matrices.push();
			Vec3d cam = wrc.camera().getPos();
			matrices.translate(-cam.x, -cam.y, -cam.z);
			for (ChunkSectionPos pos : buffers.keySet()) {
				Box box = boundingBoxes.get(pos);
				if (box != null && wrc.frustum().isVisible(box) && wrc.worldRenderer().isChunkBuilt(pos.getMinPos())) {
					matrices.push();
						matrices.translate(pos.getMinX(), pos.getMinY(), pos.getMinZ());
						VertexBuffer buf = buffers.get(pos);
						buf.bind();
						WaypointRenderLayers.getHalo().startDrawing();
						buf.draw(matrices.peek().getModel(), RenderSystem.getProjectionMatrix(), GameRenderer.getPositionTexColorNormalShader());
						WaypointRenderLayers.getHalo().endDrawing();
						VertexBuffer.unbind();
					matrices.pop();
					if (mc.getEntityRenderDispatcher().shouldRenderHitboxes() && !mc.hasReducedDebugInfo()) {
						VertexConsumerProvider.Immediate vcp = mc.getBufferBuilders().getEntityVertexConsumers();
						WorldRenderer.drawBox(matrices, vcp.getBuffer(RenderLayer.getLines()), box, 1, 1, 0, 1);
						vcp.draw(RenderLayer.getLines());
					}
				}
			}
			matrices.pop();
			wrc.profiler().pop();
		}
		wrc.profiler().swap("particles");
	}
	
	public static <T extends BlockEntity & HaloBlockEntity> void notifyCreated(T be) {
		ChunkSectionPos cs = ChunkSectionPos.from(be.getPos());
		if (!lampsBySection.containsEntry(cs, be)) {
			if (lampsByBlock.containsKey(be.getPos())) {
				BlockEntity other = lampsByBlock.remove(be.getPos());
				lampsBySection.remove(ChunkSectionPos.from(be.getPos()), other);
			}
			lampsByBlock.put(be.getPos(), be);
			lampsBySection.put(cs, be);
		}
	}
	

	public static void tick() {
		if (driverIsBuggy == Boolean.TRUE) return;
		mc.getProfiler().swap("lib39-waypoint");
		if (mc.world != null) {
			Profiler p = mc.getProfiler();
			p.push("validate");
			Iterator<BlockEntity> iter = lampsBySection.values().iterator();
			while (iter.hasNext()) {
				BlockEntity be = iter.next();
				if (be.isRemoved() || be.getWorld() != mc.world) {
					ChunkSectionPos cs = ChunkSectionPos.from(be.getPos());
					if (buffers.containsKey(cs)) {
						buffers.remove(cs).close();
					}
					lampsByBlock.remove(be.getPos(), be);
					iter.remove();
				}
			}
			p.pop();
		} else {
			lampsByBlock.clear();
			lampsBySection.clear();
			lastState.clear();
			clearCache();
		}
	}

}
