package com.hepolite.api.event.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class PlayerSaturationChange extends Event implements Cancellable
{
	private final Player player;
	private final float oldSaturation;
	private float newSaturation;

	private boolean isCancelled = false;

	public PlayerSaturationChange(final Player player, final float oldSaturation, final float newSaturation)
	{
		this.player = player;
		this.oldSaturation = oldSaturation;
		this.newSaturation = newSaturation;
	}

	/**
	 * @return Retrieves the player who had the saturation value changed
	 */
	public final Player getPlayer()
	{
		return player;
	}
	/**
	 * @return Retrieves the old saturation value for the player
	 */
	public final float getOldSaturation()
	{
		return oldSaturation;
	}
	/**
	 * @return Retrieves the new saturation value for the player
	 */
	public final float getNewSaturation()
	{
		return newSaturation;
	}
	/**
	 * @return Retrieves the change in saturation for the player
	 */
	public final float getDeltaSaturation()
	{
		return newSaturation - oldSaturation;
	}

	/**
	 * Assigns the new saturation for the player
	 * 
	 * @param newSaturation The new saturation value for the player
	 */
	public void setNewSaturation(final float newSaturation)
	{
		this.newSaturation = newSaturation;
	}
	/**
	 * Assigns the new delta saturation for the player
	 * 
	 * @param newSaturation The new saturation value for the player
	 */
	public void setDeltaSaturation(final float deltaSaturation)
	{
		this.newSaturation = oldSaturation + deltaSaturation;
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
