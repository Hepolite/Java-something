package com.hepolite.api.event;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Simple base handler which provides a helper method for posting events on the event bus and
 * processing child handlers, as well as registering listeners
 */
public class HandlerCore implements IHandler
{
	private final Collection<IHandler> handlers = new ArrayList<>();
	protected final JavaPlugin plugin;

	public HandlerCore(final JavaPlugin plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public void onTick(final int tick)
	{
		for (final IHandler handler : handlers)
			handler.onTick(tick);
	}
	@Override
	public void onReload()
	{
		for (final IHandler handler : handlers)
			handler.onReload();
	}
	@Override
	public void onDisable()
	{
		for (final IHandler handler : handlers)
			handler.onDisable();
	}

	@Override
	public <T extends IHandler> T register(final T handler)
	{
		if (handler == null)
			throw new NullPointerException("Handler cannot be null");
		handlers.add(handler);
		Bukkit.getPluginManager().registerEvents(handler, plugin);
		return handler;
	}
	@Override
	public <T extends IListener> T register(final T listener)
	{
		if (listener == null)
			throw new NullPointerException("Listener cannot be null");
		Bukkit.getPluginManager().registerEvents(listener, plugin);
		return listener;
	}

	// ...

	@Override
	public <T extends Event> T post(final T event)
	{
		Bukkit.getServer().getPluginManager().callEvent(event);
		return event;
	}
}
