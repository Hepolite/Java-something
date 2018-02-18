package com.hepolite.api.event.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.hepolite.api.user.IUser;

public class PlayerHungerChange extends Event implements Cancellable
{
	private final IUser user;
	private final float oldHunger;
	private float newHunger;

	private boolean isCancelled = false;

	public PlayerHungerChange(final IUser user, final float oldHunger, final float newHunger)
	{
		this.user = user;
		this.oldHunger = oldHunger;
		this.newHunger = newHunger;
	}

	/**
	 * @return Retrieves the user who had the hunger value changed
	 */
	public final IUser getUser()
	{
		return user;
	}
	/**
	 * @return Retrieves the old hunger value for the user
	 */
	public final float getOldHunger()
	{
		return oldHunger;
	}
	/**
	 * @return Retrieves the new hunger value for the user
	 */
	public final float getNewHunger()
	{
		return newHunger;
	}
	/**
	 * @return Retrieves the change in hunger for the user
	 */
	public final float getDeltaHunger()
	{
		return newHunger - oldHunger;
	}

	/**
	 * Assigns the new hunger for the user
	 * 
	 * @param newHunger The new hunger value for the user
	 */
	public void setNewHunger(final float newHunger)
	{
		this.newHunger = newHunger;
	}
	/**
	 * Assigns the new delta hunger for the user
	 * 
	 * @param newHunger The new hunger value for the user
	 */
	public void setDeltaHunger(final float deltaHunger)
	{
		this.newHunger = oldHunger + deltaHunger;
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
