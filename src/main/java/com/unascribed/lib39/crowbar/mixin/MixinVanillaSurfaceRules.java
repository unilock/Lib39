package com.unascribed.lib39.crowbar.mixin;

import java.util.function.BiConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.unascribed.lib39.core.Lib39Log;
import com.unascribed.lib39.crowbar.api.SurfaceRuleModifier;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.surfacebuilder.SurfaceRules;
import net.minecraft.world.gen.surfacebuilder.SurfaceRules.SequenceMaterialRule;
import net.minecraft.world.gen.surfacebuilder.VanillaSurfaceRules;

@Mixin(VanillaSurfaceRules.class)
public class MixinVanillaSurfaceRules {

	@ModifyVariable(at=@At(value="CONSTANT", args="doubleValue=-0.909", ordinal=0),
			method="getOverworldLikeRules", ordinal=6)
	private static SurfaceRules.MaterialRule lib39Crowbar$modifyDirtRule(SurfaceRules.MaterialRule rule) {
		lib39Crowbar$modifyRule(SurfaceRuleModifier::modifyDirtSurfaceRules, (SequenceMaterialRule)rule);
		return rule;
	}

	@ModifyVariable(at=@At(value="CONSTANT", args="doubleValue=-0.909", ordinal=0),
			method="getOverworldLikeRules", ordinal=7)
	private static SurfaceRules.MaterialRule lib39Crowbar$modifyGrassRule(SurfaceRules.MaterialRule rule) {
		lib39Crowbar$modifyRule(SurfaceRuleModifier::modifyGrassSurfaceRules, (SequenceMaterialRule)rule);
		return rule;
	}
	
	private static void lib39Crowbar$modifyRule(BiConsumer<SurfaceRuleModifier, BiConsumer<Identifier, BlockState>> mthd, SequenceMaterialRule rule) {
		for (var en : FabricLoader.getInstance().getEntrypointContainers("lib39:surface_rule_modifier", SurfaceRuleModifier.class)) {
			try {
				mthd.accept(en.getEntrypoint(), (id, bs) -> {
					rule.sequence().add(0,
							SurfaceRules.condition(
								SurfaceRules.biome(RegistryKey.of(Registry.BIOME_KEY, id)),
								SurfaceRules.block(bs)
							));
				});
			} catch (Throwable t) {
				Lib39Log.error("Mod '{}' threw exception during surface rule modification", en.getProvider().getMetadata().getId(), t);
			}
		}
	}
	
}
