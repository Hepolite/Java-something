package com.hepolite.api.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class InventoryHelper
{
	public static short ANY_META = 32767;

	// ...

	/**
	 * Returns the name of the given item, or an empty string if it had no name
	 * 
	 * @param item The item to look into
	 * @return The name of the item
	 */
	public static String getItemName(final ItemStack item)
	{
		if (!item.hasItemMeta())
			return null;
		final ItemMeta meta = item.getItemMeta();
		return meta.hasDisplayName() ? meta.getDisplayName() : "";
	}

	/**
	 * Returns true iff the names of the two items are identical
	 * 
	 * @param itemA The first item
	 * @param itemB The second item
	 * @return True iff the items' names match
	 */
	public static boolean areNamesEqual(final ItemStack itemA, final ItemStack itemB)
	{
		return getItemName(itemA).equals(getItemName(itemB));
	}
	/**
	 * Returns true iff the meta value of the two items are identical
	 * 
	 * @param itemA The first item
	 * @param itemB The second item
	 * @return True iff the meta values were the same
	 */
	public static boolean areMetaEqual(final ItemStack itemA, final ItemStack itemB)
	{
		final short metaA = itemA.getDurability();
		final short metaB = itemB.getDurability();
		return (metaA == metaB || metaA == ANY_META || metaB == ANY_META);
	}
	/**
	 * Returns true iff the two items were the same type
	 * 
	 * @param itemA The first item
	 * @param itemB The second item
	 * @return Tru iff the items were the same type
	 */
	public static boolean areTypesEqual(final ItemStack itemA, final ItemStack itemB)
	{
		return itemA.getType().equals(itemB.getType());
	}
	/**
	 * Returns true iff the two items are of the same type, has the same meta and same name
	 * 
	 * @param itemA The first item
	 * @param itemB The second item
	 * @return True iff the items are similar
	 */
	public static boolean areItemsSimilar(final ItemStack itemA, final ItemStack itemB)
	{
		return areTypesEqual(itemA, itemB) && areMetaEqual(itemA, itemB) && areNamesEqual(itemA, itemB);
	}

	/**
	 * Returns true iff the sub item can be derived from the main item. This is the case when the
	 * meta and types are similar, and when the name requirement is met. If the main item has a
	 * name, the sub item is considered a subtype if it then has the same name. If the main item
	 * does not have a name, the method will return true provided the initial condition was true.
	 * 
	 * @param main The main item
	 * @param sub The sub item
	 * @return True iff the sub item is actually a sub item
	 */
	public static boolean isItemSubType(final ItemStack main, final ItemStack sub)
	{
		if (!areTypesEqual(main, sub) || !areMetaEqual(main, sub))
			return false;
		final String nameMain = getItemName(main);
		final String nameSub = getItemName(sub);
		return nameMain == null || (nameSub != null && nameMain.equals(nameSub));
	}

	// ...

	/**
	 * Checks if the given inventory has the provided items; if it does not, the missing items are
	 * returned
	 * 
	 * @param inventory The inventory to look into
	 * @param item The items to look for
	 * @return The missing items or an empty collection if nothing was missing
	 */
	public static Collection<ItemStack> check(final Inventory inventory, final Collection<ItemStack> items)
	{
		final Collection<ItemStack> missing = new ArrayList<>();
		for (final ItemStack item : items)
		{
			final int remaining = check(inventory, item);
			if (remaining > 0)
			{
				final ItemStack stack = item.clone();
				stack.setAmount(remaining);
				missing.add(stack);
			}
		}
		return missing;
	}
	/**
	 * Checks if the given inventory has the provided item; if it does not, the number of missing
	 * items is returned
	 * 
	 * @param inventory The inventory to look into
	 * @param item The item to look for
	 * @return The number of missing items
	 */
	public static int check(final Inventory inventory, final ItemStack item)
	{
		int remaining = item.getAmount();

		for (final ItemStack content : inventory.getContents())
		{
			if (content == null || !isItemSubType(item, content))
				continue;

			remaining -= content.getAmount();
			if (remaining <= 0)
				return 0;
		}
		return remaining;
	}
	/**
	 * Checks if the given player has the provided items; if it does not, the missing items are
	 * returned
	 * 
	 * @param player The player to look into
	 * @param item The items to look for
	 * @return The missing items or an empty collection if nothing was missing
	 */
	public static Collection<ItemStack> check(final Player player, final Collection<ItemStack> items)
	{
		return check(player.getInventory(), items);
	}
	/**
	 * Checks if the given player has the provided item; if it does not, the number of missing items
	 * is returned
	 * 
	 * @param player The player to look into
	 * @param item The item to look for
	 * @return The number of missing items
	 */
	public static int check(final Player player, final ItemStack item)
	{
		return check(player.getInventory(), item);
	}

	/**
	 * Removes the given item from the given inventory
	 * 
	 * @param inventory The inventory to take from
	 * @param item The item to take from the inventory
	 */
	public static void take(final Inventory inventory, final ItemStack item)
	{
		int remaining = item.getAmount();

		for (int i = 0; i < inventory.getSize(); ++i)
		{
			ItemStack content = inventory.getItem(i);
			if (content == null || !isItemSubType(item, content))
				continue;

			final int amount = content.getAmount();
			content.setAmount(amount - remaining);
			if (content.getAmount() <= 0)
				content = null;
			inventory.setItem(i, content);

			remaining -= amount;
			if (remaining <= 0)
				return;
		}
	}
	/**
	 * Removes the given items from the given inventory
	 * 
	 * @param inventory The inventory to take from
	 * @param item The items to take from the inventory
	 */
	public static void take(final Inventory inventory, final Collection<ItemStack> items)
	{
		for (final ItemStack item : items)
			take(inventory, item);
	}
	/**
	 * Removes the given item from the given player
	 * 
	 * @param inventory The inventory to take from
	 * @param item The item to take from the player
	 */
	public static void take(final Player player, final ItemStack item)
	{
		take(player.getInventory(), item);
	}
	/**
	 * Removes the given items from the given player
	 * 
	 * @param inventory The inventory to take from
	 * @param items The items to take from the player
	 */
	public static void take(final Player player, final Collection<ItemStack> items)
	{
		take(player.getInventory(), items);
	}

	/**
	 * Adds the given item to the given inventory, dropping the excess items at the given location
	 * if the inventory did not have enough room for them. If the location is null, excess items are
	 * discarded.
	 * 
	 * @param inventory The inventory to add to
	 * @param item The item to add
	 * @param location The location where the items should be dropped if they did not fit
	 */
	public static void add(final Inventory inventory, final ItemStack item, final Location location)
	{
		add(inventory, Arrays.asList(item), location);
	}
	/**
	 * Adds the given items to the given inventory, discarding all items that could not fit
	 * 
	 * @param inventory The inventory to add to
	 * @param item The item to add
	 */
	public static void add(final Inventory inventory, final ItemStack item)
	{
		add(inventory, item, null);
	}
	/**
	 * Adds the given items to the given inventory, dropping the excess items at the given location
	 * if the inventory did not have enough room for them. If the location is null, excess items are
	 * discarded.
	 * 
	 * @param inventory The inventory to add to
	 * @param items The items to add
	 * @param location The location where the items should be dropped if they did not fit
	 */
	public static void add(final Inventory inventory, final Collection<ItemStack> items, final Location location)
	{
		final Map<Integer, ItemStack> remaining = inventory.addItem(items.toArray(new ItemStack[0]));

		if (location != null)
		{
			for (final ItemStack item : remaining.values())
				location.getWorld().dropItem(location, item);
		}
	}
	/**
	 * Adds the given items to the given inventory, discarding all items that could not fit
	 * 
	 * @param inventory The inventory to add to
	 * @param items The items to add
	 */
	public static void add(final Inventory inventory, final Collection<ItemStack> items)
	{
		add(inventory, items, null);
	}
	/**
	 * Adds the given item to the given player, dropping the excess items at the given location if
	 * the player did not have enough room for them. If the location is null, excess items are
	 * discarded.
	 * 
	 * @param player The player to add to
	 * @param item The item to add
	 * @param location The location where the items should be dropped if they did not fit
	 */
	public static void add(final Player player, final ItemStack item, final Location location)
	{
		add(player.getInventory(), item, location);
	}
	/**
	 * Adds the given items to the given inventory, discarding all items that could not fit
	 * 
	 * @param inventory The inventory to add to
	 * @param item The item to add
	 */
	public static void add(final Player player, final ItemStack item)
	{
		add(player.getInventory(), item, null);
	}
	/**
	 * Adds the given items to the given player, dropping the excess items at the given location if
	 * the player did not have enough room for them. If the location is null, excess items are
	 * discarded.
	 * 
	 * @param player The player to add to
	 * @param items The items to add
	 * @param location The location where the items should be dropped if they did not fit
	 */
	public static void add(final Player player, final Collection<ItemStack> items, final Location location)
	{
		add(player.getInventory(), items, location);
	}
	/**
	 * Adds the given items to the given player, discarding all items that could not fit
	 * 
	 * @param player The player to add to
	 * @param items The items to add
	 */
	public static void add(final Player player, final Collection<ItemStack> items)
	{
		add(player.getInventory(), items, null);
	}

	// ...

	/**
	 * Returns true iff the item can normally be consumed by a player
	 * 
	 * @param item The item to check
	 * @return True iff the player can consume the item
	 */
	public static boolean isConsumable(final ItemStack item)
	{
		return isEdible(item) || isDrinkable(item);
	}
	/**
	 * Returns true iff the item can normally be eaten by a player
	 * 
	 * @param item The item to check
	 * @return True iff the player can eat the item
	 */
	public static boolean isEdible(final ItemStack item)
	{
		return item.getType().isEdible();
	}
	/**
	 * Returns true iff the item can normally be drunk by a player
	 * 
	 * @param item The item to check
	 * @return True iff the player can drink the item
	 */
	public static boolean isDrinkable(final ItemStack item)
	{
		switch (item.getType())
		{
		case POTION:
		case MILK_BUCKET:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Returns true iff the item is normally treated as a weapon
	 * 
	 * @param item The item to check
	 * @return True iff the item is a weapon
	 */
	public static boolean isWeapon(final ItemStack item)
	{
		return isBow(item) || isSword(item);
	}
	/**
	 * Returns true iff the item is normally treated as a bow
	 * 
	 * @param item The item to check
	 * @return True iff the item is a bow
	 */
	public static boolean isBow(final ItemStack item)
	{
		switch (item.getType())
		{
		case BOW:
			return true;
		default:
			return false;
		}
	}
	/**
	 * Returns true iff the item is normally treated as a sword
	 * 
	 * @param item The item to check
	 * @return True iff the item is a sword
	 */
	public static boolean isSword(final ItemStack item)
	{
		switch (item.getType())
		{
		case WOOD_SWORD:
		case GOLD_SWORD:
		case STONE_SWORD:
		case IRON_SWORD:
		case DIAMOND_SWORD:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Returns true iff the item is normally treated as a tool
	 * 
	 * @param item The item to check
	 * @return True iff the item is a tool
	 */
	public static boolean isTool(final ItemStack item)
	{
		if (isAxe(item) || isPickaxe(item) || isShovel(item) || isHoe(item))
			return true;
		switch (item.getType())
		{
		case FLINT_AND_STEEL:
		case FISHING_ROD:
		case SHEARS:
			return true;
		default:
			return false;
		}
	}
	/**
	 * Returns true iff the item is normally treated as an axe
	 * 
	 * @param item The item to check
	 * @return True iff the item is an axe
	 */
	public static boolean isAxe(final ItemStack item)
	{
		switch (item.getType())
		{
		case WOOD_AXE:
		case GOLD_AXE:
		case STONE_AXE:
		case IRON_AXE:
		case DIAMOND_AXE:
			return true;
		default:
			return false;
		}
	}
	/**
	 * Returns true iff the item is normally treated as a pickaxe
	 * 
	 * @param item The item to check
	 * @return True iff the item is a pickaxe
	 */
	public static boolean isPickaxe(final ItemStack item)
	{
		switch (item.getType())
		{
		case WOOD_PICKAXE:
		case GOLD_PICKAXE:
		case STONE_PICKAXE:
		case IRON_PICKAXE:
		case DIAMOND_PICKAXE:
			return true;
		default:
			return false;
		}
	}
	/**
	 * Returns true iff the item is normally treated as a shovel
	 * 
	 * @param item The item to check
	 * @return True iff the item is a shovel
	 */
	public static boolean isShovel(final ItemStack item)
	{
		switch (item.getType())
		{
		case WOOD_SPADE:
		case GOLD_SPADE:
		case STONE_SPADE:
		case IRON_SPADE:
		case DIAMOND_SPADE:
			return true;
		default:
			return false;
		}
	}
	/**
	 * Returns true iff the item is normally treated as a hoe
	 * 
	 * @param item The item to check
	 * @return True iff the item is a hoe
	 */
	public static boolean isHoe(final ItemStack item)
	{
		switch (item.getType())
		{
		case WOOD_HOE:
		case GOLD_HOE:
		case STONE_HOE:
		case IRON_HOE:
		case DIAMOND_HOE:
			return true;
		default:
			return false;
		}
	}
}
