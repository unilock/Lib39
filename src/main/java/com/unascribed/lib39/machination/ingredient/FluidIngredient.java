package com.unascribed.lib39.machination.ingredient;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.unascribed.lib39.core.P39;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.fluid.Fluid;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class FluidIngredient implements Predicate<Fluid> {

	private final Set<Fluid> exacts = Sets.newHashSet();
	private final Set<P39.Tag<Fluid>> tags = Sets.newHashSet();
	
	private FluidIngredient() {}
	
	@Override
	public boolean test(Fluid b) {
		if (exacts.contains(b)) return true;
		for (var tag : tags) {
			if (tag.has(b)) return true;
		}
		return false;
	}
	
	public List<Fluid> getMatchingFluids() {
		List<Fluid> li = Lists.newArrayList();
		li.addAll(exacts);
		for (var tag : tags) {
			for (var fluid : tag.getAll()) {
				li.add(fluid);
			}
		}
		return li;
	}
	
	public void write(PacketByteBuf out) {
		List<Fluid> all = getMatchingFluids();
		out.writeVarInt(all.size());
		for (Fluid f : all) {
			out.writeVarInt(P39.registries().fluid().getRawId(f));
		}
	}
	
	public static FluidIngredient read(PacketByteBuf in) {
		int amt = in.readVarInt();
		FluidIngredient out = new FluidIngredient();
		for (int i = 0; i < amt; i++) {
			out.exacts.add(P39.registries().fluid().get(in.readVarInt()));
		}
		return out;
	}
	
	public static FluidIngredient fromJson(JsonElement ele) {
		FluidIngredient out = new FluidIngredient();
		if (ele.isJsonArray()) {
			for (JsonElement child : ele.getAsJsonArray()) {
				readInto(out, child);
			}
		} else {
			readInto(out, ele);
		}
		return out;
	}

	private static void readInto(FluidIngredient out, JsonElement ele) {
		if (!ele.isJsonObject()) throw new IllegalArgumentException("Expected object, got "+ele);
		JsonObject obj = ele.getAsJsonObject();
		if (obj.has("fluid")) {
			out.exacts.add(P39.registries().fluid().get(Identifier.tryParse(obj.get("fluid").getAsString())));
		} else if (obj.has("tag")) {
			out.tags.add(P39.registries().tag(P39.registries().fluid(), Identifier.tryParse(obj.get("tag").getAsString())));
		} else {
			throw new IllegalArgumentException("Don't know how to parse "+ele+" without a fluid or tag value");
		}
	}
	
}
