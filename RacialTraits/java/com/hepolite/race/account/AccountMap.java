package com.hepolite.race.account;

import java.util.HashMap;
import java.util.UUID;

import com.hepolite.api.config.IConfig;
import com.hepolite.api.config.IProperty;
import com.hepolite.api.config.IValue;

public class AccountMap extends HashMap<UUID, Account> implements IValue
{
	private static final long serialVersionUID = -7219632154848365265L;

	@Override
	public void save(final IConfig config, final IProperty property)
	{
		for (final Entry<UUID, Account> entry : entrySet())
			config.set(property.child(entry.getKey().toString()), entry.getValue());
	}
	@Override
	public void load(final IConfig config, final IProperty property)
	{
		for (final IProperty entry : config.getProperties(property))
			put(UUID.fromString(entry.getName()), config.getValue(entry, new Account()));
	}
}
