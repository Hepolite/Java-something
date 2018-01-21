package com.hepolite.traits.capabilities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.hepolite.api.config.Config;
import com.hepolite.api.config.Property;
import com.hepolite.traits.config.Field;

class CapabilityDataTest
{
	@Test
	void testCreateField()
	{
		final CapabilityData data = new CapabilityData()
		{};
		final Field fieldA = data.createField("fieldA");
		final Field fieldB = data.createField("fieldB", 3.14);

		assertEquals(0.0, fieldA.value);
		assertEquals(3.14, fieldB.value);
	}
	@Test
	void testGetField()
	{
		final CapabilityData data = new CapabilityData()
		{};
		final Field fieldA = data.createField("field", 1.0);
		final Field fieldB = data.getField("field");

		assertSame(fieldA, fieldB);
	}

	@Test
	void testSaveLoad()
	{
		final Property property = new Property("property");
		final Config config = new Config();

		final CapabilityData dataA = new CapabilityData()
		{};
		final CapabilityData dataB = new CapabilityData()
		{};
		dataA.createField("fieldA", 1.0);
		dataA.createField("fieldB", 2.0);
		dataA.level = 42;
		dataA.xp = 1337;

		config.set(property, dataA);
		config.getValue(property, dataB);

		assertEquals(1.0, dataA.getField("fieldA").value);
		assertEquals(2.0, dataB.getField("fieldB").value);
		assertEquals(42, dataB.level);
		assertEquals(1337, dataB.xp);
	}
}
