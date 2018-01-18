package com.hepolite.api.event.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerImpactGroundEvent extends Event
{
	private final Player player;
	private final float distance;

	public PlayerImpactGroundEvent(final Player player, final float distance)
	{
		this.player = player;
		this.distance = distance;
	}

	/**
	 * Retrieves the player who impacted with the ground
	 * 
	 * @return The player who impacted the ground
	 */
	public final Player getPlayer()
	{
		return player;
	}
	/**
	 * Retrieves the total distance the player fell before hitting something
	 * 
	 * @return The total player fall distance
	 */
	public final float getDistance()
	{
		return distance;
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
