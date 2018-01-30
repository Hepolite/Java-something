package com.hepolite.api.cmd.element;

import com.hepolite.api.cmd.CmdArgs;
import com.hepolite.api.cmd.CmdArgsSnapshot;
import com.hepolite.api.cmd.ICmdContext;
import com.hepolite.api.exception.ArgumentParseException;
import com.hepolite.api.user.IUser;

public class CmdFirstParsingElement implements ICmdElement
{
	private final ICmdElement[] elements;

	public CmdFirstParsingElement(final ICmdElement... elements)
	{
		this.elements = elements;
	}

	@Override
	public void parse(final IUser user, final CmdArgs args, final ICmdContext context) throws ArgumentParseException
	{
		for (final ICmdElement element : elements)
		{
			final CmdArgsSnapshot snapshot = args.getSnapshot();
			try
			{
				element.parse(user, args, context);
				return;
			}
			catch (final ArgumentParseException e)
			{
				args.restoreSnapshot(snapshot);
			}
		}
		throw new ArgumentParseException("Could not match input to any branch");
	}
	@Override
	public Object parseValue(final IUser user, final CmdArgs args) throws ArgumentParseException
	{
		return null;
	}
}
