package com.hepolite.api.damage;

import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

public enum HealType
{
	/** Regeneration due to a spell effect */
	MAGIC(RegainReason.MAGIC),
	/** Regeneration over time due to a spell effect */
	MAGIC_REGEN(RegainReason.MAGIC_REGEN),

	/** Regeneration due to a potion effect */
	POTION(RegainReason.MAGIC),
	/** Regeneration over time due to a potion effect */
	POTION_REGEN(RegainReason.MAGIC_REGEN),

	// ...

	/** Natural regeneration over time */
	NATURAL_REGEN(RegainReason.REGEN),

	/** Health gained from consuming items */
	CONSUME(RegainReason.EATING),
	/** Healing due to having a filled hunger bar */
	SATIATED_REGEN(RegainReason.SATIATED),

	// ...

	/** Healing by unclear or impossible-to-know sources */
	UNKNOWN(RegainReason.CUSTOM),

	;

	private final RegainReason underlyingReason;

	private HealType(final RegainReason reason)
	{
		this.underlyingReason = reason;
	}

	/**
	 * @return The underlying Minecraft heal reason; this should normally not be used
	 */
	@Deprecated
	public RegainReason getUnderlyingReason()
	{
		return underlyingReason;
	}
}
