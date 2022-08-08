package com.unascribed.lib39.tunnel.api;

import com.unascribed.lib39.tunnel.api.exception.BadMessageException;

import net.minecraft.network.PacketByteBuf;

public class MarshallableMarshaller<T extends Marshallable> implements Marshaller<T> {
	private final Class<T> clazz;
	public MarshallableMarshaller(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	@Override
	public T unmarshal(PacketByteBuf in) {
		T t;
		try {
			t = clazz.newInstance();
		} catch (Exception e) {
			throw new BadMessageException("Cannot instanciate marshallable " + clazz);
		}
		t.readFromNetwork(in);
		return t;
	}

	@Override
	public void marshal(PacketByteBuf out, T t) {
		t.writeToNetwork(out);
	}

}
