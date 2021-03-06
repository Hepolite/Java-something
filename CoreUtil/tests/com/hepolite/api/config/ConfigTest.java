package com.hepolite.api.config;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

class ConfigTest
{
	@Test
	void testConstructor()
	{
		new Config();
		new Config(new File("tests"), "name");

		assertThrows(IllegalArgumentException.class, () -> new Config(null, "name"));
		assertThrows(IllegalArgumentException.class, () -> new Config(new File(""), null));
		assertThrows(IllegalArgumentException.class, () -> new Config(new File(""), ""));
	}

	@Test
	void testIsVirtual()
	{
		final Config configA = new Config();
		final Config configB = new Config(new File("tests"), "name");

		assertTrue(configA.isVirtual());
		assertFalse(configB.isVirtual());
	}
	@Test
	void testSaveLoadErase()
	{
		final Property property = new Property("property");
		final Config configA = new Config(new File("tests"), "config");
		final Config configB = new Config(new File("tests"), "config");
		configA.add(property, 42);

		final boolean saved = configA.saveToDisk();
		final boolean loaded = configB.loadFromDisk();
		final boolean deleted = configA.eraseFromDisk();

		assertEquals(42, configB.getInt(property));
		assertTrue(saved);
		assertTrue(loaded);
		assertTrue(deleted);
	}

	@Test
	void testAdd()
	{
		final Property property = new Property("property");
		final Config config = new Config();

		assertFalse(config.has(property));
		assertTrue(config.add(property, 2));
		assertFalse(config.add(property, 6));
		assertTrue(config.has(property));

		assertThrows(IllegalArgumentException.class, () -> config.add(new Property("nothere"), null));
	}
	@Test
	void testSet()
	{
		final Property property = new Property("property");
		final Config config = new Config();

		assertFalse(config.has(property));
		assertTrue(config.set(property, 1));
		assertTrue(config.set(property, 4));
		assertTrue(config.has(property));

		assertThrows(IllegalArgumentException.class, () -> config.set(new Property("nothere"), null));
	}
	@Test
	void testRemove()
	{
		final Property property = new Property("property");
		final Config config = new Config();
		config.add(property, 0);

		assertTrue(config.has(property));
		assertTrue(config.remove(property));
		assertFalse(config.remove(property));
		assertFalse(config.has(property));
	}
	
	@Test
	void testClear()
	{
		final IProperty propertyA = new Property("propertyA");
		final IProperty propertyB = new Property("propertyB");
		final IProperty propertyC = propertyB.child("propertyC");
		final Config config = new Config();
		config.add(propertyA, 0);
		config.add(propertyB, 0);
		config.add(propertyC, 0);
		config.clear();
		
		assertFalse(config.has(propertyA));
		assertFalse(config.has(propertyB));
		assertFalse(config.has(propertyC));
	}

	@Test
	void testGetProperties()
	{
		final IProperty rootA = new Property("rootA");
		final IProperty rootB = new Property("rootB");
		final IProperty propertyA = rootA.child("propertyA");
		final IProperty propertyB = rootA.child("propertyB");
		final IProperty propertyC = rootA.child("propertyC");
		final Config config = new Config();
		config.add(rootB, 0);
		config.add(propertyA, 0);
		config.add(propertyB, 0);
		config.add(propertyC, 0);

		final Collection<IProperty> propertiesA = config.getProperties();
		final Collection<IProperty> propertiesB = config.getProperties(rootA);
		assertEquals(2, propertiesA.size());
		assertEquals(3, propertiesB.size());
		assertTrue(propertiesA.contains(rootA));
		assertTrue(propertiesA.contains(rootB));
		assertTrue(propertiesB.contains(propertyA));
		assertTrue(propertiesB.contains(propertyB));
		assertTrue(propertiesB.contains(propertyC));
	}

	@Test
	void testSetGetSimple()
	{
		final Property property = new Property("property");
		final Config config = new Config();

		assertTrue(compare(config, property, config::getBool, true));
		assertTrue(compare(config, property, config::getByte, (byte) 5));
		assertTrue(compare(config, property, config::getChar, 't'));
		assertTrue(compare(config, property, config::getDouble, 3.14159));
		assertTrue(compare(config, property, config::getFloat, 1.57f));
		assertTrue(compare(config, property, config::getInt, 1000000000));
		assertTrue(compare(config, property, config::getLong, 0xFFFFFFFFFFFFL));
		assertTrue(compare(config, property, config::getShort, (short) 20000));
		assertTrue(compare(config, property, config::getString, "Hello World!"));
	}
	@Test
	void testSetGetLists()
	{
		final Property property = new Property("property");
		final Config config = new Config();

		assertTrue(compare(config, property, config::getBools, Arrays.asList(true, false, true)));
		assertTrue(compare(config, property, config::getBytes, Arrays.asList((byte) 3, (byte) 126)));
		assertTrue(compare(config, property, config::getChars, Arrays.asList('H', 'E', 'P')));
		assertTrue(compare(config, property, config::getDoubles, Arrays.asList(0.0, 4.13, 3.1E51)));
		assertTrue(compare(config, property, config::getFloats, Arrays.asList(-61.52f, -3.14f, 0.0f)));
		assertTrue(compare(config, property, config::getInts, Arrays.asList(0xDEADBEEF, 42, -1337)));
		assertTrue(compare(config, property, config::getLongs, Arrays.asList(0L, 0x161L, 0xFFFFFFFFFFFFFFFFL)));
		assertTrue(compare(config, property, config::getShorts, Arrays.asList((short) 12345, (short) -61)));
		assertTrue(compare(config, property, config::getStrings, Arrays.asList("Hello", "tester", "do test")));
	}
	@Test
	void testSetGetValue()
	{
		final Property property = new Property("property");
		final Config config = new Config();

		config.add(property, new ValueMock(42, "Greetings!"));
		final ValueMock value = config.getValue(property, new ValueMock());

		assertEquals(42, value.integer);
		assertEquals("Greetings!", value.string);
	}

	// ...

	public final class ValueMock implements IValue
	{
		public int integer;
		public String string;

		public ValueMock()
		{
			this(0, "");
		}
		public ValueMock(final int integer, final String string)
		{
			this.integer = integer;
			this.string = string;
		}

		@Override
		public void save(final IConfig config, final IProperty property)
		{
			config.set(property.child("integer"), integer);
			config.set(property.child("string"), string);
		}
		@Override
		public void load(final IConfig config, final IProperty property)
		{
			integer = config.getInt(property.child("integer"));
			string = config.getString(property.child("string"));
		}
	}

	private <T> boolean compare(final Config config, final Property property, final Function<Property, T> lookup,
			final T value)
	{
		config.set(property, value);
		return value.equals(lookup.apply(property));
	}
	private <T> boolean compare(final Config config, final Property property, final Function<Property, List<T>> lookup,
			final List<T> values)
	{
		config.set(property, values);
		final List<T> out = lookup.apply(property);

		if (values.size() != out.size())
			return false;
		for (int i = 0; i < values.size(); ++i)
		{
			if (!values.get(i).equals(out.get(i)))
				return false;
		}
		return true;
	}
}
