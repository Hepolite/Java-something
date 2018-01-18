package com.hepolite.api.cmd;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class CmdArgsTest
{
	private final List<String> argsA = Arrays.asList("Buenos Aires", "Córdoba", "La Plata");

	@Test
	void testSimpleGetters()
	{
		final CmdArgs cmdArgs = new CmdArgs(argsA);

		assertFalse(cmdArgs.isEmpty());
		assertSame(3, cmdArgs.getArgCount());
	}

	@Test
	void testHasArgs()
	{
		final CmdArgs cmdArgs = new CmdArgs(argsA);

		assertTrue(cmdArgs.hasArg());
		cmdArgs.consumeArg();
		assertTrue(cmdArgs.hasArg());
		cmdArgs.consumeArg();
		assertTrue(cmdArgs.hasArg());
		cmdArgs.consumeArg();
		assertFalse(cmdArgs.hasArg());
	}

	@Test
	void testArgumentConsumption()
	{
		final CmdArgs cmdArgs = new CmdArgs(argsA);

		assertTrue(cmdArgs.hasArg());
		final String argA = cmdArgs.consumeArg();
		assertTrue(cmdArgs.hasArg());
		final String argB = cmdArgs.consumeArg();
		assertTrue(cmdArgs.hasArg());
		final String argC = cmdArgs.consumeArg();
		assertFalse(cmdArgs.hasArg());

		assertEquals("Buenos Aires", argA);
		assertEquals("Córdoba", argB);
		assertEquals("La Plata", argC);
	}

	@Test
	void testMultipleArgumentConsumption()
	{
		final CmdArgs cmdArgs = new CmdArgs(argsA);

		final Optional<String[]> arg = cmdArgs.consumeArgs(3);
		final String[] raw = arg.orElse(new String[] {});

		assertTrue(arg.isPresent());
		assertSame(3, raw.length);
		assertEquals("Buenos Aires", raw[0]);
		assertEquals("Córdoba", raw[1]);
		assertEquals("La Plata", raw[2]);
	}

	@Test
	void testSnapshotTaking()
	{
		final CmdArgs cmdArgs = new CmdArgs(argsA);

		final CmdArgsSnapshot snapshotA = cmdArgs.getSnapshot();
		cmdArgs.consumeArg();
		final CmdArgsSnapshot snapshotB = cmdArgs.getSnapshot();

		assertSame(0, snapshotA.index);
		assertSame(1, snapshotB.index);
	}

	@Test
	void testSnapshotRestoring()
	{
		final CmdArgs cmdArgs = new CmdArgs(argsA);

		cmdArgs.consumeArg();
		final CmdArgsSnapshot snapshotA = cmdArgs.getSnapshot();
		cmdArgs.consumeArg();

		cmdArgs.restoreSnapshot(snapshotA);
		final CmdArgsSnapshot snapshotB = cmdArgs.getSnapshot();

		assertSame(1, snapshotB.index);
	}
}
