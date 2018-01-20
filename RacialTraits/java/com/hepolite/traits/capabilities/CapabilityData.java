package com.hepolite.traits.capabilities;

import com.hepolite.api.config.IConfig;
import com.hepolite.api.config.IProperty;
import com.hepolite.api.config.IValue;
import com.hepolite.traits.config.FieldMap;

public class CapabilityData implements IValue
{
	private final FieldMap fields = new FieldMap();
	private int level;
	private int xp;

	// ...

	@Override
	public void save(final IConfig config, final IProperty property)
	{}
	@Override
	public void load(final IConfig config, final IProperty property)
	{}
}
