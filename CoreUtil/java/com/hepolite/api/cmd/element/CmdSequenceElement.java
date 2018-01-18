package com.hepolite.api.cmd.element;

import com.hepolite.api.cmd.CmdArgs;
import com.hepolite.api.cmd.ICmdContext;
import com.hepolite.api.exception.ArgumentParseException;
import com.hepolite.api.user.IUser;

public class CmdSequenceElement implements ICmdElement
{
	private final ICmdElement[] elements;

	public CmdSequenceElement(final ICmdElement... elements)
	{
		this.elements = elements;
	}

	@Override
	public void parse(final IUser user, final CmdArgs args, final ICmdContext context) throws ArgumentParseException
	{
		for (final ICmdElement element : elements)
			element.parse(user, args, context);
	}
	@Override
	public Object parseValue(final IUser user, final CmdArgs args) throws ArgumentParseException
	{
		return null;
	}
}
