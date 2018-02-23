package com.hepolite.api.cmd.element;

import java.util.HashMap;
import java.util.Map;

import com.hepolite.api.cmd.CmdArgs;
import com.hepolite.api.cmd.ICmd;
import com.hepolite.api.cmd.ICmdContext;
import com.hepolite.api.exception.ArgumentParseException;
import com.hepolite.api.user.IUser;

public class CmdCommandElement implements ICmdElement
{
	private final Map<String, ICmd> commands = new HashMap<>();

	public CmdCommandElement(final ICmd... commands)
	{
		for (final ICmd command : commands)
			this.commands.put(command.getName(), command);
	}

	@Override
	public void parse(final IUser user, final CmdArgs args, final ICmdContext context) throws ArgumentParseException
	{
		final String arg = args.consumeArg();
		if (!commands.containsKey(arg))
			throw new ArgumentParseException(String.format("No child command match '%s'", arg));

		context.setCommand(commands.get(arg));
		context.getCommand().getCommandElement().parse(user, args, context);
	}
	@Override
	public Object parseValue(final IUser user, final CmdArgs args) throws ArgumentParseException
	{
		return null;
	}
}
