package com.hepolite.api.cmd;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.hepolite.api.cmd.parse.CmdStringArgParser;
import com.hepolite.api.cmd.parse.ICmdArgParser;
import com.hepolite.api.exception.ArgumentParseException;
import com.hepolite.api.user.ConsoleUser;
import com.hepolite.api.user.IUser;
import com.hepolite.api.user.UserFactory;

public class CmdDispatcher
{
	/**
	 * Quick and dirty wrapper from Spigot-styled command execution to Sponge-styled command
	 * execution
	 * 
	 * @param sender The command invoker
	 * @param command The command to be invoked
	 * @param args Raw arguments
	 * @return True if the command was successfully executed
	 */
	public static boolean dispatch(final CommandSender sender, final Cmd command, final String[] args)
	{
		final IUser user;
		if (sender instanceof ConsoleCommandSender)
			user = new ConsoleUser();
		else if (sender instanceof Player)
			user = UserFactory.fromPlayer((Player) sender);
		else
			user = null;

		final String rawInput = String.join(" ", args);
		final ICmdArgParser parser = new CmdStringArgParser();
		try
		{
			final CmdArgs cmdArgs = parser.parse(rawInput);
			final ICmdContext context = new CmdContext(command);
			context.getCommand().getCommandElement().parse(user, cmdArgs, context);
			cmdArgs.throwOnUnconsumedArgs();
			if (user.hasPermission(context.getCommand().getPermission().orElse("")))
				context.getCommand().execute(user, context);
			else
				sender.sendMessage("You are not allowed to use that command.");
		}
		catch (final ArgumentParseException | CommandException e)
		{
			sender.sendMessage(e.getMessage());
		}
		return true;
	}
}
