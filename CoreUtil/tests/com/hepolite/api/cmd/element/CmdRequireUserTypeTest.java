package com.hepolite.api.cmd.element;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.hepolite.api.cmd.CmdArgs;
import com.hepolite.api.cmd.CmdContext;
import com.hepolite.api.cmd.GenericArgs;
import com.hepolite.api.exception.ArgumentParseException;
import com.hepolite.api.user.ConsoleUser;
import com.hepolite.api.user.EntityUser;
import com.hepolite.api.user.IUser;

class CmdRequireUserTypeTest
{
	private final IUser userA = new ConsoleUser();
	private final IUser userB = new EntityUser();
	private final List<String> args = Arrays.asList();

	@Test
	void testRequiredUser()
	{
		final ICmdElement elementA = GenericArgs.requireConsole();
		final ICmdElement elementB = GenericArgs.requirePlayer();
		final CmdArgs cmdArgs = new CmdArgs(args);
		final CmdContext context = new CmdContext();

		elementA.parse(userA, cmdArgs, context);
		elementB.parse(userB, cmdArgs, context);
		assertThrows(ArgumentParseException.class, () -> elementA.parse(userB, cmdArgs, context));
		assertThrows(ArgumentParseException.class, () -> elementB.parse(userA, cmdArgs, context));
	}
}
