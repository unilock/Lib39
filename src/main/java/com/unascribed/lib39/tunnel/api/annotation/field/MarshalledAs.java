package com.unascribed.lib39.tunnel.api.annotation.field;

import com.unascribed.lib39.tunnel.api.ImmutableMarshallable;
import com.unascribed.lib39.tunnel.api.Marshallable;
import com.unascribed.lib39.tunnel.api.Marshaller;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies the wire format of a Message field.
 * <p>Marshallers are automatically derived for booleans, enums, records, {@link Marshallable}s,
 * {@link ImmutableMarshallable}s, and a number of built-in non-primitive types.
 * You only need to use this annotation to specify the marshallers of other primitives,
 * lists, and other non-{@link Marshallable} classes.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MarshalledAs {
	/**
	 * The name of the marshaller to use. Either a default marshaller, such as
	 * "u8", "uint8", etc., or the fully qualified class name of a custom {@link Marshaller},
	 * or the fully qualified class name of a record or enum class.
	 * <p>To serialize a list, suffix the component's marshaller with "-list".
	 * <p>Built-in non-primitive marshallers, as well as any enum, record, or
	 * {@link Marshallable} type, don't need to be specified unless they are
	 * the entries of a list.
	 */
	String value();
}
