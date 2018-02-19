package com.hepolite.api.event.events;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.hepolite.api.damage.Damage;
import com.hepolite.api.damage.DamageModifier;
import com.hepolite.api.damage.DamageType;
import com.hepolite.api.damage.DamageVariant;

public class DamageEvent extends Event implements Cancellable
{
	private final LivingEntity target;
	private final LivingEntity attacker;
	private final Entity source;
	private final Damage damage;
	private final Map<DamageModifier, Double> modifiers = new HashMap<>();

	private boolean isCancelled = false;

	public DamageEvent(final LivingEntity target, final LivingEntity attacker, final Entity source, final Damage damage)
	{
		this.target = target;
		this.attacker = attacker;
		this.source = source;
		this.damage = damage;
	}

	/**
	 * @return Retrieves the target
	 */
	public final LivingEntity getTarget()
	{
		return target;
	}
	/**
	 * @return Retrieves the attacker
	 */
	public final LivingEntity getAttacker()
	{
		return attacker;
	}
	/**
	 * @return Retrieves the damage source
	 */
	public final Entity getSource()
	{
		return source;
	}
	/**
	 * @return Retrieves the damage variant
	 */
	public final DamageVariant getVariant()
	{
		return damage.getVariant();
	}
	/**
	 * @return Retrieves the damage type
	 */
	public final DamageType getType()
	{
		return damage.getType();
	}

	/**
	 * @return The amount of base damage associated with this damage instance
	 */
	public final double getBaseDamage()
	{
		return damage.getAmount();
	}
	/**
	 * Sets the base damage done by this damage event
	 * 
	 * @param damage The damage to deal
	 */
	public final void setBaseDamage(final double amount)
	{
		damage.setAmount(amount);
	}

	/**
	 * @return The final amount of damage associated with this damage instance
	 */
	public final double getFinalDamage()
	{
		double damage = getBaseDamage();
		for (final Entry<DamageModifier, Double> entry : modifiers.entrySet())
			damage += entry.getValue();
		return damage;
	}

	/**
	 * @param modifier The modifier to retrieve the damage from
	 * @return The damage caused by the modifier
	 */
	public final double getDamage(final DamageModifier modifier)
	{
		return modifiers.containsKey(modifier) ? modifiers.get(modifier) : 0.0;
	}
	/**
	 * Assigns the damage dealt due to the modifier
	 * 
	 * @param modifier The modifier to look up
	 * @param damage The damage caused by the modifier
	 */
	public final void setDamage(final DamageModifier modifier, final double damage)
	{
		modifiers.put(modifier, damage);
	}
	/**
	 * Removes all modifiers that are currently applied to the damage
	 */
	public final void clearModifiers()
	{
		modifiers.clear();
	}



	@Override
	public boolean isCancelled()
	{
		return isCancelled;
	}
	@Override
	public void setCancelled(final boolean cancel)
	{
		isCancelled = cancel;
	}

	// //////////////////////////////////////////////////////////

	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}
	public static HandlerList getHandlerList()
	{
		return handlers;
	}
}
