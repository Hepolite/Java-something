package com.hepolite.coreutil.hunger;

import java.io.File;
import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.hepolite.api.config.Config;
import com.hepolite.api.config.ConfigFactory;
import com.hepolite.api.config.IProperty;

public final class FoodRegistry
{
	private final JavaPlugin plugin;
	private final File folder;
	private final FoodMap foods = new FoodMap();

	public FoodRegistry(final JavaPlugin plugin)
	{
		this.plugin = plugin;
		this.folder = new File(plugin.getDataFolder(), "Hunger/Consumables");

		ConfigFactory.create(plugin, "Hunger/Consumables/Vanilla");
		ConfigFactory.create(plugin, "Hunger/Consumables/VanillaExtended");
		loadFoods();
	}

	/**
	 * Loads up all food files stored in the given folder
	 * 
	 * @param folder The folder containing all food definition files
	 */
	public void loadFoods()
	{
		foods.clear();
		for (final File file : folder.listFiles())
			loadFoods(file);
	}
	/**
	 * Attempts to load up every food stored in the provided file, or if the file is a folder,
	 * recursively loads all sub-folders and files
	 * 
	 * @param file The file containing food data or a sub-folder
	 */
	private void loadFoods(final File file)
	{
		if (file.isDirectory())
		{
			for (final File child : file.listFiles())
				loadFoods(child);
			return;
		}

		plugin.getLogger().info("Loading foods from " + file.getName() + "...");

		final Config config = new Config(file);
		for (final IProperty group : config.getProperties())
			for (final IProperty food : config.getProperties(group))
				foods.add(config.getValue(food, new FoodData()), group.getName());
	}

	// ...

	/**
	 * Attempts to retrieve the food data associated with the given item, under the given group. The
	 * the food is not a valid food under the give group, the food will be checked against the
	 * default group. If the food has a custom name, the custom name will be prioritized, then the
	 * same search will be performed on the item type.
	 * 
	 * @param item The item to look up
	 * @param group The group to search under
	 * @return The food data associated with the item if any
	 */
	public Optional<FoodData> getFoodData(final ItemStack item, final String group)
	{
		return foods.get(item, group);
	}
	/**
	 * Attempts to retrieve the food data associated with the given item, under the given group. The
	 * the food is not a valid food under the give group, the food will be checked against the
	 * default group.
	 * 
	 * @param item The item to look up
	 * @param group The group to search under
	 * @return The food data associated with the item if any
	 */
	public Optional<FoodData> getFoodData(final String item, final String group)
	{
		return foods.get(item, group);
	}
}
