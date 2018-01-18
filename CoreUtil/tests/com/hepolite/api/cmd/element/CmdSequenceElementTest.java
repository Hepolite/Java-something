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

class CmdSequenceElementTest
{
	private final IUser user = new EntityUser();
	private final List<String> args = Arrays.asList("5", "word", "3.14");

	@Test
	void testSequencing()
	{
		final ICmdElement element = GenericArgs.sequence(GenericArgs.intNum("int"), GenericArgs.string("string"),
				GenericArgs.floatNum("float"));
		final CmdArgs cmdArgs = new CmdArgs(args);
		final CmdContext context = new CmdContext();

		element.parse(user, cmdArgs, context);

		assertEquals(5, context.get("int").get());
		assertEquals("word", context.get("string").get());
		assertEquals(3.14f, context.get("float").get());
	}
}
