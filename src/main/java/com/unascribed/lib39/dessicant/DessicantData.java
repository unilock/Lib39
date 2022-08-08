package com.unascribed.lib39.dessicant;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.util.Identifier;

public class DessicantData {

	public static final Set<String> optedInNamespaces = new HashSet<>();
	public static final Multimap<Identifier, Identifier> discoveries = HashMultimap.create();
	
}
