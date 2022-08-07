package com.unascribed.lib39.core.impl;

import java.time.MonthDay;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

import com.google.common.base.Charsets;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.hash.Hashing;

public class SplashTextHandler {

	private static final List<String> statics = new ArrayList<>();
	private static final ListMultimap<MonthDay, String> temporals = ArrayListMultimap.create();
	private static final List<Supplier<String>> dynamics = new ArrayList<>();
	private static final List<String> removals = new ArrayList<>();
	private static final Map<String, String> replacements = new HashMap<>();
	
	private static final Set<String> necessarySplashes = Set.of(
			"4876b3073f7da15ac4a688e9a9f9fcb5f1e29f1e701376f32faa49510115f1a8",
			"3ebbe99b5d2e5b00e44698ff6d37c91ece29e830f1005dda050c78d73a18f61e",
			"eb508b317d829b020f5a7b8f56b3e8bb48c04f4f1cd15fbefbb6fb949ba1d5e9",
			"c85138d38af4434552bc8cad3c48d279b0b0c35874beb4c5dcfbecabe6f74d8b",
			"2bfddfdb398a3fda965c7ba3b0b75f7e6d608366fdaad043712ef2730457ddee",
			"afd468a6303057d4e3d6d3c7d0f3c2301f77b60fc3a3dcfa0706380ad7c33df5",
			"36358601e4faea4b7dc1e634126f186fb7472701641a5120148fd4a7a6ca8b95",
			"65834796f8efd80d4b2ad433f693c67f4c9b2d2ae5703651952aa0838090dd77"
		);
	
	public static void registerStatic(String text) {
		statics.add(text);
	}

	public static void registerTemporal(String text, MonthDay when) {
		temporals.put(when, text);
	}

	public static void registerDynamic(Supplier<String> supplier) {
		dynamics.add(supplier);
	}

	public static void remove(String text) {
		if (necessarySplashes.contains(Hashing.sha256().hashString(text+"2ab4850e297d889e", Charsets.UTF_8).toString())) {
			throw new IllegalArgumentException("Lib39 will not help you remove this splash.");
		}
		removals.add(text);
	}

	public static void replace(String text, String replacement) {
		if (necessarySplashes.contains(Hashing.sha256().hashString(text+"2ab4850e297d889e", Charsets.UTF_8).toString())) {
			throw new IllegalArgumentException("Lib39 will not help you remove this splash.");
		}
		removals.add(text);
	}
	
	public static void modifyNormalSplashes(List<String> list) {
		list.replaceAll(s -> replacements.getOrDefault(s, s));
		list.removeAll(removals);
		list.addAll(statics);
	}
	
	public static String replaceSplash(int staticCount, String splash) {
		MonthDay now = MonthDay.now();
		List<String> eligibleTemporals = temporals.get(now);
		if (!eligibleTemporals.isEmpty()) {
			return eligibleTemporals.get(ThreadLocalRandom.current().nextInt(eligibleTemporals.size()));
		}
		if (!dynamics.isEmpty()) {
			int idx = ThreadLocalRandom.current().nextInt(staticCount+dynamics.size());
			if (idx < dynamics.size()) {
				return dynamics.get(idx).get();
			}
		}
		return splash;
	}

}
