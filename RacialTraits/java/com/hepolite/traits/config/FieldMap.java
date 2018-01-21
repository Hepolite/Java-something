package com.hepolite.traits.config;

import java.util.HashMap;

import com.hepolite.api.config.IConfig;
import com.hepolite.api.config.IProperty;
import com.hepolite.api.config.IValue;
import com.hepolite.traits.capabilities.Capability;

/**
 * Groups a collection of {@link Field}s together, used to track the various attributes associated
 * with any player {@link Capability}
 */
public class FieldMap extends HashMap<String, Field> implements IValue
{
	private static final long serialVersionUID = -2308184690433893410L;

	@Override
	public void save(final IConfig config, final IProperty property)
	{
		for (final Entry<String, Field> entry : entrySet())
			config.set(property.child(entry.getKey()), entry.getValue());
	}
	@Override
	public void load(final IConfig config, final IProperty property)
	{
		clear();
		for (final IProperty p : config.getProperties(property))
			put(p.getName(), config.getValue(p, new Field()));
	}
}
