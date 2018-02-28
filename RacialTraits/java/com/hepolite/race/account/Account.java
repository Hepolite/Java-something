package com.hepolite.race.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import com.hepolite.api.config.IConfig;
import com.hepolite.api.config.IProperty;
import com.hepolite.api.config.IValue;
import com.hepolite.race.capability.Capability;
import com.hepolite.race.capability.CapabilityData;
import com.hepolite.race.race.Race;
import com.hepolite.race.race.Races;

public class Account implements IValue
{
	/// @formatter:off
	//
	// CONTENTS OF ACCOUNT DATA:
	//
	// Attribute data:
	//	For each attribute:
	//		The current attribute level
	//	Number of remaining attribute tokens
	//
	// Racial data:
	//	The current level
	//	Unlocked skills
	//	Number of remaining skill tokens
	//
	// Capability data:
	//	For each capability:
	//		The current capability level
	//		All field data
	//		Experience gained with the capability
	//
	/// @formatter:on

	private final ArrayList<Data> data = new ArrayList<>();
	private int active = 0;

	public Account()
	{
		data.add(new Data());
	}

	/**
	 * @return The id of the currently active data
	 */
	public int getActiveId()
	{
		return active;
	}

	/**
	 * Attempts to retrieve the currently active data, if it exists
	 * 
	 * @return The active data if it exists
	 */
	public Optional<Data> getData()
	{
		return getData(active);
	}
	/**
	 * Attempts to retrieve the data associated with the given id
	 * 
	 * @param id The id to loop up
	 * @return The data with the given id if it exists
	 */
	public Optional<Data> getData(final int id)
	{
		if (id < 0 || id >= data.size())
			return Optional.empty();
		return Optional.of(data.get(id));
	}

	// ...

	@Override
	public void save(final IConfig config, final IProperty property)
	{
		config.set(property.child("active"), active);
		for (int i = 0; i < data.size(); ++i)
			config.set(property.child(Integer.toString(i)), data.get(i));
	}
	@Override
	public void load(final IConfig config, final IProperty property)
	{
		data.clear();
		active = config.getInt(property.child("active"));
		for (final IProperty entry : config.getProperties(property))
			data.add(config.getValue(entry, new Data()));
	}

	// ...

	public static final class Data implements IValue
	{
		public Race race;
		public int level, xp;
		public int skillTokens, attributeTokens;
		public final Map<Capability, CapabilityData> capabilities = new HashMap<>();

		// ...

		@Override
		public void save(final IConfig config, final IProperty property)
		{
			config.set(property.child("race"), race == null ? "None" : race.getName());
			config.set(property.child("level"), level);
			config.set(property.child("xp"), xp);
			config.set(property.child("skillTokens"), skillTokens);
			config.set(property.child("attributeTokens"), attributeTokens);
			for (final Entry<Capability, CapabilityData> entry : capabilities.entrySet())
				config.set(property.child("capabilities").child(entry.getKey().getName()), entry.getValue());
		}
		@Override
		public void load(final IConfig config, final IProperty property)
		{
			race = Races.get(config.getString(property.child("race"))).orElse(null);
			level = config.getInt(property.child("level"));
			xp = config.getInt(property.child("xp"));
			skillTokens = config.getInt(property.child("skillTokens"));
			attributeTokens = config.getInt(property.child("attributeTokens"));
			// TODO: Load capability data
		}
	}
}
