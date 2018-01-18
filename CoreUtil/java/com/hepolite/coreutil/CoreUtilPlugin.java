package com.hepolite.coreutil;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.hepolite.api.plugin.IPlugin;
import com.hepolite.api.plugin.PluginCore;
import com.hepolite.api.task.SynchronizedTask;
import com.hepolite.api.units.Time;
import com.hepolite.coreutil.movement.MovementHandler;

public final class CoreUtilPlugin extends PluginCore implements IPlugin
{
	private final Collection<IPlugin> plugins = new ArrayList<>();

	private int currentTick = 0;

	@Override
	public void onEnable()
	{
		// Ensure that all plugins are found and updated every tick
		getLogger().info("Setting up tasks...");
		if (!new SynchronizedTask(this, this::findPlugins).start(Time.fromInstant()))
			getLogger().severe("!!FATAL!! Could not start initializer task!");
		if (!new SynchronizedTask(this, this::tickPlugins).start(Time.fromInstant(), Time.fromTicks(1)))
			getLogger().severe("!!FATAL!! Could not start updater task!");
		getLogger().info("Done setting up tasks!");

		// Ensure that all sub-systems are ready to roll
		handler.register(new MovementHandler(this));
	}
	@Override
	public void onTick(final int tick)
	{
		handler.onTick(tick);
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

		getLogger().info("Found " + plugins.size() + " plugin(s)");
	}
	private void tickPlugins()
	{
		for (final IPlugin plugin : plugins)
			plugin.onTick(currentTick);
		currentTick++;
	}
}
