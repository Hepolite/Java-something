package com.hepolite.api.user;

import java.util.UUID;

import org.bukkit.entity.Player;

public final class UserFactory
{
	/**
	 * Constructs a new user instance from the given uuid
	 * 
	 * @param uuid The uuid of the user
	 * @return The new user instance
	 */
	public static IUser fromUUID(final UUID uuid)
	{
		if (uuid == null)
			throw new IllegalArgumentException("UUID cannot be null");

		if (uuid.getLeastSignificantBits() == 0 && uuid.getMostSignificantBits() == 0)
			return new ConsoleUser();
		else
			return new EntityUser(uuid);
	}
	/**
	 * Constructs a new user instance from the given player
	 * 
	 * @param player The player of the users
	 * @return The new user instance
	 */
	public static IUser fromPlayer(final Player player)
	{
		return fromUUID(player.getUniqueId());
	}
}
