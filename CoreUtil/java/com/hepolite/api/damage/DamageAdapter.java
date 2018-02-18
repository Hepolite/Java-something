package com.hepolite.api.damage;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

@SuppressWarnings("deprecation")
public final class DamageAdapter
{
	/**
	 * Converts the given Minecraft damage variation into a damage source of the custom type
	 * 
	 * @param event The Minecraft event to convert
	 * @return The custom damage source matching the Minecraft damage
	 */
	public Damage adapt(final EntityDamageEvent event)
	{
		final DamageVariant variant;
		final DamageType type;

		switch (event.getCause())
		{
		case BLOCK_EXPLOSION:
		case ENTITY_EXPLOSION:
			type = DamageType.EXPLOSION;
			variant = DamageVariant.PHYSICAL;
			break;
		case CONTACT:
		case CRAMMING:
		case ENTITY_ATTACK:
		case ENTITY_SWEEP_ATTACK:
			type = DamageType.MELEE;
			variant = DamageVariant.PHYSICAL;
			break;
		case CUSTOM:
		case SUICIDE:
		case VOID:
			type = DamageType.TRUE;
			variant = DamageVariant.UNKNOWN;
			break;
		case DRAGON_BREATH:
		case FIRE:
		case MELTING:
			type = DamageType.FIRE;
			variant = DamageVariant.ELEMENTAL;
			break;
		case DROWNING:
		case SUFFOCATION:
			type = DamageType.SUFFOCATION;
			variant = DamageVariant.PERSONAL;
			break;
		case FALL:
		case FLY_INTO_WALL:
			type = DamageType.IMPACT;
			variant = DamageVariant.PHYSICAL;
			break;
		case FALLING_BLOCK:
			type = DamageType.BLUNT;
			variant = DamageVariant.PHYSICAL;
			break;
		case FIRE_TICK:
			type = DamageType.BURNING;
			variant = DamageVariant.PERSONAL;
			break;
		case HOT_FLOOR:
		case LAVA:
			type = DamageType.LAVA;
			variant = DamageVariant.ELEMENTAL;
			break;
		case LIGHTNING:
			type = DamageType.ELECTRICITY;
			variant = DamageVariant.ELEMENTAL;
			break;
		case MAGIC:
			type = DamageType.MAGIC;
			variant = DamageVariant.MAGICAL;
			break;
		case POISON:
			type = DamageType.POISON;
			variant = DamageVariant.PERSONAL;
			break;
		case PROJECTILE:
			type = DamageType.PROJECTILE;
			variant = DamageVariant.PHYSICAL;
			break;
		case STARVATION:
			type = DamageType.HUNGER;
			variant = DamageVariant.PERSONAL;
			break;
		case THORNS:
			type = DamageType.MELEE;
			variant = DamageVariant.MAGICAL;
			break;
		case WITHER:
			type = DamageType.WITHER;
			variant = DamageVariant.PERSONAL;
			break;
		default:
			throw new IllegalArgumentException("Illegal damage cause " + event.getCause().name());
		}
		return new Damage(variant, type, event.getDamage());
	}

	/**
	 * Applies the custom damage instance to the Minecraft damage event, applying factors
	 * 
	 * @param damage The custom damage instance
	 * @param event The Minecraft event to update
	 * @return The same damage instance as was passed in
	 */
	public Damage adapt(final Damage damage, final EntityDamageEvent event)
	{
		final DamageType type = damage.getType();

		/// @formatter:off
		if (event.isApplicable(DamageModifier.ARMOR))
			event.setDamage(DamageModifier.ARMOR, type.getArmorFactor() * event.getDamage(DamageModifier.ARMOR));
		if (event.isApplicable(DamageModifier.MAGIC))
			event.setDamage(DamageModifier.MAGIC, type.getMagicFactor() * event.getDamage(DamageModifier.MAGIC));
		if (event.isApplicable(DamageModifier.RESISTANCE))
			event.setDamage(DamageModifier.RESISTANCE, type.getPotionFactor() * event.getDamage(DamageModifier.RESISTANCE));
		if (event.isApplicable(DamageModifier.BLOCKING))
			event.setDamage(DamageModifier.BLOCKING, type.getBlockFactor() * event.getDamage(DamageModifier.BLOCKING));
		/// @formatter:on

		damage.setAmount(event.getDamage());
		return damage;
	}
}
