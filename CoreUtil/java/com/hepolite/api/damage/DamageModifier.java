package com.hepolite.api.damage;

/**
 * Don't use this class yet. It is still in development and will not be needed before
 * {@link org.bukkit.event.entity.EntityDamageEvent.DamageModifier}s are gone.
 */
public enum DamageModifier
{
	/** Represents damage blocked by basic armor */
	ARMOR,

	/** Represents damage blocked by blocking with a shield */
	BLOCKING,

	/** Represents damage blocked by enchanted armor */
	MAGIC,

	/** Represents damage blocked by the resistance potion effect */
	POTION,
}
