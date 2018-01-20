package com.hepolite.traits.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.hepolite.api.config.Config;
import com.hepolite.api.config.IProperty;
import com.hepolite.api.config.Property;

class FieldTest
{
	@Test
	void testConstructor()
	{
		final Field fieldA = new Field();
		final Field fieldB = new Field(3.14);

		assertEquals(0.0, fieldA.value);
		assertEquals(3.14, fieldB.value);
	}

	@Test
	void testSaveLoad()
	{
		final IProperty property = new Property("property");
		final Config config = new Config();
		final Field fieldA = new Field(3.14);
		final Field fieldB = new Field();

		config.set(property, fieldA);
		config.getValue(property, fieldB);

		assertEquals(3.14, fieldB.value);
	}
}
