package com.hepolite.coreutil.hunger;

import java.util.HashMap;

import com.hepolite.api.config.IConfig;
import com.hepolite.api.config.IProperty;
import com.hepolite.api.config.IValue;
import com.hepolite.api.user.IUser;
import com.hepolite.api.user.UserFactory;

public class HungerMap extends HashMap<IUser, HungerData> implements IValue
{
	private static final long serialVersionUID = 3039842326245977640L;

	@Override
	public void save(final IConfig config, final IProperty property)
	{
		for (final Entry<IUser, HungerData> entry : entrySet())
			config.set(property.child(entry.getKey().toString()), entry.getValue());
	}
	@Override
	public void load(final IConfig config, final IProperty property)
	{
		for (final IProperty user : config.getProperties(property))
			put(UserFactory.fromUUID(user.getName()), config.getValue(user, new HungerData()));
	}
}
