package com.hepolite.api.attribute;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.hepolite.api.user.EntityUser;
import com.hepolite.api.user.IUser;

class AttributeDatabaseTest
{
	@Test
	void testGet()
	{
		final IUser userA = new EntityUser();
		final IUser userB = new EntityUser();
		final Attribute attributeA = AttributeDatabase.get(userA, AttributeType.HUNGER_MAX);
		final Attribute attributeB = AttributeDatabase.get(userA, AttributeType.HUNGER_MAX);
		final Attribute attributeC = AttributeDatabase.get(userA, AttributeType.SPEED_WALK);
		final Attribute attributeD = AttributeDatabase.get(userB, AttributeType.SPEED_WALK);

		assertNotNull(attributeA);
		assertNotNull(attributeC);
		assertNotNull(attributeD);
		assertSame(attributeA, attributeB);
		assertNotSame(attributeA, attributeC);
		assertNotSame(attributeC, attributeD);
	}
}
