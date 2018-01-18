package com.hepolite.api.cmd.parse;

import com.hepolite.api.cmd.CmdArgs;
import com.hepolite.api.exception.ArgumentParseException;

public interface ICmdArgParser
{
	/**
	 * Parses the given string into the arguments. The string is split according rules decided by
	 * the implementation.
	 * 
	 * @param raw The raw command to split into arguments
	 * @return The parsed arguments
	 * @throws ArgumentParseException When parsing fails
	 */
	public CmdArgs parse(String raw) throws ArgumentParseException;
}
