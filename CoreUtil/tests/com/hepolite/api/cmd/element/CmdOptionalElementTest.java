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

class CmdOptionalElementTest
{
	private final IUser user = new EntityUser();
	private final List<String> args = Arrays.asList("word");

	@Test
	void testOptional()
	{
		final ICmdElement elementA = GenericArgs.optional(GenericArgs.intNum("int"));
		final ICmdElement elementB = GenericArgs.optional(GenericArgs.string("string"));
		final CmdArgs cmdArgs = new CmdArgs(args);
		final CmdContext context = new CmdContext();

		elementA.parse(user, cmdArgs, context);
		elementB.parse(user, cmdArgs, context);

		assertFalse(context.get("int").isPresent());
		assertTrue(context.get("string").isPresent());
		assertEquals("word", context.get("string").get());
	}
}
