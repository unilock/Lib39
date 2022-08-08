package com.unascribed.lib39.tunnel.api;

import net.minecraft.network.PacketByteBuf;

public interface ImmutableMarshallable {
	void writeToNetwork(PacketByteBuf buf);
	/**
	 * @deprecated This method doesn't do anything. It's here as a template for the static method implementers need to define.
	 */
	@Deprecated
	static void readFromNetwork(PacketByteBuf buf) {}
}
