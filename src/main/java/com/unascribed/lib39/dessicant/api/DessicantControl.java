package com.unascribed.lib39.dessicant.api;

import com.unascribed.lib39.dessicant.DessicantData;

public class DessicantControl {

	/**
	 * Opt the given namespace into Dessicant's autogeneration.
	 * @param namespace the namespace of your mod, such as "yttr"
	 */
	public static void optIn(String namespace) {
		DessicantData.optedInNamespaces.add(namespace);
	}
	
}
