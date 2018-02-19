package com.hepolite.api.util;

import java.util.function.Function;

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
}
