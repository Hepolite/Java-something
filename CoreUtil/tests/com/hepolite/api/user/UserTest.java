package com.hepolite.api.user;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.hepolite.api.user.ConsoleUser;

class UserTest
{
	@Test
	void testConsoleUser()
	{
		final ConsoleUser user = new ConsoleUser();

		assertEquals("00000000-0000-0000-0000-000000000000", user.getUUID().toString());
		assertEquals("CONSOLE", user.getName());
		assertEquals("CONSOLE", user.getNameUnformatted());

		assertTrue(user.hasPermission("*"));
		assertTrue(user.isOnline());

		assertFalse(user.getEntity().isPresent());
		assertFalse(user.getPlayer().isPresent());
		assertFalse(user.getPlayerOffline().isPresent());
		assertFalse(user.getLocation().isPresent());
	}
}
