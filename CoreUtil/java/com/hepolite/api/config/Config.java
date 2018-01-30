package com.hepolite.api.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public final class Config implements IConfig
{
	private final File file;
	private FileConfiguration config;

	/**
	 * Creates a new config instance that cannot be saved/loaded to/from disk. It exists purely in
	 * memory and will not be persistent.
	 */
	public Config()
	{
		this.file = null;
		this.config = new YamlConfiguration();
	}
	/**
	 * Creates a new config instance that can be saved/loaded to/from disk. It will not be saved
	 * automatically; the user has to manually save the config before server shuts down if the data
	 * should be persistent. The config will be automatically loaded from file if the file exists.
	 * 
	 * @param file The file to load/store the config from/to. The file should not include the file
	 *            type in its path
	 */
	public Config(final File file)
	{
		this.file = new File(file.getPath() + (file.getPath().contains(".yml") ? "" : ".yml"));
		this.config = YamlConfiguration.loadConfiguration(this.file);
	}
	/**
	 * Creates a new config instance that can be saved/loaded to/from disk. It will not be saved
	 * automatically; the user has to manually save the config before server shuts down if the data
	 * should be persistent. The config will be automatically loaded from file if the file exists.
	 * 
	 * @param directory The directory which contains the config file
	 * @param name The name of the config file; do not specify the file type in the name
	 */
	public Config(final File directory, final String name)
	{
		if (directory == null || directory.getPath().isEmpty() || name == null || name.isEmpty())
			throw new IllegalArgumentException("Directory and/or name cannot be null/empty");
		if (!directory.exists())
			directory.mkdirs();

		this.file = new File(directory.getPath() + "/" + name + ".yml");
		this.config = YamlConfiguration.loadConfiguration(this.file);
	}

	@Override
	public boolean isVirtual()
	{
		return file == null;
	}
	@Override
	public boolean saveToDisk()
	{
		if (isVirtual())
			return false;
		try
		{
			config.save(file);
			return true;
		}
		catch (final IOException e)
		{}
		return false;
	}
	@Override
	public boolean loadFromDisk()
	{
		if (isVirtual())
			return false;
		config = YamlConfiguration.loadConfiguration(file);
		return true;
	}
	@Override
	public boolean eraseFromDisk()
	{
		return file.delete();
	}

	// ...

	@Override
	public boolean has(final IProperty property)
	{
		return config.contains(property.getPath());
	}
	@Override
	public boolean add(final IProperty property, final Object value)
	{
		if (has(property))
			return false;
		return set(property, value);
	}
	@Override
	public boolean set(final IProperty property, final Object value)
	{
		if (value == null)
			throw new IllegalArgumentException("Value cannot be null");

		if (value instanceof IValue)
		{
			remove(property);
			((IValue) value).save(this, property);
		}
		else if (value instanceof Character)
			config.set(property.getPath(), (int) (char) value);
		else
			config.set(property.getPath(), value);
		return true;
	}
	@Override
	public boolean remove(final IProperty property)
	{
		if (!has(property))
			return false;
		config.set(property.getPath(), null);
		return true;
	}

	@Override
	public void clear()
	{
		config = new YamlConfiguration();
	}

	// ...

	@Override
	public Collection<IProperty> getProperties()
	{
		final HashSet<IProperty> properties = new HashSet<>();
		for (final String key : config.getKeys(false))
			properties.add(new Property(key));
		return properties;
	}
	@Override
	public Collection<IProperty> getProperties(final IProperty property)
	{
		final HashSet<IProperty> properties = new HashSet<>();
		final ConfigurationSection section = config.getConfigurationSection(property.getPath());
		if (section == null)
			return properties;

		for (final String key : section.getKeys(false))
			properties.add(property.child(key));
		return properties;
	}

	@Override
	public <T extends IValue> T getValue(final IProperty property, final T value)
	{
		if (value == null)
			throw new IllegalArgumentException("Input value cannot be null");
		value.load(this, property);
		return value;
	}

	@Override
	public boolean getBool(final IProperty property)
	{
		return getBool(property, false);
	}
	@Override
	public boolean getBool(final IProperty property, final boolean def)
	{
		return config.getBoolean(property.getPath(), def);
	}
	@Override
	public byte getByte(final IProperty property)
	{
		return getByte(property, (byte) 0);
	}
	@Override
	public byte getByte(final IProperty property, final byte def)
	{
		return (byte) config.getInt(property.getPath(), def);
	}
	@Override
	public char getChar(final IProperty property)
	{
		return getChar(property, (char) 0);
	}
	@Override
	public char getChar(final IProperty property, final char def)
	{
		return (char) config.getInt(property.getPath(), def);
	}
	@Override
	public double getDouble(final IProperty property)
	{
		return getDouble(property, 0.0);
	}
	@Override
	public double getDouble(final IProperty property, final double def)
	{
		return config.getDouble(property.getPath(), def);
	}
	@Override
	public float getFloat(final IProperty property)
	{
		return getFloat(property, 0.0f);
	}
	@Override
	public float getFloat(final IProperty property, final float def)
	{
		return (float) config.getDouble(property.getPath(), def);
	}
	@Override
	public int getInt(final IProperty property)
	{
		return getInt(property, 0);
	}
	@Override
	public int getInt(final IProperty property, final int def)
	{
		return config.getInt(property.getPath(), def);
	}
	@Override
	public long getLong(final IProperty property)
	{
		return getLong(property, 0L);
	}
	@Override
	public long getLong(final IProperty property, final long def)
	{
		return config.getLong(property.getPath(), def);
	}
	@Override
	public short getShort(final IProperty property)
	{
		return getShort(property, (short) 0);
	}
	@Override
	public short getShort(final IProperty property, final short def)
	{
		return (short) config.getInt(property.getPath(), def);
	}
	@Override
	public String getString(final IProperty property)
	{
		return getString(property, "");
	}
	@Override
	public String getString(final IProperty property, final String def)
	{
		return config.getString(property.getPath(), def);
	}

	@Override
	public List<Boolean> getBools(final IProperty property)
	{
		return getBools(property, new ArrayList<Boolean>());
	}
	@Override
	public List<Boolean> getBools(final IProperty property, final List<Boolean> def)
	{
		return has(property) ? config.getBooleanList(property.getPath()) : def;
	}
	@Override
	public List<Byte> getBytes(final IProperty property)
	{
		return getBytes(property, new ArrayList<Byte>());
	}
	@Override
	public List<Byte> getBytes(final IProperty property, final List<Byte> def)
	{
		return has(property) ? config.getByteList(property.getPath()) : def;
	}
	@Override
	public List<Character> getChars(final IProperty property)
	{
		return getChars(property, new ArrayList<Character>());
	}
	@Override
	public List<Character> getChars(final IProperty property, final List<Character> def)
	{
		return has(property) ? config.getCharacterList(property.getPath()) : def;
	}
	@Override
	public List<Double> getDoubles(final IProperty property)
	{
		return getDoubles(property, new ArrayList<Double>());
	}
	@Override
	public List<Double> getDoubles(final IProperty property, final List<Double> def)
	{
		return has(property) ? config.getDoubleList(property.getPath()) : def;
	}
	@Override
	public List<Float> getFloats(final IProperty property)
	{
		return getFloats(property, new ArrayList<Float>());
	}
	@Override
	public List<Float> getFloats(final IProperty property, final List<Float> def)
	{
		return has(property) ? config.getFloatList(property.getPath()) : def;
	}
	@Override
	public List<Integer> getInts(final IProperty property)
	{
		return getInts(property, new ArrayList<Integer>());
	}
	@Override
	public List<Integer> getInts(final IProperty property, final List<Integer> def)
	{
		return has(property) ? config.getIntegerList(property.getPath()) : def;
	}
	@Override
	public List<Long> getLongs(final IProperty property)
	{
		return getLongs(property, new ArrayList<Long>());
	}
	@Override
	public List<Long> getLongs(final IProperty property, final List<Long> def)
	{
		return has(property) ? config.getLongList(property.getPath()) : def;
	}
	@Override
	public List<Short> getShorts(final IProperty property)
	{
		return getShorts(property, new ArrayList<Short>());
	}
	@Override
	public List<Short> getShorts(final IProperty property, final List<Short> def)
	{
		return has(property) ? config.getShortList(property.getPath()) : def;
	}
	@Override
	public List<String> getStrings(final IProperty property)
	{
		return getStrings(property, new ArrayList<String>());
	}
	@Override
	public List<String> getStrings(final IProperty property, final List<String> def)
	{
		return has(property) ? config.getStringList(property.getPath()) : def;
	}
}
