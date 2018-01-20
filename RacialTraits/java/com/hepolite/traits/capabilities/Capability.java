package com.hepolite.traits.capabilities;

import com.hepolite.api.config.ConfigFactory;
import com.hepolite.api.config.IConfig;
import com.hepolite.traits.TraitsPlugin;

public abstract class Capability
{
	private final String name;
	private final IConfig config;

	public Capability(final String name)
	{
		this.name = name;
		this.config = ConfigFactory.create(TraitsPlugin.getInstance(), name);
	}

	/**
	 * @return The name of the capability
	 */
	public final String getName()
	{
		return name;
	}
}
