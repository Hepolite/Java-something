package com.hepolite.api.event.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.hepolite.api.damage.Heal;
import com.hepolite.api.damage.HealType;

public class HealEvent extends Event implements Cancellable
{
	private final LivingEntity target;
	private final LivingEntity healer;
	private final Heal healing;

	private boolean isCancelled = false;

	public HealEvent(final LivingEntity target, final LivingEntity healer, final Heal healing)
	{
		this.target = target;
		this.healer = healer;
		this.healing = healing;
	}

	/**
	 * @return Retrieves the target
	 */
	public final LivingEntity getTarget()
	{
		return target;
	}
	/**
	 * @return Retrieves the healer
	 */
	public final LivingEntity getHealer()
	{
		return healer;
	}
	/**
	 * @return Retrieves the heal type
	 */
	public final HealType getType()
	{
		return healing.getType();
	}
	/**
	 * @return The amount of healing associated with this heal instance
	 */
	public final double getHealing()
	{
		return healing.getAmount();
	}

	/**
	 * Sets the amount of healing associated with this heal instance
	 * 
	 * @param health The amount of health associated with this heal instance
	 */
	public final void setHealing(final double health)
	{
		healing.setAmount(health);
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
