package com.hepolite.coreutil.hunger;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import com.hepolite.api.config.Config;
import com.hepolite.api.config.ConfigFactory;
import com.hepolite.api.config.Property;

public class GroupRegistry
{
	public static final String DEFAULT_GROUP = "Default";

	private final JavaPlugin plugin;
	private final File folder;
	private final GroupMap groups = new GroupMap();

	public GroupRegistry(final JavaPlugin plugin)
	{
		this.plugin = plugin;
		this.folder = new File(plugin.getDataFolder(), "Hunger/Groups");

		ConfigFactory.create(plugin, "Hunger/Groups/Default");
		loadGroups();
	}

	/**
	 * Loads up all group files stored in the given folder
	 * 
	 * @param folder The folder containing all group definition files
	 */
	public void loadGroups()
	{
		groups.clear();
		for (final File file : folder.listFiles())
			loadGroups(file);
	}
	/**
	 * Attempts to load up the group stored in the provided file, or if the file is a folder,
	 * recursively loads all sub-folders and files
	 * 
	 * @param file The file containing group data or a sub-folder
	 */
	private void loadGroups(final File file)
	{
		if (file.isDirectory())
		{
			for (final File child : file.listFiles())
				loadGroups(child);
			return;
		}

		plugin.getLogger().info("Loading group " + file.getName() + "...");

		final String name = file.getName().substring(0, file.getName().lastIndexOf('.'));
		groups.put(name, new Config(file).getValue(new Property(), new GroupData()));
	}

	// ...

	/**
	 * Returns the group with the given name if it exists, returns the default group otherwise
	 */
	public GroupData getGroupData(final String group)
	{
		if (groups.containsKey(group))
			return groups.get(group);
		return groups.get(DEFAULT_GROUP);
	}
}
