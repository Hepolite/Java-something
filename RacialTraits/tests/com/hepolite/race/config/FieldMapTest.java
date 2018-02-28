package com.hepolite.race.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.hepolite.api.config.Config;
import com.hepolite.api.config.IProperty;
import com.hepolite.api.config.Property;
import com.hepolite.race.config.Field;
import com.hepolite.race.config.FieldMap;

class FieldMapTest
{
	@Test
	void testSaveLoad()
	{
		final IProperty property = new Property("property");
		final Config config = new Config();
		final FieldMap mapA = new FieldMap();
		mapA.put("fieldA", new Field(1.0));
		mapA.put("fieldB", new Field(2.0));
		final FieldMap mapB = new FieldMap();
		mapB.put("fieldC", new Field(3.0));

		config.set(property, mapA);
		config.getValue(property, mapB);

		assertEquals(2, mapB.size());
		assertEquals(1.0, mapB.get("fieldA").value);
		assertEquals(2.0, mapB.get("fieldB").value);
		assertFalse(mapB.containsKey("fieldC"));
	}
}
