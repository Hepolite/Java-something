package com.hepolite.coreutil.util.reflection;

import com.hepolite.coreutil.CoreUtilPlugin;

public final class ReflectedClass
{
	public final Class<?> handle;
	public final String name;

	public ReflectedClass(final String name, final Class<?> handle)
	{
		this.handle = handle;
		this.name = name;
	}

	/**
	 * Attempts to create a new instance of the underlying class
	 * 
	 * @return The instance if it was created, null otherwise
	 */
	public Object newInstance()
	{
		try
		{
			return handle.newInstance();
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			CoreUtilPlugin.WARN("Failed to instantiate class " + name);
			return null;
		}
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
