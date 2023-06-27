package com.unascribed.lib39.machination.emi;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.Tessellator;
import com.unascribed.lib39.core.P39;

import dev.emi.emi.EmiPort;
import dev.emi.emi.EmiRenderHelper;
import dev.emi.emi.api.render.EmiRender;
import dev.emi.emi.api.stack.ItemEmiStack;
import dev.emi.emi.runtime.EmiDrawContext;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class BlockEmiStack extends ItemEmiStack {

	public BlockEmiStack(Block block) {
		super(new ItemStack(block));
	}

	// FIXME: pre 1.20

	@Override
	public void render(GuiGraphics draw, int x, int y, float delta, int flags) {
		EmiDrawContext context = EmiDrawContext.wrap(draw);

		MatrixStack matrices = draw.method_51448();
		MinecraftClient client = MinecraftClient.getInstance();
		ItemStack stack = getItemStack();
		if ((flags & 1) != 0) {
			VertexConsumerProvider.Immediate vcp = VertexConsumerProvider.immediate(Tessellator.getInstance().getBufferBuilder());
			int light = LightmapTextureManager.pack(15, 15);
			int overlay = OverlayTexture.DEFAULT_UV;
			RenderSystem.setShaderColor(1, 1, 1, 1);
			matrices.push();
			matrices.translate(x+8, y+8, 0);
			matrices.scale(16, -16, 16);
			P39.rendering().rotate(matrices, -90, 1, 0, 0);
			matrices.push();
			P39.rendering().renderItem(getItemStack(), light, overlay, matrices, vcp, 0);
			matrices.pop();
			vcp.draw();
			matrices.pop();
		}

		if ((flags & 2) != 0) {
			String count = "";
			if (amount != 1) {
				count += amount;
			}
			EmiRenderHelper.renderAmount(context, x, y, EmiPort.literal(count));
		}

		if ((flags & 8) != 0) {
			EmiRender.renderRemainderIcon(this, draw, x, y);
		}
	}

	@Override
	public boolean isUnbatchable() {
		return true;
	}

}
