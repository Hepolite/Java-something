package com.hepolite.api.damage;

public class Heal
{
	private final HealType type;
	private double amount;

	public Heal(final HealType type, final double amount)
	{
		this.type = type;
		this.amount = amount;
	}

	/**
	 * @return The heal type associated with this heal instance
	 */
	public final HealType getType()
	{
		return type;
	}
	/**
	 * @return The amount of heal amount associated with this heal instance
	 */
	public final double getAmount()
	{
		return amount;
	}

	/**
	 * Assigns the heal amount associated with this heal instance
	 * 
	 * @param heal The amount of healing this instance should do
	 */
	public final void setAmount(final double heal)
	{
		this.amount = heal;
	}
}
