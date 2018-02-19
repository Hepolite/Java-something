package com.hepolite.api.util;

import java.util.function.Function;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class StringConverter
{
	/**
	 * Attempts to convert the given string a generic type using the provided conversion function.
	 * If it fails, the default value is returned
	 * 
	 * @param string The string to convert
	 * @param def The default value
	 * @param func The conversion function
	 * @return The converted value or the default value
	 */
	private static <T> T convert(final String string, final T def, final Function<String, T> func)
	{
		try
		{
			return func.apply(string);
		}
		catch (final Exception e)
		{
			return def;
		}
	}

	/**
	 * Attempts to convert the string to a boolean value; Default value: false
	 * 
	 * @param string The string to convert
	 * @return The converted value or the default value
	 */
	public static boolean toBoolean(final String string)
	{
		return toBoolean(string, false);
	}
	/**
	 * Attempts to convert the string to a boolean value
	 * 
	 * @param string The string to convert
	 * @param def The default value
	 * @return The converted value or the default value
	 */
	public static boolean toBoolean(final String string, final boolean def)
	{
		return convert(string, def, Boolean::parseBoolean);
	}

	/**
	 * Attempts to convert the string to a byte value; Default value: 0
	 * 
	 * @param string The string to convert
	 * @return The converted value or the default value
	 */
	public static byte toByte(final String string)
	{
		return toByte(string, (byte) 0);
	}
	/**
	 * Attempts to convert the string to a byte value
	 * 
	 * @param string The string to convert
	 * @param def The default value
	 * @return The converted value or the default value
	 */
	public static byte toByte(final String string, final byte def)
	{
		return convert(string, def, Byte::parseByte);
	}

	/**
	 * Attempts to convert the string to a double value; Default value: 0.0
	 * 
	 * @param string The string to convert
	 * @return The converted value or the default value
	 */
	public static double toDouble(final String string)
	{
		return toDouble(string, 0.0);
	}
	/**
	 * Attempts to convert the string to a double value
	 * 
	 * @param string The string to convert
	 * @param def The default value
	 * @return The converted value or the default value
	 */
	public static double toDouble(final String string, final double def)
	{
		return convert(string, def, Double::parseDouble);
	}

	/**
	 * Attempts to convert the string to a float value; Default value: 0.0f
	 * 
	 * @param string The string to convert
	 * @return The converted value or the default value
	 */
	public static float toFloat(final String string)
	{
		return toFloat(string, 0.0f);
	}
	/**
	 * Attempts to convert the string to a float value
	 * 
	 * @param string The string to convert
	 * @param def The default value
	 * @return The converted value or the default value
	 */
	public static float toFloat(final String string, final float def)
	{
		return convert(string, def, Float::parseFloat);
	}

	/**
	 * Attempts to convert the string to an int value; Default value: 0
	 * 
	 * @param string The string to convert
	 * @return The converted value or the default value
	 */
	public static int toInt(final String string)
	{
		return toInt(string, 0);
	}
	/**
	 * Attempts to convert the string to an int value
	 * 
	 * @param string The string to convert
	 * @param def The default value
	 * @return The converted value or the default value
	 */
	public static int toInt(final String string, final int def)
	{
		return convert(string, def, Integer::parseInt);
	}

	/**
	 * Attempts to convert the string to a long value; Default value: 0
	 * 
	 * @param string The string to convert
	 * @return The converted value or the default value
	 */
	public static long toLong(final String string)
	{
		return toLong(string, 0L);
	}
	/**
	 * Attempts to convert the string to a long value
	 * 
	 * @param string The string to convert
	 * @param def The default value
	 * @return The converted value or the default value
	 */
	public static long toLong(final String string, final long def)
	{
		return convert(string, def, Long::parseLong);
	}

	/**
	 * Attempts to convert the string to a short value; Default value: 0
	 * 
	 * @param string The string to convert
	 * @return The converted value or the default value
	 */
	public static short toShort(final String string)
	{
		return toShort(string, (short) 0);
	}
	/**
	 * Attempts to convert the string to a short value
	 * 
	 * @param string The string to convert
	 * @param def The default value
	 * @return The converted value or the default value
	 */
	public static short toShort(final String string, final short def)
	{
		return convert(string, def, Short::parseShort);
	}

	// ...

	/**
	 * Attempts to convert the string to a simple item; Default value: air
	 * 
	 * @param string The string to convert
	 * @return The converted value or the default value
	 */
	public static ItemStack toItem(final String string)
	{
		Material material = Material.AIR;
		short meta = 0;
		int amount = 1;
		String name = "";

		try
		{
			String parts[] = string.split("=");
			if (parts.length == 2)
				amount = Integer.parseInt(parts[2]);

			parts = parts[0].split(";");
			if (parts.length == 2)
				name = parts[1];

			parts = parts[0].split("-");
			if (parts.length == 2)
				meta = Short.parseShort(parts[1]);

			material = Material.valueOf(parts[0]);
		}
		catch (final Exception e)
		{}
		final ItemStack item = new ItemStack(material, amount, meta);
		if (!name.isEmpty())
			item.getItemMeta().setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		return item;
	}
	/**
	 * Attempts to convert the item to a simple string on the format type-meta;name=amount. If meta
	 * is 0, it is omitted, if name is not specified, it too is omitted, if the amount is 1 it is
	 * also omitted.
	 * 
	 * @param item The item to convert
	 * @return The resulting string
	 */
	public static String fromItem(final ItemStack item)
	{
		final StringBuilder builder = new StringBuilder(item.getType().toString());
		if (item.getDurability() != 0)
			builder.append("-").append(item.getDurability());
		if (item.getAmount() != 1)
			builder.append("=").append(item.getAmount());
		if (item.hasItemMeta())
		{
			final ItemMeta meta = item.getItemMeta();
			if (meta.hasDisplayName())
				builder.append(";").append(meta.getDisplayName().replaceAll(String.valueOf(ChatColor.COLOR_CHAR), "&"));
		}
		return builder.toString();
	}
}
