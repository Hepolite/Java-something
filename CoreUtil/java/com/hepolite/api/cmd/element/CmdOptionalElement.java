package com.hepolite.api.cmd.element;

import com.hepolite.api.cmd.CmdArgs;
import com.hepolite.api.cmd.CmdArgsSnapshot;
import com.hepolite.api.cmd.GenericArgs;
import com.hepolite.api.cmd.ICmdContext;
import com.hepolite.api.exception.ArgumentParseException;
import com.hepolite.api.user.IUser;

public class CmdOptionalElement implements ICmdElement
{
	private final ICmdElement element;

	public CmdOptionalElement(final ICmdElement... elements)
	{
		this.element = GenericArgs.sequence(elements);
	}

	@Override
	public void parse(final IUser user, final CmdArgs args, final ICmdContext context) throws ArgumentParseException
	{
		final CmdArgsSnapshot snapshot = args.getSnapshot();
		try
		{
			element.parse(user, args, context);
		}
		catch (final ArgumentParseException e)
		{
			args.restoreSnapshot(snapshot);
		}
	}
	@Override
	public Object parseValue(final IUser user, final CmdArgs args) throws ArgumentParseException
	{
		return null;
	}
}
