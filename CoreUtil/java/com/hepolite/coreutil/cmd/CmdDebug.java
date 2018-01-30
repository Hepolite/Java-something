package com.hepolite.coreutil.cmd;

import org.bukkit.command.CommandException;

import com.hepolite.api.cmd.Cmd;
import com.hepolite.api.cmd.GenericArgs;
import com.hepolite.api.cmd.ICmdContext;
import com.hepolite.api.user.IUser;

public class CmdDebug extends Cmd
{
	public CmdDebug()
	{
		/// @formatter:off
		super("debug",
			GenericArgs.children(
				new CmdDebugFood()
		));
		/// @formatter:on
	}

	@Override
	public boolean execute(final IUser user, final ICmdContext context) throws CommandException
	{
		return true;
	}
}
