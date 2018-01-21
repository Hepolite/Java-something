package com.hepolite.coreutil.util.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.hepolite.coreutil.CoreUtilPlugin;

public final class ReflectedMethod
{
	private final Method handle;
	private final String name;

	public ReflectedMethod(final String name, final Method handle)
	{
		this.name = name;
		this.handle = handle;
	}

	/**
	 * Attempts to invoke to method on the provided instance using the provided arguments
	 * 
	 * @param instance The instance to invoke the method on
	 * @param args The arguments for the method
	 * @return The return value of the method or null
	 */
	public Object invoke(final Object instance, final Object... args)
	{
		try
		{
			return handle.invoke(instance, args);
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			CoreUtilPlugin.INSTANCE.getLogger()
					.info(String.format("Failed to invoke '%s' using signature '%s'", name, signature(args)));
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

	// ...

	@Override
	public String toString()
	{
		if (handle == null)
			return String.format("[%s; invalid]", name);
		return String.format("[%s; %s]", name, handle.toString());
	}
}
