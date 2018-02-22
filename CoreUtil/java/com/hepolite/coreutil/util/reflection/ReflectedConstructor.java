package com.hepolite.coreutil.util.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.hepolite.coreutil.CoreUtilPlugin;

public class ReflectedConstructor
{
	private final ReflectedClass reflectedClass;
	private final Constructor<?> handle;

	public ReflectedConstructor(final ReflectedClass reflectedClass, final Constructor<?> handle)
	{
		this.reflectedClass = reflectedClass;
		this.handle = handle;
		if (handle != null)
			handle.setAccessible(true);
	}

	/**
	 * Attempts to instantiate a new instance from the underlying constructor using the provided
	 * arguments
	 * 
	 * @param args The arguments to instantiate from
	 * @return The new instance or null
	 */
	public final Object instantiate(final Object... args)
	{
		try
		{
			return handle.newInstance(args);
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			CoreUtilPlugin
					.WARN(String.format("[ReflectUtil] Failed to instantiate from class '%s', using signature '%s'",
							reflectedClass.name, signature(args)));
			return null;
		}
	}

	/**
	 * Returns the string version of the signature that is composed of the provided objects
	 * 
	 * @param args The objects forming the signature
	 * @return A string representation of the signature
	 */
	private String signature(final Object... args)
	{
		final StringBuilder builder = new StringBuilder();
		for (final Object object : args)
		{
			if (builder.length() != 0)
				builder.append(", ");
			builder.append(object.getClass().getName());
		}
		if (builder.length() == 0)
			builder.append("void");
		return builder.toString();
	}
}
