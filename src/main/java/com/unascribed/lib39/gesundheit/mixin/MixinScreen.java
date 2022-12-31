package com.unascribed.lib39.gesundheit.mixin;

import java.util.List;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.systems.RenderSystem;
import com.unascribed.lib39.gesundheit.DummyClientWorld;
import com.unascribed.lib39.gesundheit.GuiParticleManager;
import com.unascribed.lib39.gesundheit.quack.ParticleScreen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
@Mixin(Screen.class)
public class MixinScreen implements ParticleScreen {

	private DummyClientWorld lib39Gesundheit$particleWorld;
	private ParticleManager lib39Gesundheit$particleManager;
	private Camera lib39Gesundheit$dummyCamera;
	private boolean lib39Gesundheit$hasRenderedParticles;

	@Inject(at=@At("RETURN"), method="render")
	public void lib39Gesundheit$render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		lib39Gesundheit$renderParticles(matrices, delta);
		lib39Gesundheit$hasRenderedParticles = false;
	}

	@Inject(at=@At("RETURN"), method="tick")
	public void lib39Gesundheit$tick(CallbackInfo ci) {
		if (lib39Gesundheit$particleManager != null) {
			lib39Gesundheit$particleManager.tick();
		}
	}
	
	@Inject(at=@At("HEAD"), method={"renderTooltipFromComponents", "method_32633"}, remap=false)
	public void lib39Gesundheit$renderTooltipFromComponents(MatrixStack matrices, List<TooltipComponent> components, int x, int y, CallbackInfo ci) {
		lib39Gesundheit$renderParticles(matrices, MinecraftClient.getInstance().getTickDelta());
	}
	
	private void lib39Gesundheit$renderParticles(MatrixStack matrices, float delta) {
		if (lib39Gesundheit$particleManager != null && !lib39Gesundheit$hasRenderedParticles) {
			var mc = MinecraftClient.getInstance();
			if (mc.options.debugEnabled) {
				mc.textRenderer.draw(matrices, lib39Gesundheit$particleManager.getDebugString()+"gp", 2, 2, 0xFFFFCC00);
			}
			matrices.push();
			Object self = this;
			if (self instanceof AccessorHandledScreen hs) {
				matrices.translate(hs.lib39Gesundheit$getX(), hs.lib39Gesundheit$getY(), 0);
			}
			matrices.translate(0, 0, 80);
			matrices.scale(16, 16, 0.001f);
			var mvs = RenderSystem.getModelViewStack();
			mvs.push();
			mvs.loadIdentity();
			lib39Gesundheit$particleManager.renderParticles(matrices, mc.getBufferBuilders().getEntityVertexConsumers(),
					mc.gameRenderer.getLightmapTextureManager(), lib39Gesundheit$dummyCamera, delta);
			mvs.pop();
			matrices.pop();
		}
		lib39Gesundheit$hasRenderedParticles = true;
	}

	@Override
	public ParticleManager lib39Gesundheit$getParticleManager() {
		if (lib39Gesundheit$particleManager == null) {
			lib39Gesundheit$dummyCamera = new Camera() {
				{
					setRotation(0, 0);
					setPos(Vec3d.ZERO);
				}
			};
			lib39Gesundheit$particleWorld = DummyClientWorld.create();
			lib39Gesundheit$particleManager = new GuiParticleManager(lib39Gesundheit$particleWorld,
					MinecraftClient.getInstance().getTextureManager());
			lib39Gesundheit$particleWorld.setParticleManager(lib39Gesundheit$particleManager);
		}
		return lib39Gesundheit$particleManager;
	}

	@Override
	public DummyClientWorld lib39Gesundheit$getParticleWorld() {
		lib39Gesundheit$getParticleManager();
		return lib39Gesundheit$particleWorld;
	}
	
}
