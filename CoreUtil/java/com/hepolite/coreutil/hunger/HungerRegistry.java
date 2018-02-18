package com.hepolite.coreutil.hunger;

import org.bukkit.plugin.java.JavaPlugin;

import com.hepolite.api.config.Config;
import com.hepolite.api.config.IConfig;
import com.hepolite.api.config.Property;
import com.hepolite.api.user.IUser;

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
	 * Returns whether the given user has a hunger data node or not
	 */
	public boolean hasHungerData(final IUser user)
	{
		return users.containsKey(user);
	}
	/**
	 * Returns the data that is associated with the given user, creating it if it does not exist
	 */
	public HungerData getHungerData(final IUser user)
	{
		if (!users.containsKey(user))
			users.put(user, new HungerData());
		return users.get(user);
	}
}
