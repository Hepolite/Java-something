package com.hepolite.api.attribute;

import java.util.HashMap;
import java.util.Map;

import com.hepolite.api.user.IUser;

public final class AttributeDatabase
{
	private static final Map<IUser, AttributeMap> data = new HashMap<>();

	/**
	 * Attempts to retrieve the attribute under the given user and name. If the attribute does not
	 * exist, the attribute is created.
	 * 
	 * @param user The user the attribute applies to
	 * @param attribute The name of the attribute to look up
	 */
	public static Attribute get(final IUser user, final String attribute)
	{
		if (!data.containsKey(user))
			data.put(user, new AttributeMap());
		final AttributeMap map = data.get(user);
		if (!map.containsKey(attribute))
			map.put(attribute, new Attribute());
		return map.get(attribute);
	}
}
