package com.hepolite.coreutil;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.hepolite.api.plugin.IPlugin;
import com.hepolite.api.plugin.PluginCore;
import com.hepolite.api.task.SynchronizedTask;
import com.hepolite.api.units.Time;
import com.hepolite.coreutil.cmd.CmdCoreUtil;
import com.hepolite.coreutil.hunger.HungerHandler;
import com.hepolite.coreutil.movement.MovementHandler;
import com.hepolite.coreutil.util.reflection.ReflectionHandler;

public final class CoreUtilPlugin extends PluginCore implements IPlugin
{
	private final Collection<IPlugin> plugins = new ArrayList<>();

	private static CoreUtilPlugin instance;
	private HungerHandler hungerHandler;
	private MovementHandler movementHandler;

	private int currentTick = 0;

	// ...

	public static final CoreUtilPlugin getInstance()
	{
		return instance;
	}
	public static final HungerHandler getHungerHandler()
	{
		return instance.hungerHandler;
	}
	public static final MovementHandler getMovementHandler()
	{
		return instance.movementHandler;
	}

	public static final void INFO(final String msg)
	{
		instance.getLogger().info(msg);
	}
	public static final void WARN(final String msg)
	{
		instance.getLogger().warning(msg);
	}
	public static final void FATAL(final String msg)
	{
		instance.getLogger().severe("!!FATAL!! " + msg);
	}

	// ...

	@Override
	public void onEnable()
	{
		instance = this;
		cmd = new CmdCoreUtil();

		// Ensure that all plugins are found and updated every tick
		INFO("Setting up tasks...");
		if (!new SynchronizedTask(this, this::findPlugins).start(Time.fromInstant()))
			FATAL("Could not start initializer task!");
		if (!new SynchronizedTask(this, this::tickPlugins).start(Time.fromInstant(), Time.fromTicks(1)))
			FATAL("Could not start updater task!");
		INFO("Done setting up tasks!");

		// Set up utilities
		handler.register(new ReflectionHandler(this));

		// Ensure that all sub-systems are ready to roll
		hungerHandler = handler.register(new HungerHandler(this));
		movementHandler = handler.register(new MovementHandler(this));
	}

	// ...

	private void findPlugins()
	{
		plugins.clear();
		for (final Plugin plugin : Bukkit.getPluginManager().getPlugins())
		{
			if (plugin instanceof IPlugin)
				plugins.add((IPlugin) plugin);
		}
		INFO("Found " + plugins.size() + " plugin(s)");
	}
	private void tickPlugins()
	{
		for (final IPlugin plugin : plugins)
			plugin.onTick(currentTick);
		currentTick++;
	}
}
