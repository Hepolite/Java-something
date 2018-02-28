package com.hepolite.race.race;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Races
{
	private static final Map<String, Race> races = new HashMap<>();

	// ...

	public static final RaceWolf WOLF = register(new RaceWolf());

	// ...

	/**
	 * Registers the given race into the system
	 * 
	 * @param race The race to add
	 * @return The same race for convenience
	 */
	private static final <T extends Race> T register(final T race)
	{
		races.put(race.getName().toLowerCase(), race);
		return race;
	}

	/**
	 * Attempts to retrieve the race of the given name
	 * 
	 * @param name The name of the race to retrieve
	 * @return The race if it exists
	 */
	public static final Optional<Race> get(final String name)
	{
		return Optional.ofNullable(races.get(name.toLowerCase()));
	}
	/**
	 * @return A collection containing all races in the system
	 */
	public static final Collection<Race> getAll()
	{
		return Collections.unmodifiableCollection(races.values());
	}
}
