package com.hepolite.api.cmd.element;

import com.hepolite.api.cmd.CmdArgs;
import com.hepolite.api.exception.ArgumentParseException;
import com.hepolite.api.user.IUser;

public final class CmdStringElement
{
	public final static class One extends CmdKeyElement
	{
		public One(final String key)
		{
			super(key);
		}

		@Override
		public Object parseValue(final IUser user, final CmdArgs args) throws ArgumentParseException
		{
			return args.consumeArg();
		}
	}

	public final static class Remaining extends CmdKeyElement
	{
		public Remaining(final String key)
		{
			super(key);
		}

		@Override
		public Object parseValue(final IUser user, final CmdArgs args) throws ArgumentParseException
		{
			final StringBuilder builder = new StringBuilder();
			while (args.hasArg())
			{
				builder.append(args.consumeArg());
				if (args.hasArg())
					builder.append(' ');
			}
			return builder.toString();
		}
	}
}
