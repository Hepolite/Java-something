package com.hepolite.api.config;

import java.util.Collection;
import java.util.List;

/**
 * Provides method which can be used to store and retrieve arbitrary data
 */
public interface IConfig
{
	/**
	 * Checks whether the configuration exists only in memory or not. A virtual configuration cannot
	 * be stored to disk, not loaded from disk
	 * 
	 * @return Whether the configuration is virtual or not
	 */
	public boolean isVirtual();

	/**
	 * Attempts to store the configuration to disk, if possible
	 * 
	 * @return Whether the configuration was saved to disk or not
	 */
	public boolean saveToDisk();
	/**
	 * Attempts to load the configuration from disk, if possible
	 * 
	 * @return Whether the configuration was loaded from disk or not
	 */
	public boolean loadFromDisk();
	/**
	 * Attempts to erase the configuration from disk, if possible
	 * 
	 * @return Whether the configuration was erased from disk or not
	 */
	public boolean eraseFromDisk();

	// ...

	/**
	 * Checks if the given property exists in the underlying configuration or not
	 * 
	 * @param property The path in the underlying configuration
	 * @return Whether the property existed in the underlying configuration or not
	 */
	public boolean has(IProperty property);
	/**
	 * Attempts to store the given value at the given property. The method will return true iff the
	 * property does not exist and the value was stored. Valid values are non-null java fundamental
	 * types (and collections of them), strings (and collections of them), and any IValue objects.
	 * 
	 * @param property The path in the underlying configuration
	 * @param object The value that is to be stored
	 * @return Whether the value was added or not
	 */
	public boolean add(IProperty property, Object value);
	/**
	 * Attempts to store the given value at the given property. The method will return true iff the
	 * the value was stored. Valid values are non-null java fundamental types (and collections of
	 * them), strings (and collections of them), and any IValue objects. If false is returned, the
	 * existing value (if any) remains unchanged.
	 * 
	 * @param property The path in the underlying configuration
	 * @param object The value that is to be stored
	 * @return Whether the value was stored or not
	 */
	public boolean set(IProperty property, Object value);
	/**
	 * Attempts to remove the value at the given property. The method will return true iff the
	 * property was removed from the configuration.
	 * 
	 * @param property The path in the underlying configuration
	 * @return Whether the value was removed or not
	 */
	public boolean remove(IProperty property);

	// ...

	/**
	 * Retrieves all properties that are children of the given property.
	 * 
	 * @param property The property to find all children properties under
	 * @return A collection of all child properties under the given property
	 */
	public Collection<IProperty> getProperties(IProperty property);

	/**
	 * Attempts to retrieve a generic value from the configuration, by storing the data in the given
	 * value object
	 * 
	 * @param property The path in the underlying configuration
	 * @param value The data destination object
	 * @returns The same value object as was passed in
	 */
	public <T extends IValue> T getValue(IProperty property, T value);

	/**
	 * Attempts to retrieve a boolean value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @return The boolean value under property, or false if it does not exist
	 */
	public boolean getBool(IProperty property);
	/**
	 * Attempts to retrieve a boolean value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @param def The default value to be returned if property was not found
	 * @return The boolean value under property, or def if it does not exist
	 */
	public boolean getBool(IProperty property, boolean def);

	/**
	 * Attempts to retrieve a byte value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @return The byte value under property, or 0 if it does not exist
	 */
	public byte getByte(IProperty property);
	/**
	 * Attempts to retrieve a byte value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @param def The default value to be returned if property was not found
	 * @return The byte value under property, or def if it does not exist
	 */
	public byte getByte(IProperty property, byte def);

	/**
	 * Attempts to retrieve a char value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @return The char value under property, or 0 if it does not exist
	 */
	public char getChar(IProperty property);
	/**
	 * Attempts to retrieve a char value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @param def The default value to be returned if property was not found
	 * @return The char value under property, or def if it does not exist
	 */
	public char getChar(IProperty property, char def);

	/**
	 * Attempts to retrieve a double value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @return The double value under property, or 0.0 if it does not exist
	 */
	public double getDouble(IProperty property);
	/**
	 * Attempts to retrieve a double value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @param def The default value to be returned if property was not found
	 * @return The double value under property, or def if it does not exist
	 */
	public double getDouble(IProperty property, double def);

	/**
	 * Attempts to retrieve a float value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @return The float value under property, or 0.0f if it does not exist
	 */
	public float getFloat(IProperty property);
	/**
	 * Attempts to retrieve a float value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @param def The default value to be returned if property was not found
	 * @return The float value under property, or def if it does not exist
	 */
	public float getFloat(IProperty property, float def);

	/**
	 * Attempts to retrieve an integer value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @return The integer value under property, or 0 if it does not exist
	 */
	public int getInt(IProperty property);
	/**
	 * Attempts to retrieve an integer value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @param def The default value to be returned if property was not found
	 * @return The integer value under property, or def if it does not exist
	 */
	public int getInt(IProperty property, int def);

	/**
	 * Attempts to retrieve a long value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @return The long value under property, or 0L if it does not exist
	 */
	public long getLong(IProperty property);
	/**
	 * Attempts to retrieve a long value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @param def The default value to be returned if property was not found
	 * @return The long value under property, or def if it does not exist
	 */
	public long getLong(IProperty property, long def);

	/**
	 * Attempts to retrieve a short value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @return The short value under property, or 0 if it does not exist
	 */
	public short getShort(IProperty property);
	/**
	 * Attempts to retrieve a short value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @param def The default value to be returned if property was not found
	 * @return The short value under property, or def if it does not exist
	 */
	public short getShort(IProperty property, short def);

	/**
	 * Attempts to retrieve a string value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @return The string value under property, or an empty string if it does not exist
	 */
	public String getString(IProperty property);
	/**
	 * Attempts to retrieve a string value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @param def The default value to be returned if property was not found
	 * @return The string value under property, or def if it does not exist
	 */
	public String getString(IProperty property, String def);


	/**
	 * Attempts to retrieve a boolean list value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @return The list value under property, or an empty list if it does not exist
	 */
	public List<Boolean> getBools(IProperty property);
	/**
	 * Attempts to retrieve a boolean list value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @param def The default value to be returned if property was not found
	 * @return The list value under property, or def if it does not exist
	 */
	public List<Boolean> getBools(IProperty property, List<Boolean> def);

	/**
	 * Attempts to retrieve a byte list value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @return The list value under property, or an empty list if it does not exist
	 */
	public List<Byte> getBytes(IProperty property);
	/**
	 * Attempts to retrieve a byte list value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @param def The default value to be returned if property was not found
	 * @return The list value under property, or def if it does not exist
	 */
	public List<Byte> getBytes(IProperty property, List<Byte> def);

	/**
	 * Attempts to retrieve a char list value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @return The list value under property, or an empty list if it does not exist
	 */
	public List<Character> getChars(IProperty property);
	/**
	 * Attempts to retrieve a char list value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @param def The default value to be returned if property was not found
	 * @return The list value under property, or def if it does not exist
	 */
	public List<Character> getChars(IProperty property, List<Character> def);

	/**
	 * Attempts to retrieve a double list value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @return The list value under property, or an empty list if it does not exist
	 */
	public List<Double> getDoubles(IProperty property);
	/**
	 * Attempts to retrieve a double list value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @param def The default value to be returned if property was not found
	 * @return The list value under property, or def if it does not exist
	 */
	public List<Double> getDoubles(IProperty property, List<Double> def);

	/**
	 * Attempts to retrieve a float list value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @return The list value under property, or an empty list if it does not exist
	 */
	public List<Float> getFloats(IProperty property);
	/**
	 * Attempts to retrieve a float list value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @param def The default value to be returned if property was not found
	 * @return The list value under property, or def if it does not exist
	 */
	public List<Float> getFloats(IProperty property, List<Float> def);

	/**
	 * Attempts to retrieve an integer list value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @return The list value under property, or an empty list if it does not exist
	 */
	public List<Integer> getInts(IProperty property);
	/**
	 * Attempts to retrieve an integer list value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @param def The default value to be returned if property was not found
	 * @return The list value under property, or def if it does not exist
	 */
	public List<Integer> getInts(IProperty property, List<Integer> def);

	/**
	 * Attempts to retrieve a long list value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @return The list value under property, or an empty list if it does not exist
	 */
	public List<Long> getLongs(IProperty property);
	/**
	 * Attempts to retrieve a long list value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @param def The default value to be returned if property was not found
	 * @return The list value under property, or def if it does not exist
	 */
	public List<Long> getLongs(IProperty property, List<Long> def);

	/**
	 * Attempts to retrieve a short list value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @return The list value under property, or an empty list if it does not exist
	 */
	public List<Short> getShorts(IProperty property);
	/**
	 * Attempts to retrieve a short list value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @param def The default value to be returned if property was not found
	 * @return The list value under property, or def if it does not exist
	 */
	public List<Short> getShorts(IProperty property, List<Short> def);

	/**
	 * Attempts to retrieve a string list value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @return The list value under property, or an empty list if it does not exist
	 */
	public List<String> getStrings(IProperty property);
	/**
	 * Attempts to retrieve a string list value from the configuration
	 * 
	 * @param property The path in the underlying configuration
	 * @param def The default value to be returned if property was not found
	 * @return The list value under property, or def if it does not exist
	 */
	public List<String> getStrings(IProperty property, List<String> def);
}
