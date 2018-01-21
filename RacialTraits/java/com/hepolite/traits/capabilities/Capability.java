package com.hepolite.traits.capabilities;

import org.bukkit.entity.Player;

import com.hepolite.api.config.ConfigFactory;
import com.hepolite.api.config.IConfig;
import com.hepolite.traits.TraitsPlugin;

public abstract class Capability
{
	private final String name;
	protected final IConfig config;

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
