package com.hepolite.coreutil.util.reflection;

public final class ReflectedClass
{
	public final Class<?> handle;
	public final String name;

	public ReflectedClass(final String name, final Class<?> handle)
	{
		this.handle = handle;
		this.name = name;
	}

	public Object newInstance()
	{
		return null;
	}
	public Object newInstance(final Object... params)
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
