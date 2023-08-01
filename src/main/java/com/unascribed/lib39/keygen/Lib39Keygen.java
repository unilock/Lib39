package com.unascribed.lib39.keygen;

import java.util.regex.Pattern;

import net.minecraft.util.Identifier;

public class Lib39Keygen {

	private static final Pattern MODULE_FILES = Pattern.compile("\\.(xm|s3m|mod)(\\.bz2)?$");
	
	public static boolean isModuleFile(Identifier id) {
		return MODULE_FILES.matcher(id.getPath()).find();
	}

}
