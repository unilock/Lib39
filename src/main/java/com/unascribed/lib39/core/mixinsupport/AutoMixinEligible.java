package com.unascribed.lib39.core.mixinsupport;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.unascribed.lib39.core.api.AutoMixin;
import com.unascribed.lib39.core.api.RunType;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * Adds special conditions for a mixin discovered by {@link AutoMixin} to be loaded.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface AutoMixinEligible {

	/**
	 * If specified, the mixin will only be loaded if <b>all</b> of these mod IDs are present.
	 */
	String[] ifModPresent() default {};
	/**
	 * If specified, the mixin will <b>not</b> be loaded if <b>any</b> of these mod IDs are present.
	 */
	String[] unlessModPresent() default {};
	
	
	/**
	 * If specified, the mixin will only be loaded if <b>all</b> of these system properties are
	 * present and set to {@code true}.
	 */
	String[] ifSystemProperty() default {};
	/**
	 * If specified, the mixin will <b>not</b> be loaded if <b>any</b> of these system properties
	 * are present and set to {@code true}.
	 */
	String[] unlessSystemProperty() default {};
	

	/**
	 * If specified, the mixin will only be loaded if <b>all</b> of the given config keys are
	 * {@code true}, <i>as according to your override of getConfigValue in your AutoMixin
	 * subclass</i>. If you do not override getConfigValue, <b>the game will crash</b>!
	 */
	String[] ifConfigSet() default {};
	/**
	 * If specified, the mixin will <b>not</b> be loaded if <b>any</b> of the given config keys are
	 * {@code false}, <i>as according to your override of getConfigValue in your AutoMixin
	 * subclass</i>. If you do not override getConfigValue, <b>the game will crash</b>!
	 */
	String[] unlessConfigSet() default {};
	
	
	/**
	 * If specified, the mixin will only be loaded in the given environment.
	 * <p>
	 * Can also be specified via Fabric Loader's {@link Environment} annotation, or Quilt Loader's
	 * ClientOnly/DedicatedServerOnly annotations.
	 */
	EnvType inEnvType() default EnvType.SERVER; // default is not used
	/**
	 * If specified, the mixin will only be loaded in the given runtime.
	 */
	RunType inRunType() default RunType.PRODUCTION; // default is not used
	
}
