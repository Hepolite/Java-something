package com.hepolite.api.cmd;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class CmdContextTest
{
	@Test
	void testArgInserting()
	{
		final ICmdContext context = new CmdContext();

		assertFalse(context.has("key"));
		context.put("key", 0);
		assertTrue(context.has("key"));
	}

	@Test
	void testArgInsertingAndRetrieving()
	{
		final ICmdContext context = new CmdContext();

		final class Inner
		{
			public <T> void test(final String key, final T value)
			{
				context.put(key, value);
				final Optional<T> optional = context.get(key);
				assertTrue(optional.isPresent());
				assertEquals(value, optional.get());
			}
		}

		final Inner inner = new Inner();
		inner.test("float", 3.14f);
		inner.test("int", 42);
		inner.test("double", -156167.0);
		inner.test("string", "Hello World!");
	}

	@Test
	void testMultipleArgs()
	{
		final ICmdContext context = new CmdContext();

		context.put("key", 1);
		context.put("key", 42);
		context.put("key", 1337);

		final Collection<Integer> args = context.getAll("key");
		final Iterator<Integer> it = args.iterator();

		assertSame(3, args.size());
		assertEquals((Integer) 1, it.next());
		assertEquals((Integer) 42, it.next());
		assertEquals((Integer) 1337, it.next());
	}

	@Test
	void testIllegalOperations()
	{
		final ICmdContext context = new CmdContext();

		assertThrows(IllegalArgumentException.class, () -> context.put("key", null));
	}
}
