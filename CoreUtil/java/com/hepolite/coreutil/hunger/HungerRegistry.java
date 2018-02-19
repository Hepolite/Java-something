package com.hepolite.coreutil.hunger;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.hepolite.api.config.Config;
import com.hepolite.api.config.IConfig;
import com.hepolite.api.config.Property;

public final class HungerRegistry
{
	private final HungerMap users;
	private final IConfig config;

	public HungerRegistry(final JavaPlugin plugin)
	{
		config = new Config(plugin.getDataFolder(), "Hunger/Users");
		users = config.getValue(new Property("Users"), new HungerMap());
	}

	/**
	 * Stores all player hunger data and other hunger-related data to disk
	 */
	public void saveData()
	{
		config.set(new Property("Users"), users);
		config.saveToDisk();
	}

	// ...

	/**
	 * Returns whether the given player has a hunger data node or not
	 */
	public boolean hasHungerData(final Player player)
	{
		return users.containsKey(player.getUniqueId());
	}
	/**
	 * Returns the data that is associated with the given player, creating it if it does not exist
	 */
	public HungerData getHungerData(final Player player)
	{
		final UUID uuid = player.getUniqueId();
		if (!users.containsKey(uuid))
			users.put(uuid, new HungerData());
		return users.get(uuid);
	}
}
