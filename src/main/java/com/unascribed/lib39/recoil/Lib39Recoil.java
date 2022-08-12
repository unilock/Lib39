package com.unascribed.lib39.recoil;

import com.unascribed.lib39.recoil.api.DirectClickItem;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class Lib39Recoil implements ModInitializer {

	@Override
	public void onInitialize() {
		ServerPlayNetworking.registerGlobalReceiver(new Identifier("lib39-recoil", "direct_attack"), (server, player, handler, buf, sender) -> {
			server.execute(() -> {
				if (player.getMainHandStack().getItem() instanceof DirectClickItem dci) {
					if (dci.onDirectAttack(player, Hand.MAIN_HAND).shouldSwingHand()) {
						player.swingHand(Hand.MAIN_HAND, true);
					}
				}
			});
		});
		
		ServerPlayNetworking.registerGlobalReceiver(new Identifier("lib39-recoil", "direct_use"), (server, player, handler, buf, sender) -> {
			server.execute(() -> {
				if (player.getMainHandStack().getItem() instanceof DirectClickItem dci) {
					if (dci.onDirectUse(player, Hand.MAIN_HAND).shouldSwingHand()) {
						player.swingHand(Hand.MAIN_HAND, true);
					}
				}
			});
		});
	}

}
