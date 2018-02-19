package com.hepolite.api.event.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerHungerChange extends Event implements Cancellable
{
	private final Player player;
	private final float oldHunger;
	private float newHunger;

	private boolean isCancelled = false;

	public PlayerHungerChange(final Player player, final float oldHunger, final float newHunger)
	{
		this.player = player;
		this.oldHunger = oldHunger;
		this.newHunger = newHunger;
	}

	/**
	 * @return Retrieves the player who had the hunger value changed
	 */
	public final Player getPlayer()
	{
		return player;
	}
	/**
	 * @return Retrieves the old hunger value for the player
	 */
	public final float getOldHunger()
	{
		return oldHunger;
	}
	/**
	 * @return Retrieves the new hunger value for the player
	 */
	public final float getNewHunger()
	{
		return newHunger;
	}
	/**
	 * @return Retrieves the change in hunger for the player
	 */
	public final float getDeltaHunger()
	{
		return newHunger - oldHunger;
	}

	/**
	 * Assigns the new hunger for the player
	 * 
	 * @param newHunger The new hunger value for the player
	 */
	public void setNewHunger(final float newHunger)
	{
		this.newHunger = newHunger;
	}
	/**
	 * Assigns the new delta hunger for the player
	 * 
	 * @param newHunger The new hunger value for the player
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
