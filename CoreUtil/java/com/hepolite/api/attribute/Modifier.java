package com.hepolite.api.attribute;

/**
 * Represents a modifier on an attribute value.
 */
public class Modifier
{
	/**
	 * Constructs a new instance of a modifier
	 * 
	 * @param constant The constant value associated with the modifier
	 * @return The modifier instance
	 */
	public static final Modifier fromConstant(final float constant)
	{
		return new Modifier(constant, 1.0f, 0.0f);
	}
	/**
	 * Constructs a new instance of a modifier
	 * 
	 * @param multiplier The multiplier value associated with the modifier
	 * @return The modifier instance
	 */
	public static final Modifier fromMultiplier(final float multiplier)
	{
		return new Modifier(0.0f, multiplier, 0.0f);
	}
	/**
	 * Constructs a new instance of a modifier
	 * 
	 * @param scale The multiplier value associated with the scale
	 * @return The modifier instance
	 */
	public static final Modifier fromScale(final float scale)
	{
		return new Modifier(0.0f, 1.0f, scale);
	}
	/**
	 * Constructs a new instance of a modifier
	 * 
	 * @param multiplier The multiplier value associated with the modifier
	 * @param scale The multiplier value associated with the scale
	 * @param constant The constant value associated with the modifier
	 * @return The modifier instance
	 */
	public static final Modifier from(final float multiplier, final float scale, final float constant)
	{
		return new Modifier(constant, multiplier, scale);
	}

	// ...

	public final float constant;
	public final float multiplier;
	public final float scale;

	private Modifier(final float constant, final float multiplier, final float scale)
	{
		this.constant = constant;
		this.multiplier = multiplier;
		this.scale = scale;
	}
}
