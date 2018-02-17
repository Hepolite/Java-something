package com.hepolite.coreutil;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import com.hepolite.api.cmd.CmdDispatcher;
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
	private final CmdCoreUtil cmd = new CmdCoreUtil();

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

	// ...

	@Override
	public void onEnable()
	{
		instance = this;

		// Ensure that all plugins are found and updated every tick
		getLogger().info("Setting up tasks...");
		if (!new SynchronizedTask(this, this::findPlugins).start(Time.fromInstant()))
			getLogger().severe("!!FATAL!! Could not start initializer task!");
		if (!new SynchronizedTask(this, this::tickPlugins).start(Time.fromInstant(), Time.fromTicks(1)))
			getLogger().severe("!!FATAL!! Could not start updater task!");
		getLogger().info("Done setting up tasks!");

		// Set up utilities
		handler.register(new ReflectionHandler(this));

		// Ensure that all sub-systems are ready to roll
		hungerHandler = handler.register(new HungerHandler(this));
		movementHandler = handler.register(new MovementHandler(this));
	}
	@Override
	public void onReload()
	{
		handler.onReload();
	}
	@Override
	public void onTick(final int tick)
	{
		handler.onTick(tick);
	}
	@Override
	public final boolean onCommand(final CommandSender sender, final Command command, final String label,
			final String[] args)
	{
		return CmdDispatcher.dispatch(sender, cmd, args);
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
