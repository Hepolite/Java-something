package com.hepolite.api.cmd.element;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.hepolite.api.cmd.CmdArgs;
import com.hepolite.api.exception.ArgumentParseException;
import com.hepolite.api.user.IUser;

public class CmdNumberElement<T extends Number> extends CmdKeyElement
{
	private final Function<String, T> parser;
	private final BiFunction<String, Integer, T> parserRadix;
	private final String errorMessage;

	public CmdNumberElement(final String key, final Function<String, T> parser,
			final BiFunction<String, Integer, T> parserRadix, final String errorMessage)
	{
		super(key);
		this.parser = parser;
		this.parserRadix = parserRadix;
		this.errorMessage = errorMessage;
	}

	@Override
	public Object parseValue(final IUser user, final CmdArgs args) throws ArgumentParseException
	{
		final String arg = args.consumeArg();
		try
		{
			if (parserRadix != null)
			{
				if (arg.startsWith("0x"))
					return parserRadix.apply(arg.substring(2), 16);
				if (arg.startsWith("0b"))
					return parserRadix.apply(arg.substring(2), 2);
			}
			return parser.apply(arg);
		}
		catch (final NumberFormatException e)
		{
			throw new ArgumentParseException(String.format(errorMessage, arg));
		}
	}
}
