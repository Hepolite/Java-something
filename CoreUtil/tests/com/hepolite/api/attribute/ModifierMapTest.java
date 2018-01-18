package com.hepolite.api.attribute;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ModifierMapTest
{
	@Test
	void testCalculateTotal()
	{
		ModifierMap map = new ModifierMap();
		map.put("modifierA", Modifier.from(2.0f, 1.0f, 0.0f));
		map.put("modifierB", Modifier.from(1.0f, 0.5f, 2.5f));
		Modifier modifier = map.calculateTotal();
		
		assertEquals(2.0f, modifier.multiplier);
		assertEquals(1.5f, modifier.scale);
		assertEquals(2.5f, modifier.constant);
	}
}
