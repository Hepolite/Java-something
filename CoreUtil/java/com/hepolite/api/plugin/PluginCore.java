package com.hepolite.api.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.hepolite.api.cmd.CmdDispatcher;
import com.hepolite.api.cmd.ICmd;
import com.hepolite.api.event.HandlerCore;

public abstract class PluginCore extends JavaPlugin
{
	protected final HandlerCore handler = new HandlerCore(this);
	protected ICmd cmd = null;

	// ...

	@Override
	public void onDisable()
	{
		handler.onDisable();
	}
	public void onReload()
	{
		handler.onReload();
	}
	public void onTick(final int tick)
	{
		handler.onTick(tick);
	}

	@Override
	public final boolean onCommand(final CommandSender sender, final Command command, final String label,
			final String[] args)
	{
		if (cmd == null)
			return false;
		return CmdDispatcher.dispatch(sender, cmd, args);
	}
}
