package com.hepolite.api.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Charsets;

public class ConfigFactory
{
	/**
	 * Constructs a new plugin using the given plugin and name. If the config has been specified
	 * inside the plugin jar and the config does not exist as a file, the config will be created in
	 * the plugin data directory. The resulting config will load that file before being returned.
	 * 
	 * @param plugin The plugin to retrieve the config from
	 * @param path The path to the config, relative to the data directory
	 * @return The config corresponding to the path under the given plugin
	 */
	public static final IConfig create(final JavaPlugin plugin, final String path)
	{
		tryCreateConfigFile(plugin, path);
		return new Config(plugin.getDataFolder(), path);
	}
	private static final void tryCreateConfigFile(final JavaPlugin plugin, final String path)
	{
		final InputStream defConfigStream = plugin.getResource("config/" + path + ".yml");
		if (defConfigStream == null)
		{
			plugin.getLogger().warning("Could not find config resource " + path + ", absorting..");
			return;
		}

		final File file = new File(plugin.getDataFolder() + "/" + path + ".yml");
		if (file.exists())
		{
			plugin.getLogger().info("Found existing config file " + path);
			return;
		}

		try
		{
			final InputStreamReader reader = new InputStreamReader(defConfigStream, Charsets.UTF_8);
			YamlConfiguration.loadConfiguration(reader).save(file);
			plugin.getLogger().info("Created config file " + path + "!");
		}
		catch (final IOException e)
		{
			plugin.getLogger().warning("Failed to save internal config file " + path + "!");
		}
	}
}
