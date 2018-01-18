package com.hepolite.api.attribute;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ModifierTest
{
	@Test
	void testConstructor()
	{
		final Modifier modifierA = Modifier.fromConstant(3.0f);
		final Modifier modifierB = Modifier.fromMultiplier(3.0f);
		final Modifier modifierC = Modifier.fromScale(3.0f);
		final Modifier modifierD = Modifier.from(0.5f, 1.0f, 1.5f);

		assertMatches(1.0f, 0.0f, 3.0f, modifierA);
		assertMatches(3.0f, 0.0f, 0.0f, modifierB);
		assertMatches(1.0f, 3.0f, 0.0f, modifierC);
		assertMatches(0.5f, 1.0f, 1.5f, modifierD);
	}

	private void assertMatches(final float multiplier, final float scale, final float constant, final Modifier modifier)
	{
		assertEquals(multiplier, modifier.multiplier);
		assertEquals(scale, modifier.scale);
		assertEquals(constant, modifier.constant);
	}
}
