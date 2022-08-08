package com.unascribed.lib39.tunnel.api;

import net.minecraft.network.PacketByteBuf;

public interface Marshallable {
	void writeToNetwork(PacketByteBuf buf);
	void readFromNetwork(PacketByteBuf buf);
}
