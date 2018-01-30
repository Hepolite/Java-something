package com.hepolite.api.cmd;

import com.hepolite.api.cmd.element.CmdCommandElement;
import com.hepolite.api.cmd.element.CmdFirstParsingElement;
import com.hepolite.api.cmd.element.CmdNumberElement;
import com.hepolite.api.cmd.element.CmdOptionalElement;
import com.hepolite.api.cmd.element.CmdPlayerElement;
import com.hepolite.api.cmd.element.CmdPlayerOrUserElement;
import com.hepolite.api.cmd.element.CmdRequireUserTypeElement;
import com.hepolite.api.cmd.element.CmdRequireUserTypeElement.UserType;
import com.hepolite.api.cmd.element.CmdSequenceElement;
import com.hepolite.api.cmd.element.CmdStringElement;
import com.hepolite.api.cmd.element.ICmdElement;

public class GenericArgs
{
	/**
	 * Consumes a series of arguments. Usages is the matching child's elements concatenated.
	 * 
	 * @param children The sub-commands to register
	 * @return The element to match the input
	 */
	public static final ICmdElement children(final ICmd... children)
	{
		return new CmdCommandElement(children);
	}

	/**
	 * Consumes a series of arguments. Usage is the elements concatenated.
	 * 
	 * @param elements The sequence that is required
	 * @return The element to match the input
	 */
	public static final ICmdElement sequence(final ICmdElement... elements)
	{
		return new CmdSequenceElement(elements);
	}
	/**
	 * Consumes a series of arguments, if they are present. Usage is the elements concatenated.
	 * 
	 * @param elements The elements that are optional
	 * @return The element to match the input
	 */
	public static final ICmdElement optional(final ICmdElement... elements)
	{
		return new CmdOptionalElement(elements);
	}
	/**
	 * Consumes a series of arguments matching the first element that parses. Usage is either of the
	 * elements
	 * 
	 * @param elements The elements that are to be branched from
	 * @return The element to match the input
	 */
	public static final ICmdElement firstParsing(final ICmdElement... elements)
	{
		return new CmdFirstParsingElement(elements);
	}

	/**
	 * Requires an argument to be a byte. The value may be specified as base 2, 10 or 16.
	 * 
	 * @param key The key to store the value under
	 * @return The element to match the input
	 */
	public static final ICmdElement byteNum(final String key)
	{
		return new CmdNumberElement<>(key, Byte::parseByte, Byte::parseByte, "Expected a byte, but '%s' was not");
	}
	/**
	 * Requires an argument to be a double.
	 * 
	 * @param key The key to store the value under
	 * @return The element to match the input
	 */
	public static final ICmdElement doubleNum(final String key)
	{
		return new CmdNumberElement<>(key, Double::parseDouble, null, "Expected a double, but '%s' was not");
	}
	/**
	 * Requires an argument to be a float.
	 * 
	 * @param key The key to store the value under
	 * @return The element to match the input
	 */
	public static final ICmdElement floatNum(final String key)
	{
		return new CmdNumberElement<>(key, Float::parseFloat, null, "Expected a float, but '%s' was not");
	}
	/**
	 * Requires an argument to be an integer. The value may be specified as base 2, 10 or 16.
	 * 
	 * @param key The key to store the value under
	 * @return The element to match the input
	 */
	public static final ICmdElement intNum(final String key)
	{
		return new CmdNumberElement<>(key, Integer::parseInt, Integer::parseInt, "Expected an int, but '%s' was not");
	}
	/**
	 * Requires an argument to be a long. The value may be specified as base 2, 10 or 16.
	 * 
	 * @param key The key to store the value under
	 * @return The element to match the input
	 */
	public static final ICmdElement longNum(final String key)
	{
		return new CmdNumberElement<>(key, Long::parseLong, Long::parseLong, "Expected a long, but '%s' was not");
	}
	/**
	 * Requires an argument to be a short. The value may be specified as base 2, 10 or 16.
	 * 
	 * @param key The key to store the value under
	 * @return The element to match the input
	 */
	public static final ICmdElement shortNum(final String key)
	{
		return new CmdNumberElement<>(key, Short::parseShort, Short::parseShort, "Expected a short, but '%s' was not");
	}

	/**
	 * Requires an argument to be a string.
	 * 
	 * @param key The key to store the value under
	 * @return The element to match the input
	 */
	public static final ICmdElement string(final String key)
	{
		return new CmdStringElement.One(key);
	}
	/**
	 * Consumes all remaining arguments, concatenated to a single string
	 * 
	 * @param key The key to store the value under
	 * @return The element to match the input
	 */
	public static final ICmdElement remainingStrings(final String key)
	{
		return new CmdStringElement.Remaining(key);
	}

	/**
	 * Requires an argument to be a player.
	 * 
	 * @param key The key to store the value under
	 * @return The element to match the input
	 */
	public static final ICmdElement player(final String key)
	{
		return new CmdPlayerElement(key);
	}
	/**
	 * Requires an argument to be a player. Selects the user if no valid player was given.
	 * 
	 * @param key The key to store the value under
	 * @return The element to match the input
	 */
	public static final ICmdElement playerOrUser(final String key)
	{
		return new CmdPlayerOrUserElement(key);
	}

	/**
	 * Requires the command to be used by the console
	 * 
	 * @return The element to match the input
	 */
	public static final ICmdElement requireConsole()
	{
		return new CmdRequireUserTypeElement(UserType.CONSOLE);
	}
	/**
	 * Requires the command to be used by a player
	 * 
	 * @return The element to match the input
	 */
	public static final ICmdElement requirePlayer()
	{
		return new CmdRequireUserTypeElement(UserType.PLAYER);
	}
}
