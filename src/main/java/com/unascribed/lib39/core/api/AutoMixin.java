package com.unascribed.lib39.core.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipFile;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import com.unascribed.lib39.core.Lib39Log;
import com.google.common.collect.Lists;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

/**
 * Extend this class and register it as your mixin plugin to autodiscover mixins inside your mixin
 * package in your jar.
 */
public class AutoMixin implements IMixinConfigPlugin {

	private String pkg;
	
	@Override
	public void onLoad(String pkg) {
		this.pkg = pkg;
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
		
	}
	
	protected boolean shouldAnnotationSkipMixin(String name, AnnotationNode an) {
		if (an.desc.equals("Lnet/fabricmc/api/Environment;")) {
			if (an.values == null) return false;
			for (int i = 0; i < an.values.size(); i += 2) {
				String k = (String)an.values.get(i);
				Object v = an.values.get(i+1);
				if ("value".equals(k) && v instanceof String[]) {
					String[] arr = (String[])v;
					if (arr[0].equals("Lnet/fabricmc/api/EnvType;")) {
						EnvType e = EnvType.valueOf(arr[1]);
						if (e != FabricLoader.getInstance().getEnvironmentType()) {
							Lib39Log.debug("Skipping {} mixin {}", e, name);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public List<String> getMixins() {
		List<String> rtrn = Lists.newArrayList();
		int skipped = 0;
		try {
			URI uri = getJarURL(getClass().getProtectionDomain().getCodeSource().getLocation()).toURI();
			File f = new File(uri);
			if (f.isDirectory()) {
				Path base = f.toPath();
				for (Path p : (Iterable<Path>)Files.walk(base)::iterator) {
					if (discover(rtrn, base.relativize(p).toString(), () -> Files.newInputStream(p))) {
						skipped++;
					}
				}
			} else {
				try (ZipFile zip = new ZipFile(f)) {
					for (var en : Collections.list(zip.entries())) {
						if (discover(rtrn, en.getName(), () -> zip.getInputStream(en))) {
							skipped++;
						}
					}
				}
			}
		} catch (URISyntaxException e) {
			throw new AssertionError(e);
		} catch (IOException e) {
			throw new RuntimeException("Cannot autodiscover mixins for "+pkg, e);
		}
		Lib39Log.debug("Discovered {} mixins in {} (skipped {})", rtrn.size(), pkg, skipped);
		return rtrn;
	}

	private interface StreamOpener {
		InputStream openStream() throws IOException;
	}
	
	private boolean discover(List<String> li, String path, StreamOpener opener) throws IOException {
		if (path.endsWith(".class") && path.startsWith(pkg.replace('.', '/')+"/")) {
			String name = path.replace('/', '.').replace(".class", "");
			// we want nothing to do with inner classes and the like
			if (name.contains("$")) return false;
			try {
				ClassReader cr = new ClassReader(opener.openStream());
				ClassNode cn = new ClassNode();
				cr.accept(cn, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
				if (cn.invisibleAnnotations != null) {
					for (AnnotationNode an : cn.invisibleAnnotations) {
						if (shouldAnnotationSkipMixin(name, an)) {
							return true;
						}
					}
				}
				li.add(name.substring(pkg.length()+1));
			} catch (IOException e) {
				Lib39Log.warn("Exception while trying to read {}", name, e);
			}
		}
		return false;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		
	}
	
	private static URL getJarURL(URL codeSource) {
		if ("jar".equals(codeSource.getProtocol())) {
			String str = codeSource.toString().substring(4);
			int bang = str.indexOf('!');
			if (bang != -1) str = str.substring(0, bang);
			try {
				return new URL(str);
			} catch (MalformedURLException e) {
				return null;
			}
		} else if ("union".equals(codeSource.getProtocol())) {
			// some ModLauncher nonsense
			String str = codeSource.toString().substring(6);
			int bullshit = str.indexOf("%23");
			if (bullshit != -1) str = str.substring(0, bullshit);
			try {
				return new URL("file:"+str);
			} catch (MalformedURLException e) {
				return null;
			}
		}
		return codeSource;
	}


}
