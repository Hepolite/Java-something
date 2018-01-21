package com.hepolite.coreutil.util.reflection;

import java.lang.reflect.Field;

public final class ReflectedField
{
	private final Field handle;
	private final String name;

	public ReflectedField(final String name, final Field handle)
	{
		this.name = name;
		this.handle = handle;
	}

	public Object get(final Object instance)
	{
		return null;
	}
	public void set(final Object instance, final Object value)
	{}

	// ...

	@Override
	public String toString()
	{
		if (handle == null)
			return String.format("[%s; invalid]", name);
		return String.format("[%s; %s]", name, handle.toString());
	}
}
