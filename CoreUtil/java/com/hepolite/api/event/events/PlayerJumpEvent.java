package com.hepolite.api.event.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PlayerJumpEvent extends Event
{
	private final Player player;

	public PlayerJumpEvent(final Player player)
	{
		this.player = player;
	}

	/**
	 * Retrieves the player who performed a jump
	 * 
	 * @return The player who jumped
	 */
	public final Player getPlayer()
	{
		return player;
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
