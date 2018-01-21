package com.hepolite.api.nbt;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.hepolite.coreutil.util.reflection.reflected.RNBTTag;

public final class NBTTag
{
	private final Object nbtTagCompound;
	private final Map<String, NBTTagType> fields = new HashMap<>();

	/**
	 * Constructs a new NBTTagCompound internally; this is the constructor you always want to be
	 * using
	 */
	public NBTTag()
	{
		this(RNBTTag.Compound.nmsClass.newInstance());
	}
	/**
	 * Don't use this unless you know what you're doing
	 * 
	 * @param nbtTagCompound The NBTTagCompound to load and parse
	 */
	public NBTTag(final Object nbtTagCompound)
	{
		this.nbtTagCompound = nbtTagCompound;

		// Parses the provided NBTTagList and converts the values into the value types
		for (final String key : getKeys())
			fields.put(key, NBTAPI.getField(RNBTTag.Compound.nmsGetTag.invoke(this.nbtTagCompound, key)));
	}

	/**
	 * Don't use this method unless you know what you're doing!
	 * 
	 * @return The underlying nms NBTTagCompound
	 */
	public Object getNBTTagCompound()
	{
		return nbtTagCompound;
	}

	// ...

	/**
	 * Attempts to retrieve the type of the data which is stored under the given key
	 * 
	 * @param key The key to look up
	 * @return The data type or INVALID if it does not exist
	 */
	public NBTTagType getField(final String key)
	{
		if (fields.containsKey(key))
			return fields.get(key);
		return NBTTagType.INVALID;
	}

	/**
	 * Attempts to retrieve all possible keys in the tag
	 * 
	 * @return The set of all keys, or an empty set if none exists
	 */
	public final Set<String> getKeys()
	{
		@SuppressWarnings("unchecked")
		final Set<String> keys = (Set<String>) RNBTTag.Compound.nmsGetKeys.invoke(nbtTagCompound);
		return Collections.unmodifiableSet(keys == null ? new HashSet<>() : keys);
	}

	/**
	 * Checks if the tag contains the given key
	 * 
	 * @param key The key to look for
	 * @return True iff the given key exists in the tag
	 */
	public boolean has(final String key)
	{
		return (boolean) RNBTTag.Compound.nmsHasKey.invoke(nbtTagCompound, key);
	}
	/**
	 * Attempts to remove the given key from the tag
	 * 
	 * @param key The key to remove
	 */
	public void remove(final String key)
	{
		if (!has(key))
			return;
		RNBTTag.Compound.nmsRemove.invoke(nbtTagCompound, key);
		fields.remove(key);
	}

	// ...

	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key
	 */
	public boolean getBoolean(final String key)
	{
		return getBoolean(key, false);
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key, or the default value if it does not exist
	 */
	public boolean getBoolean(final String key, final boolean def)
	{
		return getByte(key) == 0 ? false : true;
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key
	 */
	public byte getByte(final String key)
	{
		return getByte(key, (byte) 0);
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key, or the default value if it does not exist
	 */
	public byte getByte(final String key, final byte def)
	{
		if (getField(key) == NBTTagType.BYTE)
			return (byte) RNBTTag.Compound.nmsGetByte.invoke(nbtTagCompound, key);
		return def;
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key
	 */
	public byte[] getByteArray(final String key)
	{
		return getByteArray(key, new byte[] {});
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key, or the default value if it does not exist
	 */
	public byte[] getByteArray(final String key, final byte[] def)
	{
		if (getField(key) == NBTTagType.BYTE_ARRAY)
			return (byte[]) RNBTTag.Compound.nmsGetByteArray.invoke(nbtTagCompound, key);
		return def;
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key
	 */
	public double getDouble(final String key)
	{
		return getDouble(key, 0.0);
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key, or the default value if it does not exist
	 */
	public double getDouble(final String key, final double def)
	{
		if (getField(key) == NBTTagType.DOUBLE)
			return (double) RNBTTag.Compound.nmsGetDouble.invoke(nbtTagCompound, key);
		return def;
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key
	 */
	public float getFloat(final String key)
	{
		return getFloat(key, 0.0f);
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key, or the default value if it does not exist
	 */
	public float getFloat(final String key, final float def)
	{
		if (getField(key) == NBTTagType.FLOAT)
			return (float) RNBTTag.Compound.nmsGetFloat.invoke(nbtTagCompound, key);
		return def;
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key
	 */
	public int getInt(final String key)
	{
		return getInt(key, 0);
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key, or the default value if it does not exist
	 */
	public int getInt(final String key, final int def)
	{
		if (getField(key) == NBTTagType.INT)
			return (int) RNBTTag.Compound.nmsGetInt.invoke(nbtTagCompound, key);
		return def;
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key
	 */
	public int[] getIntArray(final String key)
	{
		return getIntArray(key, new int[] {});
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key, or the default value if it does not exist
	 */
	public int[] getIntArray(final String key, final int[] def)
	{
		if (getField(key) == NBTTagType.INT_ARRAY)
			return (int[]) RNBTTag.Compound.nmsGetIntArray.invoke(nbtTagCompound, key);
		return def;
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key
	 */
	public NBTList getList(final String key)
	{
		return getList(key, new NBTList());
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key, or the default value if it does not exist
	 */
	public NBTList getList(final String key, final NBTList def)
	{
		if (getField(key) == NBTTagType.LIST)
			return new NBTList(RNBTTag.Compound.nmsGetTag.invoke(nbtTagCompound, key));
		return def;
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key
	 */
	public long getLong(final String key)
	{
		return getLong(key, 0L);
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key, or the default value if it does not exist
	 */
	public long getLong(final String key, final long def)
	{
		if (getField(key) == NBTTagType.LONG)
			return (long) RNBTTag.Compound.nmsGetLong.invoke(nbtTagCompound, key);
		return def;
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key
	 */
	public short getShort(final String key)
	{
		return getShort(key, (short) 0);
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key, or the default value if it does not exist
	 */
	public short getShort(final String key, final short def)
	{
		if (getField(key) == NBTTagType.SHORT)
			return (short) RNBTTag.Compound.nmsGetShort.invoke(nbtTagCompound, key);
		return def;
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key
	 */
	public String getString(final String key)
	{
		return getString(key, "");
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key, or the default value if it does not exist
	 */
	public String getString(final String key, final String def)
	{
		if (getField(key) == NBTTagType.STRING)
			return (String) RNBTTag.Compound.nmsGetString.invoke(nbtTagCompound, key);
		return def;
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key
	 */
	public NBTTag getTag(final String key)
	{
		return getTag(key, new NBTTag());
	}
	/**
	 * Retrieves the value stored under the given key, if it exists
	 * 
	 * @param key The key to retrieve the value from
	 * @return The value stored under the given key, or the default value if it does not exist
	 */
	public NBTTag getTag(final String key, final NBTTag def)
	{
		if (getField(key) == NBTTagType.TAG)
			return new NBTTag(RNBTTag.Compound.nmsGetTag.invoke(nbtTagCompound, key));
		return def;
	}

	/**
	 * Attempts to store the given value under the given key
	 * 
	 * @param key The key to store the value under
	 * @param value The value to store
	 */
	public void setBoolean(final String key, final boolean value)
	{
		setByte(key, (byte) (value ? 1 : 0));
	}
	/**
	 * Attempts to store the given value under the given key
	 * 
	 * @param key The key to store the value under
	 * @param value The value to store
	 */
	public void setByte(final String key, final byte value)
	{
		fields.put(key, NBTTagType.BYTE);
		RNBTTag.Compound.nmsSetByte.invoke(nbtTagCompound, key, value);
	}
	/**
	 * Attempts to store the given value under the given key
	 * 
	 * @param key The key to store the value under
	 * @param value The value to store
	 */
	public void setByteArray(final String key, final byte[] value)
	{
		fields.put(key, NBTTagType.BYTE_ARRAY);
		RNBTTag.Compound.nmsSetByteArray.invoke(nbtTagCompound, key, value);
	}
	/**
	 * Attempts to store the given value under the given key
	 * 
	 * @param key The key to store the value under
	 * @param value The value to store
	 */
	public void setDouble(final String key, final double value)
	{
		fields.put(key, NBTTagType.DOUBLE);
		RNBTTag.Compound.nmsSetDouble.invoke(nbtTagCompound, key, value);
	}
	/**
	 * Attempts to store the given value under the given key
	 * 
	 * @param key The key to store the value under
	 * @param value The value to store
	 */
	public void setFloat(final String key, final float value)
	{
		fields.put(key, NBTTagType.FLOAT);
		RNBTTag.Compound.nmsSetFloat.invoke(nbtTagCompound, key, value);
	}
	/**
	 * Attempts to store the given value under the given key
	 * 
	 * @param key The key to store the value under
	 * @param value The value to store
	 */
	public void setInt(final String key, final int value)
	{
		fields.put(key, NBTTagType.INT);
		RNBTTag.Compound.nmsSetInt.invoke(nbtTagCompound, key, value);
	}
	/**
	 * Attempts to store the given value under the given key
	 * 
	 * @param key The key to store the value under
	 * @param value The value to store
	 */
	public void setIntArray(final String key, final int[] value)
	{
		fields.put(key, NBTTagType.INT_ARRAY);
		RNBTTag.Compound.nmsSetIntArray.invoke(nbtTagCompound, key, value);
	}
	/**
	 * Attempts to store the given value under the given key
	 * 
	 * @param key The key to store the value under
	 * @param value The value to store
	 */
	public void setList(final String key, final NBTList value)
	{
		fields.put(key, NBTTagType.LIST);
		RNBTTag.Compound.nmsSetTag.invoke(nbtTagCompound, key, value.getNBTTagList());
	}
	/**
	 * Attempts to store the given value under the given key
	 * 
	 * @param key The key to store the value under
	 * @param value The value to store
	 */
	public void setLong(final String key, final long value)
	{
		fields.put(key, NBTTagType.LONG);
		RNBTTag.Compound.nmsSetLong.invoke(nbtTagCompound, key, value);
	}
	/**
	 * Attempts to store the given value under the given key
	 * 
	 * @param key The key to store the value under
	 * @param value The value to store
	 */
	public void setShort(final String key, final short value)
	{
		fields.put(key, NBTTagType.SHORT);
		RNBTTag.Compound.nmsSetShort.invoke(nbtTagCompound, key, value);
	}
	/**
	 * Attempts to store the given value under the given key
	 * 
	 * @param key The key to store the value under
	 * @param value The value to store
	 */
	public void setString(final String key, final String value)
	{
		fields.put(key, NBTTagType.STRING);
		RNBTTag.Compound.nmsSetString.invoke(nbtTagCompound, key, value);
	}
	/**
	 * Attempts to store the given value under the given key
	 * 
	 * @param key The key to store the value under
	 * @param value The value to store
	 */
	public void setTag(final String key, final NBTTag value)
	{
		fields.put(key, NBTTagType.TAG);
		RNBTTag.Compound.nmsSetTag.invoke(nbtTagCompound, key, value.getNBTTagCompound());
	}
}
