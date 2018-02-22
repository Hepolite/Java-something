package com.hepolite.api.util;

/**
 * Works the same as a BiFuction, only with more inputs
 *
 * @param <T> First parameter type
 * @param <U> Second parameter type
 * @param <V> Third parameter type
 * @param <R> Return type
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R>
{
	public abstract R apply(T t, U u, V v);
}
