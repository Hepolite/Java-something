package com.hepolite.api.cmd.element;

import com.hepolite.api.cmd.CmdArgs;
import com.hepolite.api.cmd.ICmdContext;
import com.hepolite.api.exception.ArgumentParseException;
import com.hepolite.api.user.IUser;

public interface ICmdElement
{
	/**
	 * Parses the entire section of arguments, storing all relevant data in the command context.
	 * 
	 * @param user The user performing the command
	 * @param args The arguments specified by the user
	 * @param context The command context
	 * @throws ArgumentParseException When any parsing error occurs
	 */
	public void parse(IUser user, CmdArgs args, ICmdContext context) throws ArgumentParseException;

	/**
	 * Attempt to extract a value for this element from the given arguments. If the element does not
	 * directly store a value, this method does not typically need to be implemented.
	 * 
	 * @param user The user performing the command
	 * @param args The arguments specified by the user
	 * @return The value that was obtained from the arguments
	 * @throws ArgumentParseException When any parsing error occurs
	 */
	public abstract Object parseValue(IUser user, CmdArgs args) throws ArgumentParseException;
}
