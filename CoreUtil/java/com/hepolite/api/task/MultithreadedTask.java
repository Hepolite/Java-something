package com.hepolite.api.task;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.hepolite.api.units.Time;

public class MultithreadedTask implements ITask
{
	private final JavaPlugin plugin;
	private final Runnable payload;

	private int id = -1;

	public MultithreadedTask(final JavaPlugin plugin, final Runnable payload)
	{
		if (plugin == null)
			throw new IllegalArgumentException("Plugin cannot be null");
		if (payload == null)
			throw new IllegalArgumentException("Payload cannot be null");

		this.plugin = plugin;
		this.payload = payload;
	}

	/**
	 * {@inheritDoc} <i>Great care should be taken to ensure that the task is not stopped on any
	 * other thread than the thread which started it.</i>
	 */
	@Override
	public boolean start(final Time startup)
	{
		// Due to a bug in scheduling, a repeating task must be scheduled instead...
		return start(startup, Time.fromYears(100));
		// if (id == -1)
		// return false;
		// if (startup.asTicks() == 0)
		// id = Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, payload);
		// else
		// id = Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, payload, startup.asTicks());
		// return id != -1;
	}
	/**
	 * {@inheritDoc} <i>Great care should be taken to ensure that the task is not stopped on any
	 * other thread than the thread which started it.</i>
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean start(final Time startup, final Time repeat)
	{
		if (id != -1)
			return false;
		id = Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, payload, startup.asTicks(), repeat.asTicks());
		return id != -1;
	}
	@Override
	public boolean stop()
	{
		if (id == -1)
			return false;

		Bukkit.getServer().getScheduler().cancelTask(id);
		id = -1;
		return true;
	}
}
