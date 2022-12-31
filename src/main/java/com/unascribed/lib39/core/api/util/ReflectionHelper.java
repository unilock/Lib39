package com.unascribed.lib39.core.api.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A convenient generic frontend to method handles.
 * <p>
 * Designed to be used in static initializers, and as such, does not throw exceptions.
 */
public class ReflectionHelper<T> {

	private final Class<T> clazz;
	private final Lookup lk;
	
	private ReflectionHelper(Class<T> clazz, MethodHandles.Lookup lk) {
		this.clazz = clazz;
		this.lk = lk;
	}
	
	public static <T> ReflectionHelper<T> of(Lookup lk, Class<T> clazz) {
		try {
			return new ReflectionHelper<>(clazz, MethodHandles.privateLookupIn(clazz, lk));
		} catch (IllegalAccessException e) {
			throw (IllegalAccessError)new IllegalAccessError().initCause(e);
		}
	}
	
	
	public <V> Supplier<V> obtainStaticGetter(Class<V> type, String... names) {
		return obtain(clazz, names,
				name -> lk.findStaticGetter(clazz, name, type),
				handle -> rethrowing(() -> (V)handle.invoke()));
	}
	public <V> Consumer<V> obtainStaticSetter(Class<V> type, String... names) {
		return obtain(clazz, names,
				name -> lk.findStaticSetter(clazz, name, type),
				handle -> rethrowing((v) -> { handle.invoke(v); }));
	}
	
	public <V> Function<T, V> obtainGetter(Class<V> type, String... names) {
		return obtain(clazz, names,
				name -> lk.findGetter(clazz, name, type),
				handle -> rethrowing((t) -> (V)handle.invoke(t)));
	}
	public <V> BiConsumer<T, V> obtainSetter(Class<V> type, String... names) {
		return obtain(clazz, names,
				name -> lk.findSetter(clazz, name, type),
				handle -> rethrowing((t, v) -> handle.invoke(t, v)));
	}

	
	public <V> MethodHandle obtainStatic(MethodType type, String... names) {
		return obtain(clazz, names,
				name -> lk.findStatic(clazz, name, type),
				handle -> handle);
	}
	public <V> MethodHandle obtainVirtual(MethodType type, String... names) {
		return obtain(clazz, names,
				name -> lk.findVirtual(clazz, name, type),
				handle -> handle);
	}
	public <V> MethodHandle obtainSpecial(MethodType type, String... names) {
		return obtain(clazz, names,
				name -> lk.findSpecial(clazz, name, type, lk.lookupClass()),
				handle -> handle);
	}
	

	private static <T, R> R obtain(Class<?> clazz, String[] names,
			ExceptableFunction<String, MethodHandle> lookup, ExceptableFunction<MethodHandle, R> constructor) {
		try {
			for (String name : names) {
				try {
					return constructor.apply(lookup.apply(name));
				} catch (NoSuchFieldException e) {}
			}
			throw new NoSuchFieldError("Could not find a field by any of the names given in "+clazz+": "+Arrays.toString(names));
		} catch (Throwable t) {
			throw new AssertionError(t);
		}
	}
	
	private static <T> Supplier<T> rethrowing(ExceptableSupplier<T> sup) {
		return () -> {
			try {
				return sup.get();
			} catch (Error | RuntimeException e) {
				throw e;
			} catch (Throwable e) {
				throw new AssertionError(e);
			}
		};
	}
	
	private static <T> Consumer<T> rethrowing(ExceptableConsumer<T> cons) {
		return (t) -> {
			try {
				cons.accept(t);
			} catch (Error | RuntimeException e) {
				throw e;
			} catch (Throwable e) {
				throw new AssertionError(e);
			}
		};
	}
	
	private static <T1, T2> BiConsumer<T1, T2> rethrowing(ExceptableBiConsumer<T1, T2> cons) {
		return (t1, t2) -> {
			try {
				cons.accept(t1, t2);
			} catch (Error | RuntimeException e) {
				throw e;
			} catch (Throwable e) {
				throw new AssertionError(e);
			}
		};
	}
	
	private static <T, R> Function<T, R> rethrowing(ExceptableFunction<T, R> func) {
		return (t) -> {
			try {
				return func.apply(t);
			} catch (Error | RuntimeException e) {
				throw e;
			} catch (Throwable e) {
				throw new AssertionError(e);
			}
		};
	}
	
	private interface ExceptableFunction<T, R> {
		R apply(T t) throws Throwable;
	}
	
	private interface ExceptableSupplier<T> {
		T get() throws Throwable;
	}
	
	private interface ExceptableConsumer<T> {
		void accept(T t) throws Throwable;
	}
	
	private interface ExceptableBiConsumer<T1, T2> {
		void accept(T1 t1, T2 t2) throws Throwable;
	}

}
