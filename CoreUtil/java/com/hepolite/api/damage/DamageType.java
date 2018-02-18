package com.hepolite.api.damage;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public enum DamageType
{
	/** Damage dealt by heavy physical attacks */
	BLUNT(DamageVariant.PHYSICAL, DamageCause.ENTITY_ATTACK, 0.5, 0.75, 1.0, 1.0),

	/** Damage dealt by being on fire */
	BURNING(DamageVariant.PERSONAL, DamageCause.FIRE, 0.0, 0.0, 1.0, 1.0),

	/** Damage dealt by electrical attacks */
	ELECTRICITY(DamageVariant.ELEMENTAL, DamageCause.LIGHTNING, 0.0, 0.0, 0.0, 0.0),

	/** Damage dealt by explosive attacks */
	EXPLOSION(DamageVariant.PHYSICAL, DamageCause.ENTITY_EXPLOSION, 0.25, 0.5, 1.0, 1.0),

	/** Damage dealt to those disrespecting the sudden stop at the end of a fall */
	IMPACT(DamageVariant.PHYSICAL, DamageCause.FALL, 0.0, 0.0, 1.0, 1.0),

	/** Damage dealt to those who got hit by a fire effect */
	FIRE(DamageVariant.ELEMENTAL, DamageCause.FIRE, 0.0, 0.0, 1.0, 1.0),

	/** Damage dealt to those who got hit by a cold effect */
	FROST(DamageVariant.ELEMENTAL, DamageCause.CUSTOM, 0.0, 0.0, 1.0, 1.0),

	/** Damage dealt due to starvation */
	HUNGER(DamageVariant.PERSONAL, DamageCause.STARVATION, 0.0, 0.0, 0.0, 0.0),

	/** Damage dealt to those who got too close to lava */
	LAVA(DamageVariant.ELEMENTAL, DamageCause.LAVA, 0.0, 0.0, 1.0, 0.0),

	/** Damage dealt by magical attacks */
	MAGIC(DamageVariant.MAGICAL, DamageCause.MAGIC, 0.0, 0.0, 0.0, 0.5),

	/** Damage dealt by normal attacks, such as swords, punches, etc */
	MELEE(DamageVariant.PHYSICAL, DamageCause.ENTITY_ATTACK, 1.0, 1.0, 1.0, 1.0),

	/** Damage dealt by sharp objects */
	PIERCING(DamageVariant.PHYSICAL, DamageCause.ENTITY_ATTACK, 0.25, 0.75, 0.5, 1.0),

	/** Damage dealt by normal ranged attacks, such as arrows, etc */
	PROJECTILE(DamageVariant.PHYSICAL, DamageCause.PROJECTILE, 1.0, 1.0, 1.0, 1.0),

	/** Damage dealt by poison-based attacks */
	POISON(DamageVariant.PERSONAL, DamageCause.POISON, 0.0, 0.0, 0.0, 0.0),

	/** Damage dealt by potion-based attacks */
	POTION(DamageVariant.PERSONAL, DamageCause.MAGIC, 0.0, 0.0, 0.0, 0.5),

	/** Damage dealt due to suffocation */
	SUFFOCATION(DamageVariant.PERSONAL, DamageCause.SUFFOCATION, 0.0, 0.0, 0.0, 0.0),

	/** Damage that bypasses most if not all resistances */
	TRUE(DamageVariant.UNKNOWN, DamageCause.CUSTOM, 0.0, 0.0, 0.0, 0.0),

	/** Damage by water-based attacks */
	WATER(DamageVariant.ELEMENTAL, DamageCause.DROWNING, 0.0, 0.0, 0.0, 1.0),

	/** Damage dealt by wither-based attacks */
	WITHER(DamageVariant.PERSONAL, DamageCause.WITHER, 0.0, 0.0, 0.0, 0.0)

	;

	private final DamageVariant variant;
	private final DamageCause underlyingCause;
	private final double armorFactor;
	private final double blockFactor;
	private final double enchantmentFactor;
	private final double potionFactor;

	private DamageType(final DamageVariant variant, final DamageCause underlyingCause, final double armorFactor,
			final double blockFactor, final double enchantmentFactor, final double potionFactor)
	{
		this.variant = variant;
		this.underlyingCause = underlyingCause;
		this.armorFactor = armorFactor;
		this.blockFactor = blockFactor;
		this.enchantmentFactor = enchantmentFactor;
		this.potionFactor = potionFactor;
	}

	/**
	 * @return The damage variant associated with this damage type
	 */
	public DamageVariant getVariant()
	{
		return variant;
	}
	/**
	 * @return The underlying Minecraft damage cause; this should normally not be used
	 */
	public DamageCause getUnderlyingCause()
	{
		return underlyingCause;
	}

	/**
	 * @return The armor multiplication factor
	 */
	public double getArmorFactor()
	{
		return armorFactor;
	}
	/**
	 * @return The block multiplication factor
	 */
	public double getBlockFactor()
	{
		return blockFactor;
	}
	/**
	 * @return The enchantment multiplication factor
	 */
	public double getMagicFactor()
	{
		return enchantmentFactor;
	}
	/**
	 * @return The potion multiplication factor
	 */
	public double getPotionFactor()
	{
		return potionFactor;
	}
}
