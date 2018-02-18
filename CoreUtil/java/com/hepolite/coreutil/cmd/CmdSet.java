package com.hepolite.coreutil.cmd;

import org.bukkit.command.CommandException;

import com.hepolite.api.cmd.Cmd;
import com.hepolite.api.cmd.GenericArgs;
import com.hepolite.api.cmd.ICmdContext;
import com.hepolite.api.user.IUser;

public class CmdSet extends Cmd
{
	public CmdSet()
	{
		/// @formatter:off
		super("set",
			GenericArgs.children(
				new CmdSetHunger()
		));
		/// @formatter:on
	}

	@Override
	public boolean execute(final IUser user, final ICmdContext context) throws CommandException
	{
		return true;
	}
}
