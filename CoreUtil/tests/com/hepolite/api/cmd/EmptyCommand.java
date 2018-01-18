package com.hepolite.api.cmd;

import org.bukkit.command.CommandException;

import com.hepolite.api.user.IUser;

public class EmptyCommand extends Cmd
{
	public EmptyCommand(final String name)
	{
		super(name);
	}

	@Override
	public boolean execute(final IUser user, final ICmdContext context) throws CommandException
	{
		return true;
	}
}
