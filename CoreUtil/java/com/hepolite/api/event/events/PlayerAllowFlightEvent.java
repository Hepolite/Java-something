package com.hepolite.api.event.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PlayerAllowFlightEvent extends Event
{
	private final Player player;
	private boolean allowFlight;

	public PlayerAllowFlightEvent(final Player player, final boolean allowFlight)
	{
		this.player = player;
		this.allowFlight = allowFlight;
	}

	/**
	 * Retrieves the player who performed a jump
	 * 
	 * @return The player who jumped
	 */
	public Player getPlayer()
	{
		return player;
	}

	/**
	 * Sets whether the player is allowed to fly or not
	 * 
	 * @param allowFlight Whether the player is allowed to fly or not
	 */
	public void setAllowFlight(final boolean allowFlight)
	{
		this.allowFlight = allowFlight;
	}
	/**
	 * Checks if the player is allowed to fly
	 * 
	 * @return True iff the player is allowed to fly
	 */
	public boolean allowFlight()
	{
		return allowFlight;
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
