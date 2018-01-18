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
		final Attribute attributeA = AttributeDatabase.get(userA, AttributeType.HEALTH);
		final Attribute attributeB = AttributeDatabase.get(userA, AttributeType.HEALTH);
		final Attribute attributeC = AttributeDatabase.get(userA, AttributeType.HEALTH_MAX);
		final Attribute attributeD = AttributeDatabase.get(userB, AttributeType.HEALTH_MAX);

		assertNotNull(attributeA);
		assertNotNull(attributeC);
		assertNotNull(attributeD);
		assertSame(attributeA, attributeB);
		assertNotSame(attributeA, attributeC);
		assertNotSame(attributeC, attributeD);
	}
}
