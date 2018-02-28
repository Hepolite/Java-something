package com.hepolite.race.cmd;

import org.bukkit.command.CommandException;

import com.hepolite.api.cmd.Cmd;
import com.hepolite.api.cmd.GenericArgs;
import com.hepolite.api.cmd.ICmdContext;
import com.hepolite.api.user.IUser;

public class CmdShow extends Cmd
{
	public CmdShow()
	{
		/// @formatter:off
		super("show",
			GenericArgs.children(
				new CmdShowInfo(),
				new CmdShowRaces()
		));
		/// @formatter:on
	}

	@Override
	public boolean execute(final IUser user, final ICmdContext context) throws CommandException
	{
		return true;
	}
}
