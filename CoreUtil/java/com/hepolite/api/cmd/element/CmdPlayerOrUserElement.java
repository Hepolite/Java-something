package com.hepolite.api.cmd.element;

import java.util.Optional;

import org.bukkit.entity.Player;

import com.hepolite.api.cmd.CmdArgs;
import com.hepolite.api.cmd.CmdArgsSnapshot;
import com.hepolite.api.exception.ArgumentParseException;
import com.hepolite.api.user.IUser;

public class CmdPlayerOrUserElement extends CmdPlayerElement
{
	public CmdPlayerOrUserElement(final String key)
	{
		super(key);
	}

	@Override
	public Object parseValue(final IUser user, final CmdArgs args) throws ArgumentParseException
	{
		final CmdArgsSnapshot snapshot = args.getSnapshot();
		try
		{
			super.parseValue(user, args);
		}
		catch (final ArgumentParseException e)
		{
			args.restoreSnapshot(snapshot);
		}

		final Optional<Player> opPlayer = user.getPlayer();
		if (opPlayer.isPresent())
			return opPlayer.get();
		throw new ArgumentParseException("Expected player name or player user");
	}
}
