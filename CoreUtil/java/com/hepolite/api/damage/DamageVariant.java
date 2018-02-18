package com.hepolite.api.damage;

public enum DamageVariant
{
	/** Damage caused by climate (temperature, air pressure, etc) */
	CLIMATE,

	/** Damage caused by elements (fire, frost, water, electricity, etc) */
	ELEMENTAL,

	/** Damage caused by magical effects (spells, potions, etc) */
	MAGICAL,

	/** Damage caused by internal causes (hunger, thirst, poison, etc) */
	PERSONAL,

	/** Damage caused by physical attacks (punches, swords, arrows, explosions, impacts, etc) */
	PHYSICAL,

	/** Damage caused by any other sources (void damage, true damage, etc) */
	UNKNOWN,
}
