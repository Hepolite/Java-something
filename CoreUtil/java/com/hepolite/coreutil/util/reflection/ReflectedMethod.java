package com.hepolite.coreutil.util.reflection;

import java.lang.reflect.Method;

public final class ReflectedMethod
{
	private final Method handle;
	private final String name;

	public ReflectedMethod(final String name, final Method handle)
	{
		this.name = name;
		this.handle = handle;
	}

	public Object invoke(final Object handle, final Object... args)
	{
		return null;
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
