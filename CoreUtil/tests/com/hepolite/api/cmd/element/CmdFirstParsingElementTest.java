package com.hepolite.api.cmd.element;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.hepolite.api.cmd.CmdArgs;
import com.hepolite.api.cmd.CmdContext;
import com.hepolite.api.cmd.GenericArgs;
import com.hepolite.api.user.EntityUser;
import com.hepolite.api.user.IUser;

class CmdFirstParsingElementTest
{
	private final IUser user = new EntityUser();
	private final List<String> args = Arrays.asList("foo", "42");

	@Test
	void testFirstParsing()
	{
		final ICmdElement element = GenericArgs.firstParsing(GenericArgs.intNum("int"), GenericArgs.string("string"));
		final CmdArgs cmdArgs = new CmdArgs(args);
		final CmdContext context = new CmdContext();

		element.parse(user, cmdArgs, context);

		assertFalse(context.get("int").isPresent());
		assertTrue(context.get("string").isPresent());
		assertEquals("foo", context.get("string").get());
	}
}
