package com.hepolite.race.cmd;

import org.bukkit.command.CommandException;

import com.hepolite.api.chat.Builder;
import com.hepolite.api.cmd.Cmd;
import com.hepolite.api.cmd.ICmdContext;
import com.hepolite.api.user.IUser;
import com.hepolite.race.race.Race;
import com.hepolite.race.race.Races;

public class CmdShowRaces extends Cmd
{
	public CmdShowRaces()
	{
		super("races");
	}

	@Override
	public boolean execute(final IUser user, final ICmdContext context) throws CommandException
	{
		final Builder builder = new Builder("&9Available races:\n");
		for (final Race race : Races.getAll())
			builder.addText(String.format("&b%s\n", race.getName()));
		user.sendMessage(builder.build());
		return true;
	}
}
