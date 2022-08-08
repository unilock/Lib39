package com.unascribed.lib39.keygen.mixin;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.unascribed.lib39.keygen.IBXMAudioStream;
import com.unascribed.lib39.keygen.IBXMAudioStream.InterpolationMode;
import com.unascribed.lib39.keygen.IBXMResourceMetadata;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.AudioStream;
import net.minecraft.client.sound.RepeatingAudioStream;
import net.minecraft.client.sound.RepeatingAudioStream.DelegateFactory;
import net.minecraft.client.sound.SoundLoader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

@Environment(EnvType.CLIENT)
@Mixin(SoundLoader.class)
public class MixinSoundLoader {

	@Shadow @Final
	private ResourceManager resourceManager;

	@Inject(at=@At("HEAD"), method="loadStreamed", cancellable=true)
	public void lib39Keygen$loadStreamed(Identifier id, boolean repeatInstantly, CallbackInfoReturnable<CompletableFuture<AudioStream>> ci) {
		String path = id.getPath();
		boolean bz2 = path.endsWith(".bz2");
		if (bz2) path = path.substring(0, path.length()-4);
		if (path.endsWith(".xm") || path.endsWith(".s3m") || path.endsWith(".mod")) {
			String fpath = path;
			ci.setReturnValue(CompletableFuture.supplyAsync(() -> {
				try {
					Resource resource = this.resourceManager.getResource(id).get();
					InputStream inputStream = resource.open();
					DelegateFactory factory;
					IBXMResourceMetadata meta = resource.getMetadata().readMetadata(IBXMResourceMetadata.READER).orElse(null);
					boolean isAmiga = fpath.endsWith(".mod");
					InterpolationMode defaultMode = isAmiga ? InterpolationMode.LINEAR : InterpolationMode.SINC;
					if (meta != null) {
						factory = in -> IBXMAudioStream.create(in, meta.getMode() == null ? defaultMode : meta.getMode(), meta.isStereo());
					} else {
						factory = in -> IBXMAudioStream.create(in, defaultMode, false);
					}
					if (bz2) {
						inputStream = new BZip2CompressorInputStream(inputStream);
					}
					return repeatInstantly ? new RepeatingAudioStream(factory, inputStream) : factory.create(inputStream);
				} catch (Throwable t) {
					t.printStackTrace();
					throw new CompletionException(t);
				}
			}, Util.getMainWorkerExecutor()));
		}
	}

}