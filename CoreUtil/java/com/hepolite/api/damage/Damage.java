package com.hepolite.api.damage;

public class Damage
{
	private final DamageVariant variant;
	private final DamageType type;
	private double amount;

	public Damage(final DamageType type, final double amount)
	{
		this(null, type, amount);
	}
	public Damage(final DamageVariant variant, final DamageType type, final double baseDamage)
	{
		this.variant = variant;
		this.type = type;
		this.amount = baseDamage;
	}

	/**
	 * @return The damage variant associated with this damage instance
	 */
	public final DamageVariant getVariant()
	{
		return variant == null ? type.getVariant() : variant;
	}
	/**
	 * @return The damage type associated with this damage instance
	 */
	public final DamageType getType()
	{
		return type;
	}
	/**
	 * @return The amount of damage amount associated with this damage instance
	 */
	public final double getAmount()
	{
		return amount;
	}

	/**
	 * Assigns the damage amount associated with this damage instance
	 * 
	 * @param damage The amount of damage this instance should do
	 */
	public final void setAmount(final double damage)
	{
		this.amount = damage;
	}
}
