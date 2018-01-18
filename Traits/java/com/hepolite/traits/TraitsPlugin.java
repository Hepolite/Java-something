package com.hepolite.traits;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.hepolite.api.cmd.CmdDispatcher;
import com.hepolite.api.plugin.IPlugin;
import com.hepolite.api.plugin.PluginCore;
import com.hepolite.traits.cmd.CmdTraits;

public final class TraitsPlugin extends PluginCore implements IPlugin
{
	private final CmdTraits cmd = new CmdTraits();

	@Override
	public void onEnable()
	{}
	@Override
	public void onDisable()
	{}
	@Override
	public void onReload()
	{}

	@Override
	public void onTick(final int tick)
	{}

	@Override
	public final boolean onCommand(final CommandSender sender, final Command command, final String label,
			final String[] args)
	{
		return CmdDispatcher.dispatch(sender, cmd, args);
	}
}
