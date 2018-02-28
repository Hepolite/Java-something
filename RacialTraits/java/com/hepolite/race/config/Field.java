package com.hepolite.race.config;

import com.hepolite.api.config.IConfig;
import com.hepolite.api.config.IProperty;
import com.hepolite.api.config.IValue;
import com.hepolite.race.capability.CapabilityData;

/**
 * Stores a single value for a {@link CapabilityData} data element
 */
public final class Field implements IValue
{
	public double value = 0.0;

	public Field()
	{}
	public Field(final double value)
	{
		this.value = value;
	}

	@Override
	public void save(final IConfig config, final IProperty property)
	{
		config.set(property, value);
	}
	@Override
	public void load(final IConfig config, final IProperty property)
	{
		value = config.getDouble(property);
	}
}
