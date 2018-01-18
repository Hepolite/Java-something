package com.hepolite.api.cmd.element;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.hepolite.api.cmd.CmdArgs;
import com.hepolite.api.exception.ArgumentParseException;
import com.hepolite.api.user.IUser;

public class CmdPlayerElement extends CmdKeyElement
{
	public CmdPlayerElement(final String key)
	{
		super(key);
	}

	@Override
	public Object parseValue(final IUser user, final CmdArgs args) throws ArgumentParseException
	{
		final String name = args.consumeArg();

		final Player player = Bukkit.getPlayer(name);
		if (player == null)
			throw new ArgumentParseException(String.format("Expected a player, but '%s' was not", name));
		return player;
	}
}
