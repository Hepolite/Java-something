package com.hepolite.api.attribute;

public final class Attribute
{
	private final ModifierMap modifiers = new ModifierMap();

	private float baseValue = 0.0f;
	private float minValue = -Float.MAX_VALUE;
	private float maxValue = Float.MAX_VALUE;

	public Attribute()
	{}
	public Attribute(final float baseValue)
	{
		this.baseValue = baseValue;
	}
	public Attribute(final float baseValue, final float minValue, final float maxValue)
	{
		this.baseValue = baseValue;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	/**
	 * Assigns the base value of the attribute
	 * 
	 * @param baseValue The new base value
	 * @return Returns the same instance for convenience
	 */
	public Attribute setBaseValue(final float baseValue)
	{
		this.baseValue = baseValue;
		return this;
	}
	/**
	 * Assigns the smallest value this attribute can have
	 * 
	 * @param minValue The smallest possible value for this attribute
	 * @return Returns the same instance for convenience
	 */
	public Attribute setMinValue(final float minValue)
	{
		this.minValue = minValue;
		return this;
	}
	/**
	 * Assigns the biggest value this attribute can have
	 * 
	 * @param minValue The biggest possible value for this attribute
	 * @return Returns the same instance for convenience
	 */
	public Attribute setMaxValue(final float maxValue)
	{
		this.maxValue = maxValue;
		return this;
	}

	/**
	 * Retrieves the base value of the attribute
	 * 
	 * @return The base value of the attribute
	 */
	public float getBaseValue()
	{
		return baseValue;
	}
	/**
	 * Retrieves the smallest possible value of the attribute
	 * 
	 * @return The smallest possible value of the attribute
	 */
	public float getMinValue()
	{
		return minValue;
	}
	/**
	 * Retrieves the biggest possible value of the attribute
	 * 
	 * @return The biggest possible value of the attribute
	 */
	public float getMaxValue()
	{
		return maxValue;
	}
	/**
	 * Calculates the current value of the attribute.<br>
	 * <br>
	 * The final attribute value is calculated as following:<br>
	 * totalMultiplier * (baseValue * (1 + totalScale) + totalConstant)<br>
	 * where the total multiplier/scale/constant is the sum of all modifiers acting on the
	 * attribute.<br>
	 * <br>
	 * The resulting value is then clamped to the maximum and minimum values, if present
	 * 
	 * @return The value of the attribute
	 */
	public float getValue()
	{
		final Modifier modifier = modifiers.calculateTotal();
		final float value = modifier.multiplier * (baseValue * (1 + modifier.scale) + modifier.constant);
		return Math.max(minValue, Math.min(maxValue, value));
	}

	// ...

	/**
	 * Checks if the attribute has a modifier stored under the given key
	 * 
	 * @param key The key of the modifier
	 * @return True iff the key is found
	 */
	public boolean has(final String key)
	{
		return modifiers.containsKey(key);
	}
	/**
	 * Assigns a modifier under the given key
	 * 
	 * @param key The key of the modifier
	 * @param modifier The modifier to store
	 */
	public void put(final String key, final Modifier modifier)
	{
		modifiers.put(key, modifier);
	}
	/**
	 * Removes the modifier under the given key, if it exists
	 * 
	 * @param key The key of the modifier to remove
	 */
	public void remove(final String key)
	{
		modifiers.remove(key);
	}
}
