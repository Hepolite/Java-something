package com.hepolite.race.capability;

public enum CapabilityType
{
	/**
	 * Traits are unlocked from the beginning; they are always active and cannot be leveled up by
	 * the player. They may be upgraded with experience, however.
	 */
	TRAIT,

	/**
	 * Abilities are passive capabilities that the player has to unlock. They may be further leveled
	 * up with skill points. They may also be improved with experience.
	 */
	ABILITY,

	/**
	 * Abilities are castable capabilities that the player has to unlock. They may be further
	 * leveled up with skill points. They may also be improved with experience.
	 */
	SKILL,
}
