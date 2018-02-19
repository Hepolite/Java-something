package com.hepolite.coreutil.hunger;

import java.util.HashMap;
import java.util.UUID;

import com.hepolite.api.config.IConfig;
import com.hepolite.api.config.IProperty;
import com.hepolite.api.config.IValue;

public class HungerMap extends HashMap<UUID, HungerData> implements IValue
{
	private static final long serialVersionUID = 3039842326245977640L;

	@Override
	public void save(final IConfig config, final IProperty property)
	{
		for (final Entry<UUID, HungerData> entry : entrySet())
			config.set(property.child(entry.getKey().toString()), entry.getValue());
	}
	@Override
	public void load(final IConfig config, final IProperty property)
	{
		for (final IProperty user : config.getProperties(property))
			put(UUID.fromString(user.getName()), config.getValue(user, new HungerData()));
	}
}
