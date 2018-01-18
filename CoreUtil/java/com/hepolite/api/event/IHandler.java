package com.hepolite.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public interface IHandler extends Listener
{
	/**
	 * Invoked every tick for as long as the handler is active
	 * 
	 * @param tick The current tick of the plugin
	 */
	public void onTick(int tick);
	/**
	 * Invoked when the plugin is being reloaded
	 */
	public void onReload();

	/**
	 * Adds the given child handler to this handler
	 * 
	 * @param handler The handler to add
	 */
	public <T extends IHandler> T register(T handler);
	/**
	 * Adds the given listener to this handler
	 * 
	 * @param listener The listener to add
	 */
	public <T extends IListener> T register(T listener);

	/**
	 * Posts the given event on the event bus, allowing it to be processed as any other normal
	 * event.
	 * 
	 * @param event The event to be processed
	 * @return The same event as was posted
	 */
	public <T extends Event> T post(final T event);
}
