package com.hepolite.coreutil.cmd;

import org.bukkit.command.CommandException;

import com.hepolite.api.cmd.Cmd;
import com.hepolite.api.cmd.GenericArgs;
import com.hepolite.api.cmd.ICmdContext;
import com.hepolite.api.user.IUser;

public class CmdCoreUtil extends Cmd
{
	public CmdCoreUtil()
	{
		/// @formatter:off
		super("coreutil",
			GenericArgs.children(
				new CmdDebug(),
				new CmdReload(),
				new CmdSet()
		));
		/// @formatter:on
	}

	@Override
	public boolean execute(final IUser user, final ICmdContext context) throws CommandException
	{
		return true;
	}
}
