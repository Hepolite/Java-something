package com.hepolite.api.cmd.parse;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.hepolite.api.cmd.CmdArgs;
import com.hepolite.api.cmd.parse.CmdStringArgParser.Inner;

class CmdStringArgParserTest
{
	@Test
	void testParsing()
	{
		final ICmdArgParser parser = new CmdStringArgParser();

		final CmdArgs argsA = parser.parse("example command arguments");
		final CmdArgs argsB = parser.parse("this \"has some\" inner \"string in addition\" to many other words");

		assertSame(3, argsA.getArgCount());
		assertSame(8, argsB.getArgCount());
	}

	@Test
	void testParserEmptyString()
	{
		final Inner inner = new Inner("");

		assertFalse(inner.next().isPresent());
	}

	@Test
	void testParserSinglePart()
	{
		final Inner inner = new Inner("string");

		final Optional<String> dataA = inner.next();
		final Optional<String> dataB = inner.next();

		assertTrue(dataA.isPresent());
		assertEquals("string", dataA.get());
		assertFalse(dataB.isPresent());
	}

	@Test
	void testParserMultipleParts()
	{
		final Inner inner = new Inner("some data");

		final Optional<String> dataA = inner.next();
		final Optional<String> dataB = inner.next();
		final Optional<String> dataC = inner.next();

		assertTrue(dataA.isPresent());
		assertEquals("some", dataA.get());
		assertTrue(dataB.isPresent());
		assertEquals("data", dataB.get());
		assertFalse(dataC.isPresent());
	}

	@Test
	void testParserString()
	{
		final Inner inner = new Inner("\"inner string\"");

		final Optional<String> dataA = inner.next();
		final Optional<String> dataB = inner.next();

		assertTrue(dataA.isPresent());
		assertEquals("inner string", dataA.get());
		assertFalse(dataB.isPresent());
	}

	@Test
	void testParserMultipleStrings()
	{
		final Inner inner = new Inner("\"this is a\" \"test with more stuff\"");

		final Optional<String> dataA = inner.next();
		final Optional<String> dataB = inner.next();
		final Optional<String> dataC = inner.next();

		assertTrue(dataA.isPresent());
		assertEquals("this is a", dataA.get());
		assertTrue(dataB.isPresent());
		assertEquals("test with more stuff", dataB.get());
		assertFalse(dataC.isPresent());
	}

	@Test
	void testParserMixedStuff()
	{
		final Inner inner = new Inner("\"stringed string\" \"and more\" with additional \"parameters\"");

		final Optional<String> dataA = inner.next();
		final Optional<String> dataB = inner.next();
		final Optional<String> dataC = inner.next();
		final Optional<String> dataD = inner.next();
		final Optional<String> dataE = inner.next();
		final Optional<String> dataF = inner.next();

		assertTrue(dataA.isPresent());
		assertEquals("stringed string", dataA.get());
		assertTrue(dataB.isPresent());
		assertEquals("and more", dataB.get());
		assertTrue(dataC.isPresent());
		assertEquals("with", dataC.get());
		assertTrue(dataD.isPresent());
		assertEquals("additional", dataD.get());
		assertTrue(dataE.isPresent());
		assertEquals("parameters", dataE.get());
		assertFalse(dataF.isPresent());
	}

	@Test
	void testIllegalOperations()
	{
		assertThrows(IllegalArgumentException.class, () -> new Inner(null));
	}
}
