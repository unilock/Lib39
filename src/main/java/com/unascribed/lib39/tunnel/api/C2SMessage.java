package com.unascribed.lib39.tunnel.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class C2SMessage extends Message {

	public C2SMessage(NetworkContext ctx) {
		super(ctx);
	}
	
	@Override
	protected final void handle(PlayerEntity player) {
		handle((ServerPlayerEntity)player);
	}
	
	protected abstract void handle(ServerPlayerEntity player);

	/**
	 * @deprecated Not applicable for a client-to-server message
	 */
	@Deprecated
	@Override
	public void sendToAllWatching(Entity e) {
		super.sendToAllWatching(e);
	}

	/**
	 * @deprecated Not applicable for a client-to-server message
	 */
	@Deprecated
	@Override
	public void sendTo(PlayerEntity player) {
		super.sendTo(player);
	}
	
}
