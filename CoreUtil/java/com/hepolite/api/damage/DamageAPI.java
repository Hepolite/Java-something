package com.hepolite.api.damage;

import java.util.Optional;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.plugin.java.JavaPlugin;

import com.hepolite.api.event.HandlerCore;
import com.hepolite.api.event.events.DamageEvent;
import com.hepolite.coreutil.CoreUtilPlugin;

public final class DamageAPI extends HandlerCore
{
	private static Damage currentDamage = null;
	private static boolean cancelledDamage = false;

	private final DamageAdapter adapter;
	private final DamageCalculator calculator;

	public DamageAPI(final JavaPlugin plugin)
	{
		super(plugin);
		this.adapter = new DamageAdapter();
		this.calculator = new DamageCalculator(plugin);
	}

	@Override
	public void onReload()
	{
		calculator.reloadConfigs();
	}

	/**
	 * Performs an attack on the target
	 * 
	 * @param target The target that will receive the damage
	 * @param damage The damage to be dealt to the target
	 * @return True iff the damage was not cancelled
	 */
	public static boolean damage(final LivingEntity target, final Damage damage)
	{
		return damage(target, null, damage);
	}
	/**
	 * Performs an attack on the target, from the attacker if the attacker is specified
	 * 
	 * @param target The target that will receive the damage
	 * @param attacker The attack which deals the damage, may be null
	 * @param damage The damage to be dealt to the target
	 * @return True iff the damage was not cancelled
	 */
	public static boolean damage(final LivingEntity target, final LivingEntity attacker, final Damage damage)
	{
		if (!target.isValid() || damage.getAmount() <= 0.0)
			return false;
		currentDamage = damage;
		if (attacker == null)
			target.damage(damage.getAmount());
		else
			target.damage(damage.getAmount(), attacker);
		currentDamage = null;
		return !cancelledDamage;
	}

	// ...

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onEntityTakeDamage(final EntityDamageEvent event)
	{
		// Figure out if this event is something that needs to be handled or not
		final Optional<LivingEntity> target = getTarget(event);
		final Optional<LivingEntity> attacker = getAttacker(event);
		final Optional<Entity> source = getSource(event);
		if (!target.isPresent())
			return;

		// Figure out how the damage should be modified to work with vanilla
		Damage damage;
		if (currentDamage == null)
			damage = adapter.adapt(event);
		else
			damage = adapter.adapt(currentDamage, event);

		// Posts the custom damage event, updating the actual entity event with applicable values
		final DamageEvent damageEvent = new DamageEvent(target.get(), attacker.orElse(null), source.orElse(null),
				damage);
		damageEvent.setCancelled(event.isCancelled());
		calculator.calculateReduction(target.get(), damageEvent);

		CoreUtilPlugin.INFO("");
		CoreUtilPlugin.INFO("Damage: " + event.getDamage() + " - " + event.getCause().toString());
		CoreUtilPlugin.INFO("Base: " + event.getDamage(DamageModifier.BASE));
		CoreUtilPlugin.INFO("Armor: " + event.getDamage(DamageModifier.ARMOR));
		CoreUtilPlugin.INFO("Magic: " + event.getDamage(DamageModifier.MAGIC));
		CoreUtilPlugin.INFO("Resistance: " + event.getDamage(DamageModifier.RESISTANCE));
		CoreUtilPlugin.INFO("Blocking: " + event.getDamage(DamageModifier.BLOCKING));
		CoreUtilPlugin.INFO("Armor: " + damageEvent.getDamage(com.hepolite.api.damage.DamageModifier.ARMOR));
		CoreUtilPlugin.INFO("Magic: " + damageEvent.getDamage(com.hepolite.api.damage.DamageModifier.MAGIC));
		CoreUtilPlugin.INFO("Resistance: " + damageEvent.getDamage(com.hepolite.api.damage.DamageModifier.POTION));
		CoreUtilPlugin.INFO("Blocking: " + damageEvent.getDamage(com.hepolite.api.damage.DamageModifier.BLOCKING));
		CoreUtilPlugin.INFO("");

		post(damageEvent);
		event.setDamage(damageEvent.getFinalDamage());
		event.setCancelled(damageEvent.isCancelled());
	}
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void monitorEntityTakeDamage(final EntityDamageEvent event)
	{
		cancelledDamage = event.isCancelled();
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
