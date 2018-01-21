package com.hepolite.coreutil.util.reflection;

import java.lang.reflect.Constructor;

public class ReflectedConstructor
{
	private final Constructor<?> handle;

	public ReflectedConstructor(final Constructor<?> handle)
	{
		this.handle = handle;
		if (handle != null)
			handle.setAccessible(true);
	}
}
