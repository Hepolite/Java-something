package com.hepolite.api.cmd;

import com.hepolite.api.cmd.element.CmdCommandElement;
import com.hepolite.api.cmd.element.CmdNumberElement;
import com.hepolite.api.cmd.element.CmdPlayerElement;
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
	public final static ICmdElement children(final ICmd... children)
	{
		return new CmdCommandElement(children);
	}

	/**
	 * Consumes a series of arguments. Usage is the elements concatenated.
	 * 
	 * @param elements The sequence that is required
	 * @return The element to match the input
	 */
	public final static ICmdElement sequence(final ICmdElement... elements)
	{
		return new CmdSequenceElement(elements);
	}

	/**
	 * Requires an argument to be a byte. The value may be specified as base 2, 10 or 16.
	 * 
	 * @param key The key to store the value under
	 * @return The element to match the input
	 */
	public final static ICmdElement byteNum(final String key)
	{
		return new CmdNumberElement<>(key, Byte::parseByte, Byte::parseByte, "Expected a byte, but '%s' was not");
	}
	/**
	 * Requires an argument to be a double.
	 * 
	 * @param key The key to store the value under
	 * @return The element to match the input
	 */
	public final static ICmdElement doubleNum(final String key)
	{
		return new CmdNumberElement<>(key, Double::parseDouble, null, "Expected a double, but '%s' was not");
	}
	/**
	 * Requires an argument to be a float.
	 * 
	 * @param key The key to store the value under
	 * @return The element to match the input
	 */
	public final static ICmdElement floatNum(final String key)
	{
		return new CmdNumberElement<>(key, Float::parseFloat, null, "Expected a float, but '%s' was not");
	}
	/**
	 * Requires an argument to be an integer. The value may be specified as base 2, 10 or 16.
	 * 
	 * @param key The key to store the value under
	 * @return The element to match the input
	 */
	public final static ICmdElement intNum(final String key)
	{
		return new CmdNumberElement<>(key, Integer::parseInt, Integer::parseInt, "Expected an int, but '%s' was not");
	}
	/**
	 * Requires an argument to be a long. The value may be specified as base 2, 10 or 16.
	 * 
	 * @param key The key to store the value under
	 * @return The element to match the input
	 */
	public final static ICmdElement longNum(final String key)
	{
		return new CmdNumberElement<>(key, Long::parseLong, Long::parseLong, "Expected a long, but '%s' was not");
	}
	/**
	 * Requires an argument to be a short. The value may be specified as base 2, 10 or 16.
	 * 
	 * @param key The key to store the value under
	 * @return The element to match the input
	 */
	public final static ICmdElement shortNum(final String key)
	{
		return new CmdNumberElement<>(key, Short::parseShort, Short::parseShort, "Expected a short, but '%s' was not");
	}

	/**
	 * Requires an argument to be a string.
	 * 
	 * @param key The key to store the value under
	 * @return The element to match the input
	 */
	public final static ICmdElement string(final String key)
	{
		return new CmdStringElement.One(key);
	}
	/**
	 * Consumes all remaining arguments, concatenated to a single string
	 * 
	 * @param key The key to store the value under
	 * @return The element to match the input
	 */
	public final static ICmdElement remainingStrings(final String key)
	{
		return new CmdStringElement.Remaining(key);
	}

	/**
	 * Requires an argument to be a player.
	 * 
	 * @param key The key to store the value under
	 * @return The element to match the input
	 */
	public final static ICmdElement player(final String key)
	{
		return new CmdPlayerElement(key);
	}
}
