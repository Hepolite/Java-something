package com.hepolite.api.nbt;

import java.util.ArrayList;
import java.util.List;

import com.hepolite.coreutil.util.reflection.ReflectedConstructor;
import com.hepolite.coreutil.util.reflection.ReflectedMethod;
import com.hepolite.coreutil.util.reflection.reflected.RNBTTag;

public final class NBTList
{
	private final Object nbtTagList;
	private final List<NBTTagType> fields = new ArrayList<>();

	/**
	 * Constructs a new NBTTagList internally; this is the constructor you always want to be using
	 */
	public NBTList()
	{
		this(RNBTTag.List.nmsClass.newInstance());
	}
	/**
	 * Don't use this unless you know what you're doing
	 * 
	 * @param nbtTagCompound The NBTTagList to load and parse
	 */
	public NBTList(final Object nbtTagList)
	{
		this.nbtTagList = nbtTagList;

		// Parses the provided NBTTagList and converts the values into the value types
		for (int i = 0; i < size(); ++i)
			fields.add(NBTAPI.getField(RNBTTag.List.nmsGet.invoke(this.nbtTagList, i)));
	}

	/**
	 * Don't use this method unless you know what you're doing!
	 * 
	 * @return The underlying nms NBTTagList
	 */
	public Object getNBTTagList()
	{
		return nbtTagList;
	}

	// ...

	/**
	 * Attempts to retrieve the type of the data which is stored under the given key
	 * 
	 * @param key The key to look up
	 * @return The data type or INVALID if it does not exist
	 */
	public NBTTagType getField(final int key)
	{
		if (key >= 0 && key < fields.size())
			return fields.get(key);
		return NBTTagType.INVALID;
	}

	/**
	 * Attempts to retrieve the number of elements in the tag list
	 * 
	 * @return The size of the list
	 */
	public int size()
	{
		return (int) RNBTTag.List.nmsSize.invoke(nbtTagList);
	}

	/**
	 * Checks if the tag contains the given key
	 * 
	 * @param key The key to look for
	 * @return True iff the given key exists in the tag
	 */
	public boolean has(final int key)
	{
		return key >= 0 && key < size();
	}
	/**
	 * Attempts to remove the given key from the tag
	 * 
	 * @param key The key to remove
	 */
	public void remove(final int key)
	{
		if (key < 0 || key >= size())
			return;
		RNBTTag.List.nmsRemove.invoke(nbtTagList, key);
		fields.remove(key);
	}

	// ...

	/**
	 * Retrieves the value under the given key. The returned value is on the form of a NBTTag of
	 * whatever type the data is. This method converts that tag to a raw value
	 * 
	 * @param key The key to look up
	 * @param method The conversion method to use
	 * @return The raw data obtained from the list
	 */
	private Object getValue(final int key, final ReflectedMethod method)
	{
		return method.invoke(RNBTTag.List.nmsGet.invoke(nbtTagList, key));
	}
	/**
	 * Attempts to store the given value in the list
	 * 
	 * @param value The value to store
	 * @param constructor The construction method of converting the value into an NBTTag of
	 *            appropriate type
	 */
	private void addValue(final Object value, final ReflectedConstructor constructor)
	{
		RNBTTag.List.nmsAdd.invoke(nbtTagList, constructor.instantiate(value));
	}

	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key
	 */
	public boolean getBoolean(final int key)
	{
		return getBoolean(key, false);
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key, or the default value if it does not exist
	 */
	public boolean getBoolean(final int key, final boolean def)
	{
		return getByte(key) == 0 ? false : true;
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key
	 */
	public byte getByte(final int key)
	{
		return getByte(key, (byte) 0);
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key, or the default value if it does not exist
	 */
	public byte getByte(final int key, final byte def)
	{
		if (getField(key) == NBTTagType.BYTE)
			return (byte) getValue(key, RNBTTag.Byte.nmsAsByte);
		return def;
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key
	 */
	public byte[] getByteArray(final int key)
	{
		return getByteArray(key, new byte[] {});
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key, or the default value if it does not exist
	 */
	public byte[] getByteArray(final int key, final byte[] def)
	{
		if (getField(key) == NBTTagType.BYTE_ARRAY)
			return (byte[]) getValue(key, RNBTTag.ByteArray.nmsAsByteArray);
		return def;
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key
	 */
	public double getDouble(final int key)
	{
		return getDouble(key, 0.0);
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key, or the default value if it does not exist
	 */
	public double getDouble(final int key, final double def)
	{
		if (getField(key) == NBTTagType.DOUBLE)
			return (double) getValue(key, RNBTTag.Double.nmsAsDouble);
		return def;
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key
	 */
	public float getFloat(final int key)
	{
		return getFloat(key, 0.0f);
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key, or the default value if it does not exist
	 */
	public float getFloat(final int key, final float def)
	{
		if (getField(key) == NBTTagType.FLOAT)
			return (float) getValue(key, RNBTTag.Float.nmsAsFloat);
		return def;
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key
	 */
	public int getInt(final int key)
	{
		return getInt(key, 0);
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key, or the default value if it does not exist
	 */
	public int getInt(final int key, final int def)
	{
		if (getField(key) == NBTTagType.INT)
			return (int) getValue(key, RNBTTag.Int.nmsAsInt);
		return def;
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key
	 */
	public int[] getIntArray(final int key)
	{
		return getIntArray(key, new int[] {});
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key, or the default value if it does not exist
	 */
	public int[] getIntArray(final int key, final int[] def)
	{
		if (getField(key) == NBTTagType.INT_ARRAY)
			return (int[]) getValue(key, RNBTTag.IntArray.nmsAsIntArray);
		return def;
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key
	 */
	public NBTList getList(final int key)
	{
		return getList(key, new NBTList());
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key, or the default value if it does not exist
	 */
	public NBTList getList(final int key, final NBTList def)
	{
		if (getField(key) == NBTTagType.LIST)
			return new NBTList(RNBTTag.List.nmsGet.invoke(nbtTagList, key));
		return def;
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key
	 */
	public long getLong(final int key)
	{
		return getLong(key, 0L);
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key, or the default value if it does not exist
	 */
	public long getLong(final int key, final long def)
	{
		if (getField(key) == NBTTagType.LONG)
			return (long) getValue(key, RNBTTag.Long.nmsAsLong);
		return def;
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key
	 */
	public short getShort(final int key)
	{
		return getShort(key, (short) 0);
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key, or the default value if it does not exist
	 */
	public short getShort(final int key, final short def)
	{
		if (getField(key) == NBTTagType.SHORT)
			return (short) getValue(key, RNBTTag.Short.nmsAsShort);
		return def;
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key
	 */
	public String getString(final int key)
	{
		return getString(key, "");
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key, or the default value if it does not exist
	 */
	public String getString(final int key, final String def)
	{
		if (getField(key) == NBTTagType.STRING)
			return (String) getValue(key, RNBTTag.String.nmsAsString);
		return def;
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key
	 */
	public NBTTag getTag(final int key)
	{
		return getTag(key, new NBTTag());
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key, or the default value if it does not exist
	 */
	public NBTTag getTag(final int key, final NBTTag def)
	{
		if (getField(key) == NBTTagType.TAG)
			return new NBTTag(RNBTTag.List.nmsGet.invoke(nbtTagList, key));
		return def;
	}

	/**
	 * Attempts to add the given value to the list
	 * 
	 * @param value The value to store
	 */
	public void addBoolean(final boolean value)
	{
		addByte((byte) (value ? 1 : 0));
	}
	/**
	 * Attempts to add the given value to the list
	 * 
	 * @param value The value to store
	 */
	public void addByte(final byte value)
	{
		fields.add(NBTTagType.BYTE);
		addValue(value, RNBTTag.Byte.nmsConstructor);
	}
	/**
	 * Attempts to add the given value to the list
	 * 
	 * @param value The value to store
	 */
	public void addByteArray(final byte[] value)
	{
		fields.add(NBTTagType.BYTE_ARRAY);
		addValue(value, RNBTTag.ByteArray.nmsConstructor);
	}
	/**
	 * Attempts to add the given value to the list
	 * 
	 * @param value The value to store
	 */
	public void addDouble(final double value)
	{
		fields.add(NBTTagType.DOUBLE);
		addValue(value, RNBTTag.Double.nmsConstructor);
	}
	/**
	 * Attempts to add the given value to the list
	 * 
	 * @param value The value to store
	 */
	public void addFloat(final float value)
	{
		fields.add(NBTTagType.FLOAT);
		addValue(value, RNBTTag.Float.nmsConstructor);
	}
	/**
	 * Attempts to add the given value to the list
	 * 
	 * @param value The value to store
	 */
	public void addInt(final int value)
	{
		fields.add(NBTTagType.INT);
		addValue(value, RNBTTag.Int.nmsConstructor);
	}
	/**
	 * Attempts to add the given value to the list
	 * 
	 * @param value The value to store
	 */
	public void addIntArray(final int[] value)
	{
		fields.add(NBTTagType.INT_ARRAY);
		addValue(value, RNBTTag.IntArray.nmsConstructor);
	}
	/**
	 * Attempts to add the given value to the list
	 * 
	 * @param value The value to store
	 */
	public void addList(final NBTList value)
	{
		fields.add(NBTTagType.LIST);
		RNBTTag.List.nmsAdd.invoke(nbtTagList, value.getNBTTagList());
	}
	/**
	 * Attempts to add the given value to the list
	 * 
	 * @param value The value to store
	 */
	public void addLong(final Long value)
	{
		fields.add(NBTTagType.LONG);
		addValue(value, RNBTTag.Long.nmsConstructor);
	}
	/**
	 * Attempts to add the given value to the list
	 * 
	 * @param value The value to store
	 */
	public void addShort(final Short value)
	{
		fields.add(NBTTagType.SHORT);
		addValue(value, RNBTTag.Short.nmsConstructor);
	}
	/**
	 * Attempts to add the given value to the list
	 * 
	 * @param value The value to store
	 */
	public void addString(final String value)
	{
		fields.add(NBTTagType.STRING);
		addValue(value, RNBTTag.String.nmsConstructor);
	}
	/**
	 * Attempts to add the given value to the list
	 * 
	 * @param value The value to store
	 */
	public void addTag(final NBTTag value)
	{
		fields.add(NBTTagType.TAG);
		RNBTTag.List.nmsAdd.invoke(nbtTagList, value.getNBTTagCompound());
	}
}
