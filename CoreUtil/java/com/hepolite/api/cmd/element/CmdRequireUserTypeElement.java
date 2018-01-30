package com.hepolite.api.cmd.element;

import com.hepolite.api.cmd.CmdArgs;
import com.hepolite.api.cmd.ICmdContext;
import com.hepolite.api.exception.ArgumentParseException;
import com.hepolite.api.user.ConsoleUser;
import com.hepolite.api.user.EntityUser;
import com.hepolite.api.user.IUser;

public class CmdRequireUserTypeElement implements ICmdElement
{
	public static enum UserType
	{
		CONSOLE,
		PLAYER,
	}

	private final UserType type;

	public CmdRequireUserTypeElement(final UserType type)
	{
		this.type = type;
	}

	@Override
	public void parse(final IUser user, final CmdArgs args, final ICmdContext context) throws ArgumentParseException
	{
		switch (type)
		{
		case CONSOLE:
			if (!(user instanceof ConsoleUser))
				throw new ArgumentParseException("User must be the console");
			break;
		case PLAYER:
			if (!(user instanceof EntityUser))
				throw new ArgumentParseException("User must be a player");
			break;
		}
	}
	@Override
	public Object parseValue(final IUser user, final CmdArgs args) throws ArgumentParseException
	{
		return null;
	}
}
