package com.hepolite.api.event.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PlayerAllowEatCheck extends Event
{
	private final Player player;
	private final ItemStack item;
	private boolean allow = true;

	public PlayerAllowEatCheck(final Player player, final ItemStack item)
	{
		this.player = player;
		this.item = item;
	}

	/**
	 * Retrieves the player who tries to eat something
	 * 
	 * @return The player who wants to eat
	 */
	public final Player getPlayer()
	{
		return player;
	}
	/**
	 * Retrieves the item the player wants to eat. Don't bother changing this item in any way - no
	 * change will make any difference
	 * 
	 * @return The item that might be eaten
	 */
	public final ItemStack getItem()
	{
		return item.clone();
	}

	/**
	 * Checks whether the player is allowed to eat or not
	 * 
	 * @return Whether the player can eat the item or not
	 */
	public final boolean getAllow()
	{
		return allow;
	}
	/**
	 * Sets whether the player is allowed to eat the item or not
	 * 
	 * @param allow Whether the player is allowed to eat or not
	 */
	public final void setAllow(final boolean allow)
	{
		this.allow = allow;
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
