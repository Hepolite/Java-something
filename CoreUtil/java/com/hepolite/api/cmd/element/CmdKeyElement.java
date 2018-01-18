package com.hepolite.api.cmd.element;

import com.hepolite.api.cmd.CmdArgs;
import com.hepolite.api.cmd.ICmdContext;
import com.hepolite.api.exception.ArgumentParseException;
import com.hepolite.api.user.IUser;

public abstract class CmdKeyElement implements ICmdElement
{
	private final String key;

	/** @param key The key from which the values parsed by this element will be stored */
	protected CmdKeyElement(final String key)
	{
		if (key == null)
			throw new IllegalArgumentException("Key cannot be null");
		this.key = key;
	}

	/** @return The key associated with this element */
	public final String getKey()
	{
		return key;
	}

	@Override
	public void parse(final IUser user, final CmdArgs args, final ICmdContext context) throws ArgumentParseException
	{
		final Object value = parseValue(user, args);
		if (value == null)
			throw new ArgumentParseException("Parsed value cannot be null");

		if (value instanceof Iterable<?>)
		{
			for (final Object object : (Iterable<?>) value)
				context.put(key, object);
		}
		else
			context.put(key, value);
	}
}
