package com.hepolite.api.attribute;

import java.util.HashMap;

/**
 * Represents a mapping from a key to a specific attribute modifier.
 */
public final class ModifierMap extends HashMap<String, Modifier>
{
	private static final long serialVersionUID = -5733741155004150334L;

	/**
	 * Calculates the total modifier values associated with this map
	 * 
	 * @return The total modification due to the stored modifiers
	 */
	public Modifier calculateTotal()
	{
		float totalMultiplier = 1.0f;
		float totalScale = 0.0f;
		float totalConstant = 0.0f;

		for (final Modifier modifier : values())
		{
			totalMultiplier *= modifier.multiplier;
			totalScale += modifier.scale;
			totalConstant += modifier.constant;
		}
		return Modifier.from(totalMultiplier, totalScale, totalConstant);
	}
}
