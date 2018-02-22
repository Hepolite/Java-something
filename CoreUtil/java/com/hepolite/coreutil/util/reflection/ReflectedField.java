package com.hepolite.coreutil.util.reflection;

import java.lang.reflect.Field;

import com.hepolite.coreutil.CoreUtilPlugin;

public final class ReflectedField
{
	private final Field handle;
	private final String name;

	public ReflectedField(final String name, final Field handle)
	{
		this.name = name;
		this.handle = handle;
	}

	/**
	 * Attempts to retrieve the value stored in the field under the given instance
	 * 
	 * @param instance The instance to read from
	 * @return The value or null
	 */
	public Object get(final Object instance)
	{
		try
		{
			return handle.get(instance);
		}
		catch (IllegalArgumentException | IllegalAccessException e)
		{
			CoreUtilPlugin.WARN("[ReflectUtil] Failed to read field " + name);
			return null;
		}
	}
	/**
	 * Attempts to assign the field value under the provided instance
	 * 
	 * @param instance The instance to write to
	 * @param value The value to write
	 */
	public void set(final Object instance, final Object value)
	{
		try
		{
			handle.set(instance, value);
		}
		catch (IllegalArgumentException | IllegalAccessException e)
		{
			CoreUtilPlugin.WARN(String.format("[ReflectUtil] Failed to set field '%s' to '%s'", name, value));
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
