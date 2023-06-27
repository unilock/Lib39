package com.unascribed.lib39.fractal.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.blaze3d.systems.RenderSystem;
import com.unascribed.lib39.core.P39;
import com.unascribed.lib39.core.mixinsupport.AutoMixinEligible;
import com.unascribed.lib39.fractal.api.ItemSubGroup;
import com.unascribed.lib39.fractal.quack.ItemGroupParent;
import com.unascribed.lib39.fractal.quack.SubTabLocation;

import net.fabricmc.api.EnvType;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen.CreativeScreenHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Mixin(CreativeInventoryScreen.class)
@AutoMixinEligible(
		unlessConfigSet="platform 1.19.3",
		inEnvType=EnvType.CLIENT
	)
public abstract class MixinCreativeInventoryScreen extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeScreenHandler> implements SubTabLocation {
	
	public MixinCreativeInventoryScreen(CreativeScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
		super(screenHandler, playerInventory, text);
	}

	@Shadow
	private float scrollPosition;
	
	private int lib39Fractal$x, lib39Fractal$y, lib39Fractal$w, lib39Fractal$h;
	
	@Inject(at=@At(value="INVOKE", target="net/minecraft/client/gui/screen/ingame/CreativeInventoryScreen.drawMouseoverTooltip(Lnet/minecraft/client/util/math/MatrixStack;II)V"),
			method="render")
	public void lib39Fractal$render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		ItemGroup selected = P39.screens().getSelectedItemGroup((CreativeInventoryScreen)(Object)this);
		if (selected instanceof ItemGroupParent parent && parent.lib39Fractal$getChildren() != null && !parent.lib39Fractal$getChildren().isEmpty()) {
			if (!selected.shouldRenderName()) {
				ItemGroup child = parent.lib39Fractal$getSelectedChild();
				// TODO [jas]: is getString() correct?
				float x = P39.rendering().drawText(textRenderer, matrices, selected.getName().getString(), this.x+8, this.y+6, 4210752);
				if (child != null) {
					x = P39.rendering().drawText(textRenderer, matrices, " ", x, this.y+6, 4210752);
					x = P39.rendering().drawText(textRenderer, matrices, child.getName().getString(), x, this.y+6, 4210752);
				}
			}
			int ofs = 5;
			int x = this.x-ofs;
			int y = this.y+6;
			int tw = 56;
			lib39Fractal$x = x-tw;
			lib39Fractal$y = y;
			for (ItemSubGroup child : parent.lib39Fractal$getChildren()) {
				RenderSystem.setShaderColor(1, 1, 1, 1);
				RenderSystem.setShaderTexture(0, new Identifier("lib39-fractal", "textures/subtab.png"));
				boolean childSelected = child == parent.lib39Fractal$getSelectedChild();
				int bgV = childSelected ? 11 : 0;
				P39.rendering().drawTexture(matrices, x-tw, y, 0, bgV, tw+ofs, 11, 70, 22);
				P39.rendering().drawTexture(matrices, this.x, y, 64, bgV, 6, 11, 70, 22);
				RenderSystem.setShaderTexture(0, new Identifier("lib39-fractal", "textures/tinyfont.png"));
				String str = child.getName().getString();
				for (int i = str.length()-1; i >= 0; i--) {
					char c = str.charAt(i);
					if (c > 0x7F) continue;
					int u = (c%16)*4;
					int v = (c/16)*6;
					RenderSystem.setShaderColor(0, 0, 0, 1);
					P39.rendering().drawTexture(matrices, x, y+3, u, v, 4, 6, 64, 48);
					x -= 4;
				}
				x = this.x-ofs;
				y += 10;
			}
			lib39Fractal$w = tw+ofs;
			lib39Fractal$h = y-lib39Fractal$y;
			RenderSystem.setShaderColor(1, 1, 1, 1);
		}
	}
	
	@Inject(at=@At("HEAD"), method="mouseClicked", cancellable=true)
	public void lib39Fractal$mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> ci) {
		ItemGroup selected = P39.screens().getSelectedItemGroup((CreativeInventoryScreen)(Object)this);
		if (selected instanceof ItemGroupParent parent && parent.lib39Fractal$getChildren() != null && !parent.lib39Fractal$getChildren().isEmpty()) {
			int x = lib39Fractal$x;
			int y = lib39Fractal$y;
			int w = lib39Fractal$w;
			for (ItemSubGroup child : parent.lib39Fractal$getChildren()) {
				if (mouseX >= x && mouseX <= x+w && mouseY >= y && mouseY <= y+11) {
					parent.lib39Fractal$setSelectedChild(child);
					handler.itemList.clear();
					selected.method_7738(handler.itemList);
					this.scrollPosition = 0.0F;
					handler.scrollItems(0.0F);
					ci.setReturnValue(true);
					return;
				}
				y += 10;
			}
		}
	}

	@Override
	public int lib39Fractal$getX() {
		return lib39Fractal$x;
	}
	
	@Override
	public int lib39Fractal$getY() {
		return lib39Fractal$y;
	}
	
	@Override
	public int lib39Fractal$getW() {
		return lib39Fractal$w;
	}
	
	@Override
	public int lib39Fractal$getH() {
		return lib39Fractal$h;
	}
	
}
