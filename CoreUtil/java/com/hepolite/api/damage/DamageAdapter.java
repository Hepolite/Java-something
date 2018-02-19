package com.hepolite.api.damage;

import java.util.Optional;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.hepolite.api.event.events.DamageEvent;

@SuppressWarnings("deprecation")
public final class DamageAdapter
{
	private Damage damage;

	/**
	 * Provides the adapter with the damage it should be working on. If the damage is null, the
	 * adapter will work with whatever it is provided with in the event.
	 * 
	 * @param event The damage to inject into the adapter
	 */
	public void inject(final Damage damage)
	{
		this.damage = damage;
	}

	/**
	 * @return Whether the provided information is enough, or if modifiers should be calculated
	 */
	public boolean requiresModifierCalculations()
	{
		return damage != null;
	}

	// ...

	/**
	 * Converts the given Minecraft damage event into a custom damage event, if possible. For the
	 * conversion to be successful, the minecraft event must target a living entity
	 * 
	 * @param event The Minecraft event to convert
	 * @return The custom damage source matching the Minecraft damage
	 */
	public DamageEvent adapt(final EntityDamageEvent event)
	{
		// Figure out if this event is something that can be adapted or not
		final Optional<LivingEntity> target = getTarget(event);
		final Optional<LivingEntity> attacker = getAttacker(event);
		final Optional<Entity> source = getSource(event);
		if (!target.isPresent())
			return null;

		// Create the new event and fill in the needed information
		final Damage damage = this.damage == null ? convert(event.getCause(), event.getDamage()) : this.damage;
		final DamageEvent damageEvent = new DamageEvent(target.get(), attacker.orElse(null), source.orElse(null),
				damage);
		damageEvent.setCancelled(event.isCancelled());
		clear(event);
		return damageEvent;
	}
	private void clear(final EntityDamageEvent event)
	{
		// Wipe all unwanted garbage associated with the vanilla event
		if (event.isApplicable(EntityDamageEvent.DamageModifier.ARMOR))
			event.setDamage(EntityDamageEvent.DamageModifier.ARMOR, 0.0);
		if (event.isApplicable(EntityDamageEvent.DamageModifier.BLOCKING))
			event.setDamage(EntityDamageEvent.DamageModifier.BLOCKING, 0.0);
		if (event.isApplicable(EntityDamageEvent.DamageModifier.MAGIC))
			event.setDamage(EntityDamageEvent.DamageModifier.MAGIC, 0.0);
		if (event.isApplicable(EntityDamageEvent.DamageModifier.RESISTANCE))
			event.setDamage(EntityDamageEvent.DamageModifier.RESISTANCE, 0.0);
	}

	/**
	 * Converts the given vanilla cause into a custom damage instance
	 * 
	 * @param cause The cause to convert
	 * @return The resulting damage instance
	 */
	private Damage convert(final DamageCause cause, final double damage)
	{
		switch (cause)
		{
		case BLOCK_EXPLOSION:
		case ENTITY_EXPLOSION:
			return new Damage(DamageVariant.PHYSICAL, DamageType.EXPLOSION, damage);
		case CONTACT:
		case CRAMMING:
		case ENTITY_ATTACK:
		case ENTITY_SWEEP_ATTACK:
			return new Damage(DamageVariant.PHYSICAL, DamageType.NORMAL, damage);
		case CUSTOM:
		case SUICIDE:
		case VOID:
			return new Damage(DamageVariant.UNKNOWN, DamageType.TRUE, damage);
		case DRAGON_BREATH:
		case FIRE:
		case MELTING:
			return new Damage(DamageVariant.ELEMENTAL, DamageType.FIRE, damage);
		case DROWNING:
		case SUFFOCATION:
			return new Damage(DamageVariant.PERSONAL, DamageType.SUFFOCATION, damage);
		case FALL:
		case FLY_INTO_WALL:
			return new Damage(DamageVariant.PHYSICAL, DamageType.IMPACT, damage);
		case FALLING_BLOCK:
			return new Damage(DamageVariant.PHYSICAL, DamageType.BLUNT, damage);
		case FIRE_TICK:
			return new Damage(DamageVariant.PERSONAL, DamageType.BURNING, damage);
		case HOT_FLOOR:
		case LAVA:
			return new Damage(DamageVariant.ELEMENTAL, DamageType.LAVA, damage);
		case LIGHTNING:
			return new Damage(DamageVariant.ELEMENTAL, DamageType.ELECTRICITY, damage);
		case MAGIC:
			return new Damage(DamageVariant.MAGICAL, DamageType.MAGIC, damage);
		case POISON:
			return new Damage(DamageVariant.PERSONAL, DamageType.POISON, damage);
		case PROJECTILE:
			return new Damage(DamageVariant.PHYSICAL, DamageType.NORMAL_PROJECTILE, damage);
		case STARVATION:
			return new Damage(DamageVariant.PERSONAL, DamageType.HUNGER, damage);
		case THORNS:
			return new Damage(DamageVariant.MAGICAL, DamageType.NORMAL, damage);
		case WITHER:
			return new Damage(DamageVariant.PERSONAL, DamageType.WITHER, damage);
		default:
			throw new IllegalArgumentException("Illegal damage cause " + cause.name());
		}
	}

	// ...

	/**
	 * Attempts to retrieve the target from the provided event
	 * 
	 * @param event The event to look into
	 * @return The target if it exists
	 */
	private Optional<LivingEntity> getTarget(final EntityDamageEvent event)
	{
		if (event.getEntity() instanceof LivingEntity)
			return Optional.of((LivingEntity) event.getEntity());
		return Optional.empty();
	}
	/**
	 * Attempts to retrieve the attacker from the provided event
	 * 
	 * @param event The event to look into
	 * @return The attacker if it exists
	 */
	private Optional<LivingEntity> getAttacker(final EntityDamageEvent event)
	{
		if (!(event instanceof EntityDamageByEntityEvent))
			return Optional.empty();
		final EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;

		LivingEntity attacker = null;
		if (e.getDamager() instanceof Projectile)
		{
			final Projectile projectile = (Projectile) e.getDamager();
			if (projectile.getShooter() instanceof LivingEntity)
				attacker = (LivingEntity) projectile.getShooter();
		}
		else if (e.getDamager() instanceof LivingEntity)
			attacker = (LivingEntity) e.getDamager();
		return Optional.ofNullable(attacker);
	}
	/**
	 * Attempts to retrieve the source from the provided event
	 * 
	 * @param event The event to look into
	 * @return The source if it exists
	 */
	private Optional<Entity> getSource(final EntityDamageEvent event)
	{
		if (event instanceof EntityDamageByEntityEvent)
			return Optional.of(((EntityDamageByEntityEvent) event).getDamager());
		return Optional.empty();
	}
}
