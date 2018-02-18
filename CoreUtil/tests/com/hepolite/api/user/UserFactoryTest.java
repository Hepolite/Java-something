package com.hepolite.api.user;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.hepolite.api.user.ConsoleUser;
import com.hepolite.api.user.EntityUser;
import com.hepolite.api.user.IUser;
import com.hepolite.api.user.UserFactory;

class UserFactoryTest
{
	@Test
	void testFromUUID()
	{
		final IUser consoleUser = UserFactory.fromUUID(new UUID(0, 0));
		final IUser entityUser = UserFactory.fromUUID(UUID.randomUUID());

		assertTrue(consoleUser instanceof ConsoleUser);
		assertTrue(entityUser instanceof EntityUser);
		assertThrows(IllegalArgumentException.class, () -> UserFactory.fromUUID((UUID) null));
		assertThrows(IllegalArgumentException.class, () -> UserFactory.fromUUID((String) null));
	}
}
