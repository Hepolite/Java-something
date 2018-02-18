package com.hepolite.api.event.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.hepolite.api.user.IUser;

public class PlayerSaturationChange extends Event implements Cancellable
{
	private final IUser user;
	private final float oldSaturation;
	private float newSaturation;

	private boolean isCancelled = false;

	public PlayerSaturationChange(final IUser user, final float oldSaturation, final float newSaturation)
	{
		this.user = user;
		this.oldSaturation = oldSaturation;
		this.newSaturation = newSaturation;
	}

	/**
	 * @return Retrieves the user who had the saturation value changed
	 */
	public final IUser getUser()
	{
		return user;
	}
	/**
	 * @return Retrieves the old saturation value for the user
	 */
	public final float getOldSaturation()
	{
		return oldSaturation;
	}
	/**
	 * @return Retrieves the new saturation value for the user
	 */
	public final float getNewSaturation()
	{
		return newSaturation;
	}
	/**
	 * @return Retrieves the change in saturation for the user
	 */
	public final float getDeltaSaturation()
	{
		return newSaturation - oldSaturation;
	}

	/**
	 * Assigns the new saturation for the user
	 * 
	 * @param newSaturation The new saturation value for the user
	 */
	public void setNewSaturation(final float newSaturation)
	{
		this.newSaturation = newSaturation;
	}
	/**
	 * Assigns the new delta saturation for the user
	 * 
	 * @param newSaturation The new saturation value for the user
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
