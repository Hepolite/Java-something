package com.hepolite.coreutil.hunger;

import java.util.HashMap;
import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class FoodMap extends HashMap<String, FoodData>
{
	private static final long serialVersionUID = -1765236330244617598L;

	/**
	 * Adds the provided food to the collection
	 * 
	 * @param food The food to add
	 * @param group The group to associate the food with; {@code DEFAULT_GROUP} is the default
	 *            group, which will be used if a player is not associated with any particular group
	 */
	public void add(final FoodData food, final String group)
	{
		put(group + ":" + food.name, food);
	}
	/**
	 * Attempts to find the specified food under the given group. If the food was not found under
	 * the given group, the food will be searched for in the default group. If the food has a
	 * special name, the food will be looked up under that name first, then it will be looked up
	 * under its type.
	 * 
	 * @param food The food to search for
	 * @param group The group to search for the food in
	 * @return Returns the food if found
	 */
	public Optional<FoodData> get(final ItemStack food, final String group)
	{
		final ItemMeta meta = food.hasItemMeta() ? food.getItemMeta() : null;
		final String material = getItemType(food);
		final String name = meta != null && meta.hasDisplayName() ? meta.getDisplayName() : material;

		Optional<FoodData> data = get(name, group);
		if (!data.isPresent())
			data = get(material, group);
		return data;
	}
	/**
	 * Attempts to find the specified food under the given group. If the food was not found under
	 * the given group, the food will be searched for in the default group.
	 * 
	 * @param food The food to search for
	 * @param group The group to search for the food in
	 * @return Returns the food if found
	 */
	public Optional<FoodData> get(final String food, final String group)
	{
		FoodData data = get(group + ":" + food);
		if (data == null)
			data = get(GroupRegistry.DEFAULT_GROUP + ":" + food);
		if (data == null)
			return Optional.empty();
		resolveContents(data, group);
		return Optional.of(data);
	}

	/**
	 * Converts the given item into a string key; this might require modification in Minecraft
	 * 1.13+, from that point and outwards, a simple item.getType().toString().toLowerCase() should
	 * be enough
	 * 
	 * @param item The item to typestringify
	 * @return A string key matching the item's type
	 */
	private String getItemType(final ItemStack item)
	{
		final String material = item.getType().toString().toLowerCase();
		final String meta = item.getDurability() == 0 ? "" : "-" + item.getDurability();
		return material + meta;
	}

	/**
	 * Does black magic to figure out all contents of the given food
	 * 
	 * @param data The data to resolve
	 * @param group Main group to resolve for
	 */
	private void resolveContents(final FoodData data, final String group)
	{
		data.ingredients.remove("");
		for (final String ingredient : data.ingredients)
			get(ingredient, group).ifPresent((other) -> data.categories.addAll(other.categories));
		data.categories.remove("");
	}
}
