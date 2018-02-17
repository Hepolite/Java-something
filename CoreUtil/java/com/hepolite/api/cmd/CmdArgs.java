package com.hepolite.api.cmd;

import java.util.List;
import java.util.Optional;

import com.hepolite.api.exception.ArgumentParseException;

public final class CmdArgs
{
	private final List<String> args;
	private int index = 0;

	public CmdArgs(final List<String> args)
	{
		this.args = args;
	}

	/** @return The number of arguments stored within the structure */
	public final int getArgCount()
	{
		return args.size();
	}
	/** @return True iff the structure has no arguments in it */
	public final boolean isEmpty()
	{
		return args.isEmpty();
	}

	/** @return The snapshot of the current state of the arguments */
	public final CmdArgsSnapshot getSnapshot()
	{
		return new CmdArgsSnapshot(index);
	}
	/** Restores the state of the arguments to the given snapshot */
	public final void restoreSnapshot(final CmdArgsSnapshot snapshot)
	{
		if (snapshot == null || snapshot.index < 0 || snapshot.index > getArgCount())
			throw new IllegalArgumentException(String.format("Invalid or illegal snapshot (received %d, range 0-%d)",
					snapshot.index, getArgCount()));
		index = snapshot.index;
	}

	/** @return True iff there are more arguments available */
	public final boolean hasArg()
	{
		return index < getArgCount();
	}

	/**
	 * Pulls one string argument out of the arguments, if possible. If there are no more arguments
	 * available, an empty optional is returned
	 * 
	 * @return The argument if it exists
	 */
	public final String consumeArg() throws ArgumentParseException
	{
		if (index >= getArgCount())
			throw new ArgumentParseException("Not enough arguments provided");
		return args.get(index++);
	}
	/**
	 * Pulls multiple string arguments out of the arguments, if possible. If there are less
	 * arguments than the given amount available, an empty optional is returned
	 * 
	 * @param argCount The number of arguments to extract
	 * @return The argument if it exists
	 */
	public final Optional<String[]> consumeArgs(final int argCount)
	{
		if (index + argCount > getArgCount())
			return Optional.empty();

		final String[] data = new String[argCount];
		for (int i = 0; i < argCount; ++i)
			data[i] = args.get(index + i);
		index += argCount;
		return Optional.of(data);
	}

	/**
	 * If there are remaining arguments left to be consumed after all elements have been parsed, the
	 * user will be informed by this through the exception thrown from here
	 * 
	 * @throws ArgumentParseException The exception being thrown if more any arguments remain
	 *             unconsumed
	 */
	public final void throwOnUnconsumedArgs() throws ArgumentParseException
	{
		if (!hasArg())
			return;

		final StringBuilder message = new StringBuilder("Received too many arguments:");
		for (int i = index; i < getArgCount(); ++i)
			message.append(" ").append(args.get(i));
		throw new ArgumentParseException(message.toString());
	}
}
