package com.hepolite.api.damage;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.hepolite.api.event.HandlerCore;
import com.hepolite.api.event.events.DamageEvent;
import com.hepolite.coreutil.CoreUtilPlugin;

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
		instance.adapter.inject(damage);
		if (attacker == null)
			target.damage(damage.getAmount());
		else
			target.damage(damage.getAmount(), attacker);
		instance.adapter.inject(null);
		return !cancelledDamage;
	}

	// ...

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onEntityTakeDamage(final EntityDamageEvent event)
	{
		final DamageEvent damageEvent = adapter.adapt(event);
		if (damageEvent == null)
			return;
		calculator.calculateReduction(damageEvent.getTarget(), damageEvent);

		CoreUtilPlugin.INFO("");
		CoreUtilPlugin.INFO("Damage: " + damageEvent.getBaseDamage() + " - " + damageEvent.getVariant() + "/"
				+ damageEvent.getType());
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
}
