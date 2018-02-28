package com.hepolite.race.capability;

import org.bukkit.entity.Player;

import com.hepolite.api.config.ConfigFactory;
import com.hepolite.api.config.IConfig;
import com.hepolite.race.RacialTraitsPlugin;
import com.hepolite.race.race.Race;

public abstract class Capability
{
	private final String name;
	private final CapabilityType type;

	protected final IConfig config;
	private final Class<? extends CapabilityData> dataClass;

	public Capability(final Race race, final CapabilityType type, final String name,
			final Class<? extends CapabilityData> dataClass)
	{
		this.name = name;
		this.type = type;

		this.config = ConfigFactory.create(RacialTraitsPlugin.getInstance(), race.getName() + "/" + name);
		this.dataClass = dataClass;
	}

	/**
	 * @return The name of the capability
	 */
	public final String getName()
	{
		return name;
	}
	/**
	 * @return The type of the capability
	 */
	public final CapabilityType getType()
	{
		return type;
	}

	/**
	 * @return The data of the capability
	 */
	public final CapabilityData createData()
	{
		try
		{
			return dataClass.newInstance();
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			RacialTraitsPlugin.WARN(String.format("Could not create capability data for '%s'", name));
			return null;
		}
	}

	// ...

	/**
	 * Invoked whenever the player is using the capability through actively casting it. If the
	 * capability was successfully cast, resources required to cast the capability will
	 * automatically be consumed, and it will go on cooldown if relevant.
	 * 
	 * @param player The player who cast the capability
	 * @param data The data associated with the capability
	 * @return True iff the ability was successfully cast
	 */
	public boolean onCast(final Player player, final CapabilityData data)
	{
		return false;
	}
	/**
	 * Invoked every tick for every player who has the capability unlocked, or has the capability as
	 * a trait.
	 * 
	 * @param player The player who has the capability
	 * @param data The data associated with the capability
	 * @param tick The current plugin tick
	 */
	public void onTick(final Player player, final CapabilityData data, final int tick)
	{}
}
