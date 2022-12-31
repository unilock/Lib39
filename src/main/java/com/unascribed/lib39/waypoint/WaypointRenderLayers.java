package com.unascribed.lib39.waypoint;

import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.texture.SpriteAtlasTexture;

//extends RenderPhase for access to protected fields
public final class WaypointRenderLayers extends RenderPhase {

	private static final RenderPhase.Transparency ADDITIVE_WITH_ALPHA_TRANSPARENCY = new RenderPhase.Transparency("lib39_waypoint_additive_transparency_with_alpha", () -> {
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE, SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
	}, () -> {
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	});

	private static final RenderLayer HALO = RenderLayer.of("lib39_waypoint_halo",
				VertexFormats.POSITION_TEXTURE_COLOR_NORMAL,
				DrawMode.QUADS, 256, false, true,
		RenderLayer.MultiPhaseParameters.builder()
			.texture(new RenderPhase.Texture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, false, true))
			.transparency(ADDITIVE_WITH_ALPHA_TRANSPARENCY)
			.writeMaskState(new RenderPhase.WriteMaskState(true, false))
			.shader(new RenderPhase.Shader(GameRenderer::getPositionTexColorNormalShader))
			.build(false));

	public static RenderLayer getHalo() {
		return HALO;
	}
	
	private WaypointRenderLayers() {
		super(null, null, null);
	}
	
}
