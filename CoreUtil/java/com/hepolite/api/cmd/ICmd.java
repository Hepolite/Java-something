package com.hepolite.api.cmd;

import java.util.Optional;

import org.bukkit.command.CommandException;

import com.hepolite.api.cmd.element.ICmdElement;
import com.hepolite.api.user.IUser;

public interface ICmd
{
	/** @return The name of the command */
	public String getName();
	/** @return The permission needed to run the command */
	public Optional<String> getPermission();
	/** @return The root element for the command */
	public ICmdElement getCommandElement();

	/**
	 * Executes the command as the given user, with the given context.
	 * 
	 * @param user The user running the command
	 * @param context The context specified by the user
	 * @return True iff the command executed successfully
	 * @throws CommandException If the command received invalid input or unexpected or undefined
	 *             behavior is encountered
	 */
	public boolean execute(IUser user, ICmdContext context) throws CommandException;
}
