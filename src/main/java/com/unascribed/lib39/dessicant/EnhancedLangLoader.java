package com.unascribed.lib39.dessicant;

import java.util.function.BiConsumer;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.unascribed.lib39.core.Lib39Log;

public class EnhancedLangLoader {

	private static final Pattern BRACE_PATTERN = Pattern.compile("\\{([0-9]+)\\}");
	
	public static void load(String prefix, JsonObject obj, BiConsumer<String, String> out) {
		for (var en : obj.entrySet()) {
			var k = prefix+en.getKey();
			if (en.getValue() instanceof JsonPrimitive jp && jp.isString()) {
				out.accept(k, process(jp.getAsString()));
			} else if (en.getValue() instanceof JsonObject jo) {
				char fin = k.charAt(k.length()-1);
				load(k+(fin == '/' || fin == '.' ? "" : "."), jo, out);
			} else if (en.getValue() instanceof JsonArray ja) {
				for (int i = 0; i < ja.size(); i++) {
					var ele = ja.get(i);
					if (ele instanceof JsonPrimitive jp && jp.isString()) {
						out.accept(k+"."+(i+1), process(jp.getAsString()));
					} else {
						Lib39Log.warn("Unexpected value {} for key {}[{}] while loading enhanced lang file", ele, k, i);
					}
				}
			} else {
				Lib39Log.warn("Unexpected value {} for key {} while loading enhanced lang file", en.getValue(), k);
			}
		}
	}

	private static String process(String str) {
		return BRACE_PATTERN.matcher(str.replace("%", "%%"))
				.replaceAll("%$1\\$s")
				.replace("{}", "%s");
	}

}
