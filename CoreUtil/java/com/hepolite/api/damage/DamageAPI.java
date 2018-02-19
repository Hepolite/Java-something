package com.hepolite.api.damage;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.plugin.java.JavaPlugin;

import com.hepolite.api.event.HandlerCore;
import com.hepolite.api.event.events.DamageEvent;
import com.hepolite.api.event.events.HealEvent;

public final class DamageAPI extends HandlerCore
{
	private static boolean cancelledDamage = false;

	private static DamageAPI instance;
	private final DamageAdapter adapter;
	private final DamageCalculator calculator;

	public DamageAPI(final JavaPlugin plugin)
	{
		super(plugin);
		DamageAPI.instance = this;
		this.adapter = new DamageAdapter();
		this.calculator = new DamageCalculator(plugin);
	}

	@Override
	public void onReload()
	{
		calculator.reloadConfigs();
	}

	// ...

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
		instance.adapter.injectDamage(damage);
		if (attacker == null)
			target.damage(damage.getAmount());
		else
			target.damage(damage.getAmount(), attacker);
		instance.adapter.injectDamage(null);
		return !cancelledDamage;
	}

	/**
	 * Performs a heal on the target
	 * 
	 * @param target The entity to heal
	 * @param healing The healing to perform
	 * @return True if the entity was healed
	 */
	public static boolean heal(final LivingEntity target, final Heal healing)
	{
		return heal(target, null, healing);
	}
	/**
	 * Performs a heal on the target
	 * 
	 * @param target The entity to heal
	 * @param healer The entity performing the healing
	 * @param healing The healing to perform
	 * @return True if the entity was healed
	 */
	public static boolean heal(final LivingEntity target, final LivingEntity healer, final Heal healing)
	{
		if (!target.isValid() || healing.getAmount() <= 0.0)
			return false;

		// It's utterly ridiculous that Spigot does not provide some heal method on living
		// entities... Seriously, it would have been super-easy to add some LivingEntity.heal
		// method!
		instance.adapter.injectHeal(healing);
		final EntityRegainHealthEvent event = instance
				.post(new EntityRegainHealthEvent(target, healing.getAmount(), RegainReason.CUSTOM));
		instance.adapter.injectHeal(null);

		// Make sure the entity is not dead at this point, otherwise it ends up in limbo!
		if (event.isCancelled() || event.getAmount() < 0.0 || !target.isValid() || target.isDead())
			return false;
		final double maxHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		if (target.getHealth() >= maxHealth)
			return false;
		target.setHealth(Math.min(target.getHealth() + event.getAmount(), maxHealth));
		return true;
	}

	// ...

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onEntityReceiveDamage(final EntityDamageEvent event)
	{
		final DamageEvent damageEvent = adapter.adapt(event);
		if (damageEvent == null)
			return;
		calculator.calculateReduction(damageEvent.getTarget(), damageEvent);

		post(damageEvent);
		event.setDamage(damageEvent.getFinalDamage());
		event.setCancelled(damageEvent.isCancelled());
	}
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void monitorEntityReceiveDamage(final EntityDamageEvent event)
	{
		cancelledDamage = event.isCancelled();
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onEntityReceiveHeal(final EntityRegainHealthEvent event)
	{
		final HealEvent healEvent = adapter.adapt(event);
		if (healEvent == null)
			return;

		post(healEvent);
		event.setAmount(healEvent.getHealing());
		event.setCancelled(healEvent.isCancelled());
	}
}
