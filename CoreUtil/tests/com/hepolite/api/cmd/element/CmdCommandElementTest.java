package com.hepolite.api.cmd.element;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.hepolite.api.cmd.CmdArgs;
import com.hepolite.api.cmd.CmdContext;
import com.hepolite.api.cmd.EmptyCommand;
import com.hepolite.api.cmd.GenericArgs;
import com.hepolite.api.cmd.ICmd;
import com.hepolite.api.user.EntityUser;
import com.hepolite.api.user.IUser;

class CmdCommandElementTest
{
	private final IUser user = new EntityUser();
	private final List<String> argsA = Arrays.asList("test", "data", "moredata");
	private final List<String> argsB = Arrays.asList("scenario", "test", "arguments");

	@Test
	void testInitialChild()
	{
		final ICmd command = new EmptyCommand("test");
		final ICmdElement element = GenericArgs.sequence(GenericArgs.children(command), GenericArgs.string("data"),
				GenericArgs.string("moredata"));
		final CmdArgs cmdArgs = new CmdArgs(argsA);
		final CmdContext context = new CmdContext();

		element.parse(user, cmdArgs, context);

		assertEquals(command, context.getCommand());
		assertEquals("data", context.get("data").get());
		assertEquals("moredata", context.get("moredata").get());
	}

	@Test
	void testLaterChild()
	{
		final ICmd command = new EmptyCommand("test");
		final ICmdElement element = GenericArgs.sequence(GenericArgs.string("scenario"), GenericArgs.children(command),
				GenericArgs.string("arguments"));
		final CmdArgs cmdArgs = new CmdArgs(argsB);
		final CmdContext context = new CmdContext();

		element.parse(user, cmdArgs, context);

		assertEquals(command, context.getCommand());
		assertEquals("scenario", context.get("scenario").get());
		assertEquals("arguments", context.get("arguments").get());
	}
}
