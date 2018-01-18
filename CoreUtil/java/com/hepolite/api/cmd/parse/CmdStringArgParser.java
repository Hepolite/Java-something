package com.hepolite.api.cmd.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.hepolite.api.cmd.CmdArgs;
import com.hepolite.api.exception.ArgumentParseException;

public class CmdStringArgParser implements ICmdArgParser
{
	@Override
	public final CmdArgs parse(final String raw) throws ArgumentParseException
	{
		final Inner inner = new Inner(raw);

		final List<String> args = new ArrayList<>();
		for (Optional<String> arg = inner.next(); arg.isPresent(); arg = inner.next())
			args.add(arg.get());
		return new CmdArgs(args);
	}

	public static final class Inner
	{
		private final String raw;
		private final int endIndex;
		private int index;
		private char separator = ' ';

		public Inner(final String raw)
		{
			if (raw == null)
				throw new IllegalArgumentException("Input string cannot be null");
			this.raw = raw;
			this.endIndex = raw.length();
		}

		public final Optional<String> next()
		{
			if (index >= endIndex)
				return Optional.empty();
			final int start = findStart(index);
			final int end = findEnd(start);
			index = end + 1;

			return Optional.of(findString(start, end));
		}

		private final int findStart(final int index)
		{
			final char next = raw.charAt(index);
			if (next == '"' || next == ' ')
			{
				separator = next;
				return findStart(index + 1);
			}
			return index;
		}
		private final int findEnd(final int start)
		{
			final int end = raw.indexOf(separator, start);
			if (end == -1)
				return endIndex;
			return end;
		}
		private final String findString(final int start, final int end)
		{
			if (end >= endIndex)
				return raw.substring(start);
			return raw.substring(start, end);
		}
	}
}
