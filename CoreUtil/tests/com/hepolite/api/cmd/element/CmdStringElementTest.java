package com.hepolite.api.cmd.element;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.hepolite.api.cmd.CmdArgs;
import com.hepolite.api.cmd.GenericArgs;
import com.hepolite.api.exception.ArgumentParseException;
import com.hepolite.api.user.EntityUser;
import com.hepolite.api.user.IUser;

class CmdStringElementTest
{
	private final IUser user = new EntityUser();
	private final List<String> args = Arrays.asList("these", "are", "arguments");

	@Test
	void testValueParsing()
	{
		final ICmdElement element = GenericArgs.string("key");
		final CmdArgs cmdArgs = new CmdArgs(args);

		assertEquals("these", element.parseValue(user, cmdArgs));
		assertEquals("are", element.parseValue(user, cmdArgs));
		assertEquals("arguments", element.parseValue(user, cmdArgs));
	}

	@Test
	void testIllegalOperations()
	{
		final ICmdElement element = GenericArgs.string("key");
		final CmdArgs cmdArgs = new CmdArgs(new ArrayList<String>());

		assertThrows(ArgumentParseException.class, () -> element.parseValue(user, cmdArgs));
	}
}
