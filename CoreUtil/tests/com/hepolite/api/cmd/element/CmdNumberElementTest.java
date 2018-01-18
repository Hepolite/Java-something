package com.hepolite.api.cmd.element;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.hepolite.api.cmd.CmdArgs;
import com.hepolite.api.cmd.GenericArgs;
import com.hepolite.api.exception.ArgumentParseException;
import com.hepolite.api.user.EntityUser;
import com.hepolite.api.user.IUser;

class CmdNumberElementTest
{
	private final IUser user = new EntityUser();

	@Test
	void testValueParsing()
	{
		final class Inner
		{
			public final <T> void test(final ICmdElement element, final List<String> args, final List<T> expected)
			{
				final CmdArgs cmdArgs = new CmdArgs(args);
				assertEquals(expected.get(0), element.parseValue(user, cmdArgs));
				assertEquals(expected.get(1), element.parseValue(user, cmdArgs));
				assertEquals(expected.get(2), element.parseValue(user, cmdArgs));
				assertThrows(ArgumentParseException.class, () -> element.parseValue(user, cmdArgs));
			}
		}

		final Inner inner = new Inner();

		inner.test(GenericArgs.byteNum(""),
				Arrays.asList("42", "0b101", "0x1F", "512"),
				Arrays.asList((byte) 42, (byte) 0b101, (byte) 0x1F)
		);
		inner.test(GenericArgs.doubleNum(""),
				Arrays.asList("0.13", "616.016", "5", "0x6"),
				Arrays.asList(0.13, 616.016, 5.0)
		);
		inner.test(GenericArgs.floatNum(""),
				Arrays.asList("3.14", "0.4", "0.1234", "0b1001"),
				Arrays.asList(3.14f, 0.4f, 0.1234f)
		);
		inner.test(GenericArgs.intNum(""),
				Arrays.asList("42", "0b10010110101110", "0x6BA6F72", "5379269363927692"),
				Arrays.asList(42, 0b10010110101110, 0x6BA6F72)
		);
		inner.test(GenericArgs.longNum(""),
				Arrays.asList("415639651612", "0b100101110110", "0x58D592CBA2EB", "world"),
				Arrays.asList(415639651612L, 0b100101110110L, 0x58D592CBA2EBL)
		);
		inner.test(GenericArgs.shortNum(""),
				Arrays.asList("12151", "0b0111101", "0xB5A", "whatever"),
				Arrays.asList((short) 12151, (short) 0b0111101, (short) 0xB5A)
		);
	}
}
