package com.hepolite.api.attribute;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AttributeTest
{
	@Test
	void testConstructor()
	{
		final Attribute attributeA = new Attribute();
		final Attribute attributeB = new Attribute(2.0f);
		final Attribute attributeC = new Attribute(1.0f, -3.0f, 4.0f);

		assertMatches(0.0f, -Float.MAX_VALUE, Float.MAX_VALUE, attributeA);
		assertMatches(2.0f, -Float.MAX_VALUE, Float.MAX_VALUE, attributeB);
		assertMatches(1.0f, -3.0f, 4.0f, attributeC);
	}
	
	@Test
	void testSetValue()
	{
		final Attribute attribute = new Attribute();
		
		assertEquals(1.0f, attribute.setBaseValue(1.0f).getBaseValue());
		assertEquals(2.0f, attribute.setMinValue(2.0f).getMinValue());
		assertEquals(3.0f, attribute.setMaxValue(3.0f).getMaxValue());
	}
	@Test
	void testGetValue()
	{
		final Attribute attributeA = new Attribute(2.0f);
		attributeA.put("modifierA", Modifier.fromConstant(1.0f));
		attributeA.put("modifierB", Modifier.from(0.5f, 1.0f, -0.5f));
		final Attribute attributeB = new Attribute(0.5f, 0.0f, 1.0f);
		attributeB.put("modifierA", Modifier.fromConstant(1.5f));
		final Attribute attributeC = new Attribute(0.5f, 0.0f, 1.0f);
		attributeC.put("modifierA", Modifier.fromConstant(-1.5f));
		
		assertEquals(2.25f, attributeA.getValue());
		assertEquals(1.0f, attributeB.getValue());
		assertEquals(0.0f, attributeC.getValue());
	}

	@Test
	void testPut()
	{
		final String key = "key";
		final Attribute attribute = new Attribute();

		assertFalse(attribute.has(key));
		attribute.put(key, Modifier.fromConstant(0.0f));
		assertTrue(attribute.has(key));
	}
	@Test
	void testRemove()
	{
		final String key = "key";
		final Attribute attribute = new Attribute();
		attribute.put(key, Modifier.fromConstant(0.0f));

		assertTrue(attribute.has(key));
		attribute.remove(key);
		assertFalse(attribute.has(key));
	}

	private void assertMatches(final float base, final float min, final float max, final Attribute attribute)
	{
		assertEquals(base, attribute.getBaseValue());
		assertEquals(min, attribute.getMinValue());
		assertEquals(max, attribute.getMaxValue());
	}
}
