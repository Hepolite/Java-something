package com.hepolite.coreutil.cmd;

import org.bukkit.command.CommandException;

import com.hepolite.api.chat.Builder;
import com.hepolite.api.chat.Color;
import com.hepolite.api.chat.Message;
import com.hepolite.api.cmd.Cmd;
import com.hepolite.api.cmd.ICmdContext;
import com.hepolite.api.user.IUser;
import com.hepolite.coreutil.CoreUtilPlugin;

public class CmdReload extends Cmd
{
	private final Message BEFORE_RELOAD = new Builder("Reloading configs...", Color.BLUE).build();
	private final Message AFTER_RELOAD = new Builder("Done reloading configs!", Color.BLUE).build();

	public CmdReload()
	{
		super("reload");
	}

	@Override
	public boolean execute(final IUser user, final ICmdContext context) throws CommandException
	{
		user.sendMessage(BEFORE_RELOAD);
		CoreUtilPlugin.INSTANCE.onReload();
		user.sendMessage(AFTER_RELOAD);
		return true;
	}
}
