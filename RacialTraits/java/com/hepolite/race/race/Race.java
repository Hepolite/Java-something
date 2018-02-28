package com.hepolite.race.race;

import java.util.ArrayList;
import java.util.Collection;

import com.hepolite.race.capability.Capability;

public abstract class Race
{
	private final String name;
	private final Collection<Capability> capabilities = new ArrayList<>();

	public Race(final String name)
	{
		this.name = name;
	}

	/**
	 * @return The name of the race
	 */
	public final String getName()
	{
		return name;
	}

	// ...

	/**
	 * Adds the given capability to the race
	 * 
	 * @param capability The capability to add
	 */
	protected final void addCapability(final Capability capability)
	{
		if (capability == null)
			throw new NullPointerException("Capability cannot be null");
		capabilities.add(capability);
	}
}
